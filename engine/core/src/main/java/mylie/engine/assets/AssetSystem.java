package mylie.engine.assets;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import mylie.engine.assets.exceptions.AssetException;
import mylie.engine.assets.exceptions.AssetNotFoundException;
import mylie.engine.assets.exceptions.AssetNotSupportedException;
import mylie.engine.assets.loaders.TextFileLoader;
import mylie.time.Time;

/**
 * The {@code AssetSystem} class is responsible for managing the lifecycle of assets in a system.
 * It provides functionality to handle asset loading, locating, importing, caching, tracking of
 * dependencies, and updating assets during runtime. This class acts as a centralized component for
 * coordinating interactions with asset locators, loaders, and keys.
 *
 * The class intends to streamline asset management tasks by ensuring efficient handling of asset
 * discovery, versioning, and resolution of dependencies. It supports extensibility by allowing
 * registration of custom asset locators and loaders.
 *
 * Key Features:
 * - Locate assets through a list of registered asset locators.
 * - Load and import assets using registered asset loaders.
 * - Cache loaded assets for efficiency.
 * - Respond to changes in assets and update dependencies.
 * - Register custom asset locators and loaders to expand functionality.
 * - Automatically update and monitor asset changes during runtime.
 */
@Slf4j
public final class AssetSystem {
	private final List<AssetLocator<?>> assetLocators = new ArrayList<>();
	private final List<AssetLoader<?, ?>> assetLoaders = new ArrayList<>();
	private final Map<AssetKey<?, ?>, Object> assetCache = new WeakHashMap<>();

	/**
	 * Constructs an instance of the AssetSystem.
	 * Initializes the AssetSystem with optional default loaders.
	 *
	 * @param loadDefaults a boolean flag indicating whether to add default loaders.
	 *                      If {@code true}, default loaders will be added during initialization.
	 */
	public AssetSystem(boolean loadDefaults) {
		log.trace("Initializing AssetSystem");
		if (loadDefaults) {
			addDefaultLoaders();
		}
	}

	/**
	 * Loads an asset identified by the provided asset key, leveraging asset location
	 * and importing mechanisms. If the asset is successfully loaded, additional processing
	 * takes place, and a log entry is created.
	 *
	 * @param <A>       the specific type of the asset to be loaded
	 * @param <K>       the type of the key used to identify the asset
	 * @param assetKey  the key identifying the asset to load
	 * @return          the loaded asset of type {@code A}
	 * @throws AssetNotFoundException       if the asset could not be located
	 * @throws AssetNotSupportedException   if the asset's file type is unsupported
	 * @throws AssetException               for unexpected errors during loading
	 */
	@SuppressWarnings("unchecked")
	public <A extends Asset<A, K>, K extends AssetKey<A, K>> A loadAsset(AssetKey<A, K> assetKey) {
		K key = (K) assetKey;
		A asset = (A) assetCache.get(key);
		if (asset == null) {
			AssetLocation<A, K> assetLocation = locateAsset(key);
			asset = importAsset(assetLocation);
			if (asset != null) {
				onAssetLoaded(asset, key);
			}
			log.trace("Asset {} loaded", asset);
		}
		return asset;
	}

	/**
	 * Locates the asset identified by the given key by searching through available asset locators.
	 * If the asset cannot be found, an {@code AssetNotFoundException} is thrown.
	 *
	 * @param <A>      the type of the asset being located.
	 * @param <K>      the type of the key used to identify the asset.
	 * @param assetKey the key uniquely identifying the desired asset.
	 * @return the {@code AssetLocation} for the specified asset, providing access to its content.
	 * @throws AssetNotFoundException if the asset could not be located by any of the registered locators.
	 */
	public <A extends Asset<A, K>, K extends AssetKey<A, K>> AssetLocation<A, K> locateAsset(K assetKey) {
		AssetLocation<A, K> assetLocation = null;
		for (AssetLocator<?> assetLocator : assetLocators) {
			assetLocation = assetLocator.locateAsset(assetKey);
			if (assetLocation != null) {
				break;
			}
		}
		if (assetLocation == null) {
			log.error("Asset {} not found", assetKey);
			throw new AssetNotFoundException(null, assetKey);
		}
		return assetLocation;
	}

	/**
	 * Called when an asset has successfully been loaded to update its key, cache it, and set its version.
	 *
	 * @param asset    the loaded asset.
	 * @param assetKey the key uniquely identifying the asset.
	 * @param <A>      the specific type of the asset.
	 * @param <K>      the type of the key identifying the asset.
	 */
	private <A extends Asset<A, K>, K extends AssetKey<A, K>> void onAssetLoaded(A asset, K assetKey) {
		asset.key(assetKey);
		assetCache.put(assetKey, asset);
		asset.version(Time.frameId());
	}

	/**
	 * Imports an asset based on the provided {@code AssetLocation}.
	 * This method iterates through registered asset loaders to find one that supports
	 * the asset's key type and invokes the respective loader to load the asset.
	 *
	 * @param assetLocation the {@link AssetLocation} of the asset to be imported,
	 *                      containing the asset key and its locator.
	 * @param <A>           the specific type of the asset being imported.
	 * @param <K>           the type of the key uniquely identifying the asset.
	 * @return the imported asset of type {@code A} if successfully loaded.
	 * @throws AssetNotSupportedException if no suitable asset loader is found for the asset key.
	 */
	@SuppressWarnings("unchecked")
	public <A extends Asset<A, K>, K extends AssetKey<A, K>> A importAsset(AssetLocation<A, K> assetLocation) {
		K assetKey = assetLocation.assetKey();
		for (AssetLoader<?, ?> assetLoader : assetLoaders) {
			if (assetLoader.isSupported(assetKey)) {
				return ((AssetLoader<A, K>) assetLoader).loadAsset(assetLocation);
			}
		}
		throw new AssetNotSupportedException(null, assetLocation.assetKey());
	}

	/**
	 * Performs periodic updates for all registered asset locators, checking for changes in assets.
	 */
	public void onUpdate() {
		for (AssetLocator<?> assetLocator : assetLocators) {
			assetLocator.onPollForChanges();
		}
	}

	/**
	 * Handles asset updates when an asset is changed by reloading the asset and notifying dependencies.
	 *
	 * @param assetKey the key of the asset that has changed.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void onAssetChanged(AssetKey<?, ?> assetKey) {
		log.trace("Asset {} changed", assetKey);
		if (assetCache.containsKey(assetKey)) {
			Asset<?, ?> asset = (Asset<?, ?>) assetCache.get(assetKey);
			loadAsset((AssetKey) assetKey);
			asset.version(asset.version() + 1);
			assetKey.dependingAssets().forEach(this::onAssetChanged);
		}
	}

	/**
	 * Registers a new asset locator for locating assets in the specified path with the provided options.
	 *
	 * @param locator the class of the asset locator to be registered.
	 * @param options the configuration options for the asset locator.
	 * @param path    the base path where the asset locator will operate.
	 * @param <L>     the type of the asset locator.
	 * @param <O>     the type of the locator options.
	 * @throws AssetException if the locator initialization fails.
	 */
	public <L extends AssetLocator<O>, O extends AssetLocator.Options> void addAssetLocator(Class<L> locator, O options,
			String path) {
		try {
			assetLocators.add(locator.getConstructor(AssetSystem.class, options.getClass(), String.class)
					.newInstance(this, options, path));
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException
				| NoSuchMethodException e) {
			log.error("Failed to initialized the locator: {}", options.getClass(), e);
			throw new AssetException(e);
		}
	}

	/**
	 * Registers a new asset loader for handling assets with the specified file extensions.
	 *
	 * @param loader         the class of the asset loader to be registered.
	 * @param fileExtensions the file extensions supported by the asset loader.
	 * @param <L>            the specific loader class type.
	 * @param <A>            the type of asset handled by the loader.
	 * @param <K>            the type of the key identifying the asset.
	 * @throws AssetException if the loader initialization fails.
	 */
	public <L extends AssetLoader<A, K>, A extends Asset<A, K>, K extends AssetKey<A, K>> void addAssetLoader(
			Class<L> loader, String... fileExtensions) {
		try {
			assetLoaders.add(loader.getConstructor(String[].class).newInstance((Object) fileExtensions));
		} catch (InstantiationException | InvocationTargetException | IllegalAccessException
				| NoSuchMethodException e) {
			log.error("Failed to initialized the loader: {}", loader, e);
			throw new AssetException(e);
		}
	}

	/**
	 * Adds a dependency between two assets, enabling tracking of changes in dependent assets.
	 *
	 * @param key        the key of the primary asset.
	 * @param dependency the key of the asset it depends on.
	 */
	public void addAssetDependency(AssetKey<?, ?> key, AssetKey<?, ?> dependency) {
		key.dependingAssets().add(dependency);
	}

	/**
	 * Registers default asset loaders to handle standard asset types.
	 */
	public void addDefaultLoaders() {
		addAssetLoader(TextFileLoader.class, "txt");
	}
}

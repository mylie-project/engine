package mylie.engine.assets;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import mylie.engine.assets.exceptions.AssetException;
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
public class AssetSystem {
	private final List<AssetLocator<?>> assetLocators = new ArrayList<>();
	private final List<AssetLoader<?, ?>> assetLoaders = new ArrayList<>();
	private final Map<AssetKey<?, ?>, Object> assetCache = new WeakHashMap<>();

	/**
	 * The {@code AssetSystem} class is responsible for managing the lifecycle
	 * of assets within the system. It serves as a central component for coordinating
	 * asset loading, locating, and tracking dependencies.
	 *
	 * This class establishes the infrastructure for interacting with other asset-related
	 * components such as asset loaders, locators, and keys. It enables organization and
	 * efficient management of assets in complex systems by abstracting operations like
	 * asset discovery, tracking, and versioning.
	 *
	 * By leveraging this class, systems can implement a robust asset management pipeline
	 * that caters to different asset types and ensures proper handling of dependencies
	 * and updates.
	 */
	public AssetSystem() {
		log.trace("Initializing AssetSystem");
	}

	/**
	 * Loads an asset using the specified asset key by locating, importing, and caching the asset.
	 *
	 * @param assetKey the key uniquely identifying the asset to be loaded.
	 * @param <A>      the specific type of the asset.
	 * @param <K>      the type of the key identifying the asset.
	 * @return the loaded asset of type {@code A}, or {@code null} if the asset could not be found or loaded.
	 */
	@SuppressWarnings("unchecked")
	public <A extends Asset<A, K>, K extends AssetKey<A, K>> A loadAsset(AssetKey<A, K> assetKey) {
		K key = (K) assetKey;
		AssetLocation<A, K> assetLocation = locateAsset(key);
		if (assetLocation != null) {
			A asset = importAsset(assetLocation);
			if (asset != null) {
				onAssetLoaded(asset, key);
			}
			log.trace("Asset {} loaded", asset);
			return asset;
		}
		return null;
	}

	/**
	 * Locates an asset using the specified key by querying all registered asset locators.
	 *
	 * @param assetKey the key uniquely identifying the asset.
	 * @param <A>      the specific type of the asset.
	 * @param <K>      the type of the key identifying the asset.
	 * @return the location of the asset, or {@code null} if the asset could not be located.
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
	 * Imports an asset using the provided asset location, attempting to find a suitable loader.
	 *
	 * @param assetLocation the location of the asset to be imported.
	 * @param <A>           the specific type of the asset.
	 * @param <K>           the type of the key identifying the asset.
	 * @return the imported asset of type {@code A}, or {@code null} if a suitable loader could not be found.
	 */
	@SuppressWarnings("unchecked")
	public final <A extends Asset<A, K>, K extends AssetKey<A, K>> A importAsset(AssetLocation<A, K> assetLocation) {
		K assetKey = assetLocation.assetKey();
		for (AssetLoader<?, ?> assetLoader : assetLoaders) {
			if (assetLoader.isSupported(assetKey)) {
				return ((AssetLoader<A, K>) assetLoader).loadAsset(assetLocation);
			}
		}
		log.error("No loader found for asset {}", assetKey);
		return null;
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
			loadAsset((AssetKey) assetKey);
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
	public void addDefaultAsserLoaders() {
		addAssetLoader(TextFileLoader.class, "txt");
	}
}

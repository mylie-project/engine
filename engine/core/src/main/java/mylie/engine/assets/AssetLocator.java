package mylie.engine.assets;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Base class for asset locators that manage the discovery and handling of assets.
 * <p>
 * Asset locators are responsible for finding, monitoring, and interacting with assets
 * using a specific strategy defined by their implementation.
 *
 * @param <T> The type of options used to configure this locator.
 */
@Getter(AccessLevel.PROTECTED)
public abstract class AssetLocator<T extends AssetLocator.Options> {
	/**
	 * The asset system to which this locator is attached.
	 * This provides the necessary context for asset management.
	 */
	final AssetSystem assetSystem;

	/**
	 * Configuration options for this locator, defining specific behaviors or settings.
	 */
	final T options;

	/**
	 * The base path where this locator searches for assets.
	 */
	final String path;

	/**
	 * Constructs a new instance of the asset locator with the specified parameters.
	 *
	 * @param assetSystem The asset system to use for managing asset-related operations.
	 * @param options     The options used to configure the locator's behavior.
	 * @param path        The base path where this locator starts searching for assets.
	 */
	protected AssetLocator(AssetSystem assetSystem, T options, String path) {
		this.assetSystem = assetSystem;
		this.options = options;
		this.path = path;
	}

	/**
	 * Periodically called to check for changes in the assets managed by this locator.
	 * <p>
	 * Subclasses can implement this method to monitor specific triggers, such as file system changes.
	 */
	protected abstract void onPollForChanges();

	/**
	 * Finds an asset's location based on its key.
	 * <p>
	 * This method allows subclasses to define the strategy for resolving asset paths or locations.
	 *
	 * @param assetKey The identifying key of the asset to locate.
	 * @param <A>      The specific asset type.
	 * @param <K>      The key type identifying the asset.
	 * @return A {@link AssetLocation} instance pointing to the asset, or {@code null}
	 * if the asset could not be found.
	 */
	protected abstract <A extends Asset<A, K>, K extends AssetKey<A, K>> AssetLocation<A, K> locateAsset(K assetKey);

	/**
	 * Marker interface for the configuration options used by {@link AssetLocator} instances.
	 * Subclasses of {@link AssetLocator} can extend this interface to add custom settings.
	 */
	public interface Options {

	}
}

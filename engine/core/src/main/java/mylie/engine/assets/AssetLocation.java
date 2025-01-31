package mylie.engine.assets;

import java.io.InputStream;
import lombok.Getter;

/**
 * Represents the location of an asset, combining an asset key with an asset locator.
 * This class defines how to derive an input stream for accessing the asset's content.
 *
 * @param <A> the specific type of the asset.
 * @param <K> the type of the key associated with the asset.
 */
@Getter
public abstract class AssetLocation<A extends Asset<A, K>, K extends AssetKey<A, K>> {
	/**
	 * The unique key identifying the associated asset.
	 */
	final AssetKey<A, K> assetKey;

	/**
	 * The locator used to find and retrieve the asset's contents.
	 */
	final AssetLocator<?> assetLocator;

	/**
	 * Constructs an AssetLocation with the specified asset key and locator.
	 *
	 * @param assetKey     the key uniquely identifying the associated asset.
	 * @param assetLocator the locator for accessing the asset's content.
	 */
	protected AssetLocation(AssetKey<A, K> assetKey, AssetLocator<?> assetLocator) {
		this.assetKey = assetKey;
		this.assetLocator = assetLocator;
	}

	/**
	 * Opens an input stream for reading the content of the asset.
	 *
	 * @return an input stream to access the asset content.
	 */
	public abstract InputStream open();
}

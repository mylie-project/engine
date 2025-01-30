package mylie.engine.assets.exceptions;

import lombok.Getter;
import mylie.engine.assets.AssetKey;

/**
 * An exception that is thrown when an asset cannot be found in the system.
 * This exception indicates that a referenced asset, identified by its key, does not exist.
 *
 * This exception is typically used in scenarios involving asset retrieval or loading
 * where the specified asset key does not correspond to any existing asset.
 */
@Getter
public class AssetNotFoundException extends AssetException {
	private final transient AssetKey<?, ?> assetKey;

	/**
	 * Constructs a new AssetNotFoundException with the specified cause and key.
	 *
	 * @param cause the underlying cause of this exception (can be null)
	 * @param assetKey the key that uniquely identifies the asset that could not be found
	 */
	public AssetNotFoundException(Throwable cause, AssetKey<?, ?> assetKey) {
		super(cause);
		this.assetKey = assetKey;
	}
}

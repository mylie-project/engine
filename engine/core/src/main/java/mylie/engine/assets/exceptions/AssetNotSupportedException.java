package mylie.engine.assets.exceptions;

import lombok.Getter;
import mylie.engine.assets.AssetKey;

/**
 * Exception thrown to indicate that a specific asset is not supported.
 *
 * This exception typically occurs when attempting to load or process an asset
 * whose type, format, or file extension is not recognized or supported by the system.
 * The unsupported asset is identified using an {@code AssetKey}.
 */
@Getter
public class AssetNotSupportedException extends AssetException {
	private final AssetKey<?, ?> key;

	/**
	 * Constructs a new AssetNotSupportedException with the specified cause and asset key.
	 *
	 * @param cause the underlying cause of this exception (can be null)
	 * @param key the key that uniquely identifies the asset which is not supported
	 */
	public AssetNotSupportedException(Throwable cause, AssetKey<?, ?> key) {
		super(cause);
		this.key = key;
	}
}

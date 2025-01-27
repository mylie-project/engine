package mylie.engine.assets.exceptions;

/**
 * Represents an exception specifically for asset-related errors in the
 * Mylie engine. It serves as a runtime exception base for asset-related issues.
 */
public class AssetException extends RuntimeException {

	/**
	 * Constructs a new AssetException with the specified cause.
	 *
	 * @param cause the underlying cause of this exception (can be null)
	 */
	public AssetException(Throwable cause) {
		super(cause);
	}
}

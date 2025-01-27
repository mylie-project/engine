package mylie.engine.assets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a generic asset that is identified by a key and can have a version number.
 *
 * @param <A> the specific type of the asset extending this class.
 * @param <K> the type of the key that uniquely identifies the asset.
 */
public abstract class Asset<A extends Asset<A, K>, K extends AssetKey<A, K>> {
	/**
	 * Constructs an instance of an {@code Asset}.
	 * This constructor is typically invoked during the creation of an asset
	 * that includes a unique identifying key and an optional version number.
	 */
	protected Asset() {
	}

	/**
	 * The unique key associated with this asset.
	 * This is set at the package level and not accessible publicly.
	 */
	@Setter(AccessLevel.PACKAGE)
	private K key;

	/**
	 * The version number of this asset.
	 * This is used to track changes or updates to the asset.
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private long version;
}

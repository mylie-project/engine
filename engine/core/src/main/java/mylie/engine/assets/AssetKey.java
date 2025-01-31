package mylie.engine.assets;

import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a key used to uniquely identify an {@link Asset}.
 *
 * @param <A> the type of the asset associated with this key.
 * @param <K> the specific type of the key, extending this class.
 */
@ToString
public abstract class AssetKey<A extends Asset<A, K>, K extends AssetKey<A, K>> {
	/**
	 * A set of assets that the current asset depends on.
	 * Dependencies are tracked to ensure proper management of related assets.
	 */
	@Getter(AccessLevel.PACKAGE)
	private final Set<AssetKey<?, ?>> dependingAssets = new HashSet<>();

	/**
	 * The path used to uniquely identify this asset in the system.
	 */
	@Getter
	private final String path;

	/**
	 * Constructs an {@code AssetKey} with the specified path.
	 *
	 * @param path the unique path to identify the asset.
	 */
	protected AssetKey(String path) {
		this.path = path.startsWith("/") ? path.substring(1) : path;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof AssetKey && ((AssetKey<?, ?>) obj).path.equals(path);
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}
}

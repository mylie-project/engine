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

	/**
	 * Extracts and returns the directory path from the complete asset path.
	 *
	 * The directory path refers to the portion of the path string up to,
	 * but not including, the last forward slash ("/").
	 *
	 * @return the directory path as a string derived from the asset's path
	 */
	public String directoryPath() {
		if(path.contains("/")) {
			return path.substring(0, path.lastIndexOf("/"));
		}else{
			return "";
		}
	}

	/**
	 * Extracts and returns the file name from the complete asset path.
	 *
	 * The file name is the portion of the path string starting after the last
	 * forward slash ("/") and excludes the file extension after the first dot (".").
	 *
	 * @return the file name as a string derived from the asset's path
	 */
	public String fileName(){
		String tmp = fileNameWithExtension();
		if(tmp.contains(".")) {
			return tmp.substring(0, tmp.indexOf("."));
		}else{
			return tmp;
		}
	}

	/**
	 * Extracts and returns the file extension from the file name in the asset's path.
	 *
	 * The file extension is the portion of the file name after the first dot (".").
	 * If no dot is present in the file name, this method may throw an exception
	 * due to an invalid substring operation.
	 *
	 * @return the file extension derived from the asset's file name
	 */
	public String fileExtension(){
		String tmp= fileNameWithExtension();
		if(tmp.contains(".")) {
			return tmp.substring(tmp.indexOf(".") + 1);
		}else{
			return "";
		}
	}

	/**
	 * Extracts and returns the file name along with its extension from the complete asset path.
	 *
	 * The file name with extension is the portion of the path string starting after the last
	 * forward slash ("/"). If the path does not contain any forward slashes, the entire path
	 * string is returned.
	 *
	 * @return the file name with its extension as a string derived from the asset's path
	 */
	private String fileNameWithExtension(){
		if(path.contains("/")) {
			return path.substring(path.lastIndexOf("/")+1);
		}else{
			return path;
		}
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

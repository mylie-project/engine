package mylie.engine.assets;

import java.util.HashSet;
import java.util.Set;

/**
 * A generic abstract class for loading assets of a specific type based on their key and file extensions.
 *
 * <p>This class provides functionality for checking if an asset's file extension is supported
 * and loading the asset based on its location.</p>
 *
 * @param <A> the specific type of asset this loader handles
 * @param <K> the type of key uniquely identifying the asset
 */
public abstract class AssetLoader<A extends Asset<A, K>, K extends AssetKey<A, K>> {
	final Set<String> fileExtensions = new HashSet<>();

	protected AssetLoader(String... fileExtensions) {
		this.fileExtensions.addAll(Set.of(fileExtensions));
	}

	/**
	 * Checks if the given asset key is supported based on its file extension.
	 *
	 * @param key the asset key containing the path to check
	 * @return {@code true} if the asset's file extension is supported, {@code false} otherwise
	 */
	protected boolean isSupported(AssetKey<?, ?> key) {
		for (String fileExtension : fileExtensions) {
			if (key.path().endsWith(fileExtension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads an asset based on its location.
	 *
	 * @param location the location of the asset to be loaded
	 * @return the loaded asset of type {@code A}
	 */
	protected abstract A loadAsset(AssetLocation<A, K> location);
}

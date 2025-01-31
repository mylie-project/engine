package mylie.engine.assets.locators;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.Getter;
import mylie.engine.assets.*;
import mylie.engine.assets.exceptions.AssetException;

/**
 * A concrete implementation of {@link AssetLocator} responsible for locating assets
 * within the classpath. This locator resolves assets by combining a predefined base
 * path within the classpath with the asset key's path to identify the resource.
 */
public class ClasspathLocator extends AssetLocator<ClasspathLocator.Options> {

	/**
	 * Constructs a ClasspathLocator instance to locate assets within the classpath.
	 *
	 * @param assetSystem The asset system to which this locator belongs.
	 * @param options Options configuring the behavior of the locator.
	 * @param path The base path within the classpath to be used for locating assets.
	 */
	public ClasspathLocator(AssetSystem assetSystem, Options options, String path) {
		super(assetSystem, options, path);
	}

	/**
	 * Polls for changes in the located resources within the classpath and takes appropriate actions.
	 *
	 * This method is intended to be overridden by subclasses to provide specific behaviors
	 * for detecting and handling changes to assets managed by the locator. By default, it
	 * does nothing and can be customized to respond to updates in the assets or related resources.
	 */
	@Override
	protected void onPollForChanges() {
		// evaluate the possiblity for classpath reloading?
	}

	/**
	 * Locates an asset by finding its corresponding resource within the classpath.
	 * The method constructs the full asset path by combining the locator's base
	 * path with the asset key's path, then attempts to resolve the resource.
	 *
	 * @param <A>      The type of the asset to locate.
	 * @param <K>      The type of the key that uniquely identifies the asset.
	 * @param assetKey The key uniquely identifying the asset to locate.
	 * @return An instance of {@code AssetLocation} representing the resolved location
	 *         of the asset if the resource is found, or {@code null} if the asset
	 *         could not be located.
	 */
	@Override
	protected <A extends Asset<A, K>, K extends AssetKey<A, K>> AssetLocation<A, K> locateAsset(
			AssetKey<A, K> assetKey) {
		String assetPath = path() + assetKey.path();
		URL resource = ClasspathLocator.class.getResource(assetPath);
		if (resource != null) {
			return new ClassPathLocation<>(assetKey, this, resource);
		}
		return null;
	}

	/**
	 * Represents a location in the classpath from which an asset can be accessed.
	 * This class specializes the {@link AssetLocation} to handle resources available
	 * on the classpath, leveraging a {@link URL} for locating the resource.
	 *
	 * @param <A> The type of the asset associated with this location.
	 * @param <K> The type of the key that uniquely identifies the asset.
	 */
	private static class ClassPathLocation<A extends Asset<A, K>, K extends AssetKey<A, K>>
			extends
				AssetLocation<A, K> {
		private final URL resource;

		/**
		 * Constructs an AssetLocation with the specified asset key, locator, and resource.
		 *
		 * @param assetKey     The key uniquely identifying the associated asset.
		 * @param assetLocator The locator for accessing the asset's content.
		 * @param resource     The {@link URL} resource representing the asset's location.
		 */
		protected ClassPathLocation(AssetKey<A, K> assetKey, AssetLocator<?> assetLocator, URL resource) {
			super(assetKey, assetLocator);
			this.resource = resource;
		}

		/**
		 * Opens an input stream to the {@link URL} resource associated with this location.
		 *
		 * @return An {@code InputStream} to read the resource content.
		 * @throws AssetException If an I/O error occurs while attempting to open the resource.
		 */
		@Override
		public InputStream open() {
			try {
				return resource.openStream();
			} catch (IOException e) {
				throw new AssetException(e);
			}
		}
	}

	/**
	 * Represents configuration options for the {@link ClasspathLocator}.
	 *
	 * This class serves as a specific implementation of the {@link AssetLocator.Options} marker
	 * interface, used to define behaviors or settings for the {@link ClasspathLocator}. It does
	 * not add any additional properties or functionality but provides a concrete implementation
	 * to satisfy the configuration needs for classpath-based asset locators.
	 */
	@Getter
	public static class Options implements AssetLocator.Options {

		/**
		 * Constructs an instance of the {@code Options} class with default values.
		 * <p>
		 * This class is a specific implementation of the {@link AssetLocator.Options} interface,
		 * intended to represent configuration options for the {@link ClasspathLocator}. It serves
		 * as a placeholder and does not introduce any additional functionality or properties.
		 */
		public Options() {
			// Nothing to be done
		}
	}
}

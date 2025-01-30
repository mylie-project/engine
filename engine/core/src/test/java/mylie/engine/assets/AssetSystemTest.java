package mylie.engine.assets;// java
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import mylie.engine.assets.exceptions.AssetNotFoundException;
import mylie.engine.assets.exceptions.AssetNotSupportedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssetSystemTest {

	private AssetSystem assetSystem;

	@BeforeEach
	void setUp() {
		// Pass false or true depending on your need to load default loaders
		assetSystem = new AssetSystem(true);
	}

	@Test
	void testLoadAsset_Success() {
		MockAsset.Key mockKey = new MockAsset.Key("notfound.mock");
		Assertions.assertThrows(AssetNotFoundException.class,
				() -> assetSystem.loadAsset(mockKey));
		assetSystem.addAssetLocator(MockAssetLocator.class, new MockAssetLocator.Options("notfound.mock"), "");
		Assertions.assertThrows(AssetNotSupportedException.class,
				() -> assetSystem.loadAsset(mockKey));
		assetSystem.addAssetLoader(MockAssetLoader.class, "mock");
		MockAsset asset = assetSystem.loadAsset(mockKey);
		Assertions.assertEquals("notfound.mock", asset.data);
	}

	@Test
	void testLoadAsset_NotFound() {
		Assertions.assertThrows(AssetNotFoundException.class,
				() -> assetSystem.loadAsset(new MockAsset.Key("notfound")));
	}

	private static class MockAssetLocator extends AssetLocator<MockAssetLocator.Options> {
		Set<String> validAssets = new HashSet<>();
		/**
		 * Constructs a new instance of the asset locator with the specified parameters.
		 *
		 * @param assetSystem The asset system to use for managing asset-related operations.
		 * @param options     The options used to configure the locator's behavior.
		 * @param path        The base path where this locator starts searching for assets.
		 */
		public MockAssetLocator(AssetSystem assetSystem, Options options, String path) {
			super(assetSystem, options, path);
			validAssets.addAll(Set.of(options.validAssets));
		}

		@Override
		protected void onPollForChanges() {

		}

		@Override
		protected <A extends Asset<A, K>, K extends AssetKey<A, K>> AssetLocation<A, K> locateAsset(K assetKey) {
			if (validAssets.contains(assetKey.path())) {
				return new AssetLocation<>(assetKey, this) {
					@Override
					public InputStream open() {
						return new ByteArrayInputStream(assetKey().path().getBytes(StandardCharsets.UTF_8));
					}
				};
			}
			return null;
		}

		static class Options implements AssetLocator.Options {
			final String[] validAssets;
			protected Options(String... validAssets) {
				this.validAssets = validAssets;
			}
		}
	}

	private static class MockAsset extends Asset<MockAsset, MockAsset.Key> {
		final String data;

		public MockAsset(String data) {
			this.data = data;
		}

		static class Key extends AssetKey<MockAsset, Key> {

			/**
			 * Constructs an {@code AssetKey} with the specified path.
			 *
			 * @param path the unique path to identify the asset.
			 */
			protected Key(String path) {
				super(path);
			}
		}
	}

	private static class MockAssetLoader extends AssetLoader<MockAsset, MockAsset.Key> {
		public MockAssetLoader(String... validExtensions) {
			super(validExtensions);
		}
		@Override
		protected MockAsset loadAsset(AssetLocation<MockAsset, MockAsset.Key> location) {
			try (InputStream inputStream = location.open()) {
				byte[] bytes = inputStream.readAllBytes();
				return new MockAsset(new String(bytes, StandardCharsets.UTF_8));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}

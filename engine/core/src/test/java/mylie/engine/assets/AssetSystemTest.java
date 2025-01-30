package mylie.engine.assets;// java
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import mylie.engine.assets.assets.TextFile;
import mylie.engine.assets.exceptions.AssetNotFoundException;
import mylie.engine.assets.exceptions.AssetNotSupportedException;
import mylie.engine.assets.locators.ClasspathLocator;
import mylie.engine.assets.locators.FileSystemLocator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

class AssetSystemTest {

	@TempDir
	static Path sharedTempDir;

	private AssetSystem assetSystem;

	@BeforeAll
	static void beforeAll() {
		writeFile("filesystem/info.txt", new String[]{"Hello", "World"});
	}

	@AfterAll
	static void afterAll() {
		sharedTempDir.toFile().deleteOnExit();
	}

	private static void writeFile(String fileName, String[] text) {
		Path path = sharedTempDir.resolve(fileName);
		path.getParent().toFile().mkdirs();
		try {
			File file = path.toFile();
			if (!file.exists()) {
				boolean newFile = file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			for (String s : text) {
				fileOutputStream.write(s.getBytes());
				fileOutputStream.write('\n');
			}
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@BeforeEach
	void setUp() {
		// Pass false or true depending on your need to load default loaders
		assetSystem = new AssetSystem(true);
	}

	@Test
	void testLoadAsset_Success() {
		MockAsset.Key mockKey = new MockAsset.Key("notfound.mock");
		Assertions.assertThrows(AssetNotFoundException.class, () -> assetSystem.loadAsset(mockKey));
		assetSystem.addAssetLocator(MockAssetLocator.class, new MockAssetLocator.Options("notfound.mock"), "");
		Assertions.assertThrows(AssetNotSupportedException.class, () -> assetSystem.loadAsset(mockKey));
		assetSystem.addAssetLoader(MockAssetLoader.class, "mock");
		MockAsset asset = assetSystem.loadAsset(mockKey);
		Assertions.assertEquals("notfound.mock", asset.data);
	}

	@Test
	void testLoadAsset_NotFound() {
		MockAsset.Key notfound = new MockAsset.Key("notfound");
		Assertions.assertThrows(AssetNotFoundException.class, () -> assetSystem.loadAsset(notfound));
	}

	@Test
	void testClassPathLoader() {
		assetSystem.addAssetLocator(ClasspathLocator.class, new ClasspathLocator.Options(), "/");
		assetSystem.loadAsset(new TextFile.Key("testfiles/info.txt"));
		TextFile.Key key = new TextFile.Key("testfiles/infoo.txt");
		Assertions.assertThrows(AssetNotFoundException.class, () -> assetSystem.loadAsset(key));
		Assertions.assertDoesNotThrow(() -> assetSystem.onUpdate());
	}

	@Test
	void testFileSystemLoader() {
		assetSystem.addAssetLocator(FileSystemLocator.class, new FileSystemLocator.Options(true), "/");
		TextFile.Key key = new TextFile.Key("filesystem/info.txt");
		Assertions.assertThrows(AssetNotFoundException.class, () -> assetSystem.loadAsset(key));
		Assertions.assertDoesNotThrow(() -> assetSystem.onUpdate());

		assetSystem.addAssetLocator(FileSystemLocator.class, new FileSystemLocator.Options(true),
				sharedTempDir.toString());
		TextFile textFile = assetSystem.loadAsset(key);
		String[] content = textFile.content();
		Assertions.assertEquals("Hello", content[0]);
		Assertions.assertEquals("World", content[1]);
		Assertions.assertEquals(0, textFile.version());
		Assertions.assertDoesNotThrow(() -> assetSystem.onUpdate());
		Assertions.assertEquals(0, textFile.version());
		writeFile("filesystem/info.txt", new String[]{"Edited"});
		Assertions.assertDoesNotThrow(() -> assetSystem.onUpdate());
		Assertions.assertEquals(1, textFile.version());
	}

	@Test
	void testHotReload() {
		Assertions.assertDoesNotThrow(() -> assetSystem.onUpdate());
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
			// Not yet tested
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

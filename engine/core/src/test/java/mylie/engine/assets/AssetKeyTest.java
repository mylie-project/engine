package mylie.engine.assets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AssetKeyTest {
	static class TestAsset extends Asset<TestAsset, TestAssetKey> {

	}
	static class TestAssetKey extends AssetKey<TestAsset, TestAssetKey> {
		protected TestAssetKey(String path) {
			super(path);
		}
	}

	/**
	 * Test for the directoryPath() method in the AssetKey class.
	 * This method extracts the directory portion of the path by removing the file name and its extension.
	 */

	@Test
	void testDirectoryPath_withSingleLevelPath() {
		AssetKey<?, ?> assetKey = new TestAssetKey("folder/file.txt");
		String expected = "folder";
		assertEquals(expected, assetKey.directoryPath());
	}

	@Test
	void testDirectoryPath_withNestedPath() {
		AssetKey<?, ?> assetKey = new TestAssetKey("folder/subfolder/file.txt");
		String expected = "folder/subfolder";
		assertEquals(expected, assetKey.directoryPath());
	}

	@Test
	void testDirectoryPath_withRootPath() {
		AssetKey<?, ?> assetKey = new TestAssetKey("/file.txt");
		String expected = "";
		assertEquals(expected, assetKey.directoryPath());
	}

	@Test
	void testDirectoryPath_withTrailingSlash() {
		AssetKey<?, ?> assetKey = new TestAssetKey("folder/");
		String expected = "folder"; // If the trailing slash is treated properly.
		assertEquals(expected, assetKey.directoryPath());
	}

	@Test
	void testDirectoryPath_withNoSlash() {
		AssetKey<?, ?> assetKey = new TestAssetKey("file.txt");
		String expected = ""; // No directory present.
		assertEquals(expected, assetKey.directoryPath());
	}

	@Test
	void testFileExtension() {
		AssetKey<?, ?> assetKey = new TestAssetKey("file.txt");
		String expected = "txt";
		assertEquals(expected, assetKey.fileExtension());
	}

	@Test
	void testDoubleFileExtension() {
		AssetKey<?, ?> assetKey = new TestAssetKey("file.txt.bin");
		String expected = "txt.bin";
		assertEquals(expected, assetKey.fileExtension());
	}

	@Test
	void testNoFileExtension() {
		AssetKey<?, ?> assetKey = new TestAssetKey("file");
		String expected = "";
		assertEquals(expected, assetKey.fileExtension());
	}

	@Test
	void testFileName() {
		AssetKey<?, ?> assetKey = new TestAssetKey("file.txt");
		String expected = "file";
		assertEquals(expected, assetKey.fileName());
	}

	@Test
	void testFileNameWithNoExtension() {
		AssetKey<?, ?> assetKey = new TestAssetKey("file");
		String expected = "file";
		assertEquals(expected, assetKey.fileName());
	}
}

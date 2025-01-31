package mylie.engine.assets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AssetKeyTest {
	static class TestAsset extends Asset<TestAsset, TestAssetKey> {

	}
	static class TestAssetKey extends AssetKey<TestAsset, TestAssetKey> {
		protected TestAssetKey(String path) {
			super(path);
		}
	}

	@ParameterizedTest
	@CsvSource({"folder/file.txt, folder", "folder/subfolder/file.txt, folder/subfolder", "/file.txt,''",
			"folder/, folder", "file.txt,''"})
	void testPath(String assedId, String expectedPath) {
		AssetKey<?, ?> assetKey = new TestAssetKey(assedId);
		assertEquals(expectedPath, assetKey.directoryPath());
	}

	@ParameterizedTest
	@CsvSource({"file.txt,txt", "file.txt.bin,txt.bin", "file,''"})
	void testFileExtension(String assedId, String expectedExtension) {
		AssetKey<?, ?> assetKey = new TestAssetKey(assedId);
		assertEquals(expectedExtension, assetKey.fileExtension());
	}

	@ParameterizedTest
	@CsvSource({"file.txt,file", "file.txt.bin,file", "file,file", "/home/file,file", "/home/file.bin,file"})
	void testFileName(String assedId, String expectedFileName) {
		AssetKey<?, ?> assetKey = new TestAssetKey(assedId);
		assertEquals(expectedFileName, assetKey.fileName());
	}

	@Test
	void testEqualsHashCode() {
		TestAssetKey testAssetKey = new TestAssetKey("file.txt");
		TestAssetKey testAssetKey2 = new TestAssetKey("file.txt");
		assertEquals(testAssetKey, testAssetKey2);
		assertEquals(testAssetKey.hashCode(), testAssetKey2.hashCode());
	}
}

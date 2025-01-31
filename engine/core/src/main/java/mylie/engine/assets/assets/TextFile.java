package mylie.engine.assets.assets;

import lombok.Getter;
import mylie.engine.assets.AssetKey;
import mylie.engine.assets.AssetReloadable;

/**
 * Represents a text file asset that contains an array of strings as its content.
 * This asset can be identified uniquely using its associated {@link TextFile.Key}.
 */
public class TextFile extends AssetReloadable<TextFile, TextFile.Key> {

	/**
	 * The content of the text file, stored as an array of strings.
	 */
	@Getter
	String[] content;

	/**
	 * Constructs a new {@code TextFile} with the specified content.
	 *
	 * @param content an array of strings representing the text content of the file
	 */
	public TextFile(String[] content) {
		this.content = content;
	}

	/**
	 * Updates the content of the current instance with the content
	 * from the newly loaded version of the {@code TextFile}.
	 *
	 * @param newVersion the new version of the {@code TextFile} containing
	 *                   the updated content to replace the current content
	 */
	@Override
	protected void onReload(TextFile newVersion) {
		this.content = newVersion.content;
	}

	/**
	 * Represents the unique key for identifying a {@link TextFile} asset.
	 */
	public static class Key extends AssetKey<TextFile, Key> {

		/**
		 * Constructs a new {@code Key} instance with the specified file path.
		 *
		 * @param path the file path associated with this key
		 */
		public Key(String path) {
			super(path);
		}
	}
}

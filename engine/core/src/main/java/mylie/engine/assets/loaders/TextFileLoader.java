package mylie.engine.assets.loaders;

import java.io.InputStream;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import mylie.engine.assets.AssetLoader;
import mylie.engine.assets.AssetLocation;
import mylie.engine.assets.assets.TextFile;
import mylie.engine.assets.exceptions.AssetException;

/**
 * Asset loader for text files. This loader reads text files and converts them into {@link TextFile} objects.
 * It supports specified file extensions and ensures the content is properly loaded as a list of strings.
 */
@Slf4j
public class TextFileLoader extends AssetLoader<TextFile, TextFile.Key> {
	/**
	 * Constructs a TextFileLoader that supports the provided file extensions.
	 *
	 * @param fileExtensions The file extensions this loader can handle (e.g., "txt", "csv").
	 */
	public TextFileLoader(String... fileExtensions) {
		super(fileExtensions);
	}

	/**
	 * Loads a {@link TextFile} asset from the specified {@link AssetLocation}.
	 * It reads the content from the input stream, line by line, into a list of strings.
	 *
	 * @param location The asset location containing the file to be loaded.
	 * @return A {@link TextFile} containing the loaded content as an array of strings.
	 * @throws AssetException If there is an error while reading the file.
	 */
	@Override
	protected TextFile loadAsset(AssetLocation<TextFile, TextFile.Key> location) {
		try (InputStream inputStream = location.open()) {
			List<String> lines = new ArrayList<>();
			Scanner scanner = new Scanner(inputStream);
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
			return new TextFile(lines.toArray(new String[0]));
		} catch (Exception e) {
			log.error("Error while reading from input stream");
			throw new AssetException(e);
		}
	}
}

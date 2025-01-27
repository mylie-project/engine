package mylie.engine.assets.locators;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.engine.assets.*;
import mylie.engine.assets.exceptions.AssetException;

/**
 * A locator for loading assets from the file system. This class is responsible for locating,
 * monitoring, and reloading assets based on file system changes.
 */
@Slf4j
public class FileSystemLocator extends AssetLocator<FileSystemLocator.FileSystemOptions> {
	private final WatchService watchService;
	private final Set<Path> watchedPaths = new HashSet<>();
	private final Map<String, AssetKey<?, ?>> loadedAssets = new HashMap<>();

	/**
	 * Constructs a FileSystemLocator with the specified asset system, options, and path.
	 *
	 * @param assetSystem The asset system to use for managing assets.
	 * @param options     The options to configure the locator, such as enabling auto-reloading.
	 * @param path        The base path in the file system to search for assets.
	 * @throws AssetException If the WatchService cannot be created when auto-reloading is enabled.
	 */
	@SuppressWarnings("unused")
	public FileSystemLocator(AssetSystem assetSystem, FileSystemOptions options, String path) {
		super(assetSystem, options, path);
		if (!Paths.get(path()).toFile().exists()) {
			log.error("Path does not exist: {}", path());
		}
		if (options != null && options.allowReload()) {
			FileSystem fileSystem = FileSystems.getDefault();
			try {
				watchService = fileSystem.newWatchService();
			} catch (IOException e) {
				log.error("Failed to create watch service");
				throw new AssetException(e);
			}
		} else {
			watchService = null;
		}
	}

	/**
	 * Polls the WatchService for file system changes and notifies the asset system
	 * of any modifications to loaded assets.
	 */
	@Override
	protected void onPollForChanges() {
		WatchKey watchKey;
		while ((watchKey = watchService.poll()) != null) {
			for (WatchEvent<?> pollEvent : watchKey.pollEvents()) {
				if (pollEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY
						&& pollEvent.context() instanceof Path path) {
					assetSystem().onAssetChanged(loadedAssets.get(path.toString()));
				}

			}
			watchKey.reset();
		}
	}

	/**
	 * Locates an asset in the file system based on the provided asset key.
	 *
	 * @param assetKey The key of the asset to locate.
	 * @param <A>      The type of the asset.
	 * @param <K>      The type of the asset key.
	 * @return An {@link AssetLocation} object pointing to the asset, or {@code null}
	 * if the asset could not be found.
	 * @throws AssetException If the asset cannot be registered with the WatchService
	 *                        for monitoring changes.
	 */
	@Override
	protected <A extends Asset<A, K>, K extends AssetKey<A, K>> AssetLocation<A, K> locateAsset(K assetKey) {
		String filePath = path() + assetKey.path();
		File file = new File(filePath);
		log.trace("Path: {}", filePath);
		if (file.exists()) {
			log.trace("Asset found: {}", assetKey.path());
			if (watchService != null) {
				try {
					Path path = new File(file.getParent()).toPath();
					if (!watchedPaths.contains(path)) {
						path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
						watchedPaths.add(path);
					}
					loadedAssets.put(assetKey.path(), assetKey);
				} catch (IOException e) {
					log.error("Failed to register file {} to watch service", filePath);
					throw new AssetException(e);
				}
			}
			return new FileAssetLocation<>(assetKey, this, path());
		}
		return null;
	}

	/**
	 * Options for configuring the {@link FileSystemLocator}.
	 * <p>
	 * This class holds parameters such as whether auto-reloading of assets is enabled.
	 */
	@Getter
	public static class FileSystemOptions implements AssetLocator.Options {
		/**
		 * Indicates whether assets should be automatically reloaded
		 * when changes are detected in the file system.
		 */
		final boolean allowReload;

		/**
		 * Constructs a new instance of {@code FileSystemOptions} with the given configuration.
		 *
		 * @param allowReload A boolean value indicating whether assets should be automatically
		 *                    reloaded when changes are detected in the file system.
		 */
		public FileSystemOptions(boolean allowReload) {
			this.allowReload = allowReload;
		}
	}

	/**
	 * Represents a location for an asset in the file system. This class provides
	 * methods to open and read the asset from the disk.
	 *
	 * @param <A> The type of the asset.
	 * @param <K> The type of the asset key.
	 */
	private static class FileAssetLocation<A extends Asset<A, K>, K extends AssetKey<A, K>>
			extends
				AssetLocation<A, K> {
		/**
		 * The base path where the assets are located in the file system.
		 */
		final String basePath;

		public FileAssetLocation(K assetKey, AssetLocator<?> assetLocator, String basePath) {
			super(assetKey, assetLocator);
			this.basePath = basePath;
		}

		/**
		 * Opens an input stream to the asset located at the path specified by the key.
		 *
		 * @return An {@link InputStream} to read the asset data.
		 * @throws AssetException        If the file cannot be found or read.
		 * @throws IllegalStateException If the file does not exist (unexpected situation).
		 */
		@Override
		public InputStream open() {
			File file = new File(basePath + assetKey().path());
			if (file.exists()) {
				try {
					return new FileInputStream(file);
				} catch (FileNotFoundException e) {
					throw new AssetException(e);
				}
			}
			throw new IllegalStateException("This should not be possible");
		}
	}
}

package mylie.core.assets;

import java.io.*;
import java.net.URL;
import java.util.EnumSet;
import lombok.AllArgsConstructor;

public abstract class AssetLocation {
	public enum Operation {
		READ, WRITE
	}
	abstract EnumSet<Operation> operations();

	abstract <K extends AssetId<A>, A> AssetInfo<K, A> locate(K assetId, Operation operation);

	public abstract <K extends AssetId<A>, A> InputStream read(AssetInfo<K, A> assetInfo);

	public abstract <K extends AssetId<A>, A> OutputStream write(AssetInfo<K, A> assetInfo);

	public static AssetLocation classpath(String basePath) {
		return new ClassPathAssetLocation(basePath);
	}

	public static AssetLocation file(String basePath) {
		return new FileAssetLocation(basePath);
	}

	public static AssetLocation fileProjectResources() {
		return new FileAssetLocation("src/main/resources/");
	}

	@AllArgsConstructor
	private static class ClassPathAssetLocation extends AssetLocation {
		private final String basePath;

		@Override
		EnumSet<Operation> operations() {
			return EnumSet.of(Operation.READ);
		}

		@Override
		<K extends AssetId<A>, A> AssetInfo<K, A> locate(K assetId, Operation operation) {
			assert operation == Operation.READ;
			URL resource = ClassPathAssetLocation.class.getClassLoader().getResource(path(assetId));
			if (resource != null) {
				return new AssetInfo<>(this, assetId);
			}
			return null;
		}

		@Override
		public <K extends AssetId<A>, A> OutputStream write(AssetInfo<K, A> assetInfo) {
			throw new UnsupportedOperationException("Cannot write to classpath assets");
		}

		@Override
		public <K extends AssetId<A>, A> InputStream read(AssetInfo<K, A> assetInfo) {
			return ClassPathAssetLocation.class.getClassLoader().getResourceAsStream(path(assetInfo.assetId()));
		}

		private String path(AssetId<?> assetId) {
			return basePath + assetId.id;
		}
	}

	@AllArgsConstructor
	private static class FileAssetLocation extends AssetLocation {
		final String basePath;
		@Override
		EnumSet<Operation> operations() {
			return EnumSet.of(Operation.READ, Operation.WRITE);
		}

		@Override
		<K extends AssetId<A>, A> AssetInfo<K, A> locate(K assetId, Operation operation) {
			File file = new File(path(assetId));
			if (operation == Operation.READ) {
				if (file.exists() && file.canRead()) {
					return new AssetInfo<>(this, assetId);
				}
			} else if (operation == Operation.WRITE) {
				if (file.exists() && file.canWrite()) {
					return new AssetInfo<>(this, assetId);
				}
				if (file.getParentFile().exists() && file.getParentFile().canWrite()) {
					return new AssetInfo<>(this, assetId);
				}
			}
			return null;
		}

		@Override
		public <K extends AssetId<A>, A> InputStream read(AssetInfo<K, A> assetInfo) {
			File file = new File(path(assetInfo.assetId()));
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public <K extends AssetId<A>, A> OutputStream write(AssetInfo<K, A> assetInfo) {
			File file = new File(path(assetInfo.assetId()));
			try {
				return new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		private String path(AssetId<?> assetId) {
			return basePath + assetId.id;
		}
	}
}

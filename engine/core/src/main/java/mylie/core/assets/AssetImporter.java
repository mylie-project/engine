package mylie.core.assets;

import java.io.IOException;

public abstract class AssetImporter<K extends AssetId<A>, A> {
	protected abstract boolean canRead(AssetId<?> assetInfo);
	protected abstract A read(AssetInfo<K, A> assetInfo) throws IOException;
}

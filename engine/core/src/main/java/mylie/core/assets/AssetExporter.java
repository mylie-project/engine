package mylie.core.assets;

public abstract class AssetExporter<K extends AssetId<A>, A> {
	protected abstract boolean canWrite(K assetInfo);
	protected abstract A write(AssetInfo<K, A> assetInfo);
}

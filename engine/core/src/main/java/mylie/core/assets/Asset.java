package mylie.core.assets;

public interface Asset<K extends AssetId<A>, A> {
	K assetId();
	void assetId(K assetId);
}

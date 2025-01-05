package mylie.core.assets;

public interface Asset<K extends AssetId<A>, A> {
	K assetId();
	A assetId(K assetId);
}

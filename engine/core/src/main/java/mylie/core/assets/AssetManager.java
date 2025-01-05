package mylie.core.assets;

import mylie.core.async.Result;

public interface AssetManager {
	<K extends AssetId<A>, A> A load(K assetId);

	<K extends AssetId<A>, A> Result<A> loadAsync(K assetId);
}

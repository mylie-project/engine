package mylie.core.assets;

import mylie.core.async.Result;
import mylie.core.component.Components;

public interface AssetManager extends Components.AppComponent {
	@SuppressWarnings("unchecked")
	<K extends AssetId<A>, A> Result<A> loadAssetAsync(K assetId);

	<K extends AssetId<A>, A> A loadAsset(K assetId);

	void addAssetLocation(AssetLocation assetLocation);

	void removeAssetLocation(AssetLocation assetLocation);

	void addAssetImporter(AssetImporter<?, ?> assetImporter);

	void removeAssetImporter(AssetImporter<?, ?> assetImporter);
}

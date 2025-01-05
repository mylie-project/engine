package mylie.core.assets;

import mylie.core.async.Result;

public class AssetId<T> {
	public final String id;

	public AssetId(String id) {
		this.id = id;
	}

	public String filetype() {
		return id.substring(id.lastIndexOf('.') + 1);
	}

	public T load(AssetManager assetManager) {
		return assetManager.load(this);
	}

	public Result<T> loadAsync(AssetManager assetManager) {
		return assetManager.loadAsync(this);
	}

}

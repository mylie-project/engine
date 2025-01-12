package mylie.core.assets;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mylie.core.async.Result;

@ToString
public class AssetId<T> {
	public final String id;
	@Getter
	@Setter
	boolean loaded = false;
	public AssetId(String id) {
		this.id = id;
	}

	public String filetype() {
		return id.substring(id.lastIndexOf('.') + 1);
	}

	public T load(AssetManager assetManager) {
		return assetManager.loadAsset(this);
	}

	public Result<T> loadAsync(AssetManager assetManager) {
		return assetManager.loadAssetAsync(this);
	}

}

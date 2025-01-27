package mylie.engine.assets;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public abstract class AssetLocator<T extends AssetLocator.Options> {
	final AssetSystem assetSystem;
	final T options;
	final String path;
	public AssetLocator(AssetSystem assetSystem, T options, String path) {
		this.assetSystem = assetSystem;
		this.options = options;
		this.path = path;
	}

	protected abstract void onPollForChanges();

	public abstract <A extends Asset<A, K>, K extends AssetKey<A, K>> AssetLocation<A, K> locateAsset(K assetKey);

	public static abstract class Options {

	}
}

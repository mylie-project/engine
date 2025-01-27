package mylie.engine.assets;

import java.io.InputStream;
import lombok.Getter;

@Getter
public abstract class AssetLocation<A extends Asset<A, K>, K extends AssetKey<A, K>> {
	final K assetKey;
	final AssetLocator<?> assetLocator;

	protected AssetLocation(K assetKey, AssetLocator<?> assetLocator) {
		this.assetKey = assetKey;
		this.assetLocator = assetLocator;
	}

	public abstract InputStream open();
}

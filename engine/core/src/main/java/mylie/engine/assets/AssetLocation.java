package mylie.engine.assets;

import lombok.Getter;

import java.io.InputStream;

@Getter
public abstract class AssetLocation<A extends Asset<A,K>,K extends AssetKey<A,K>> {
    final K assetKey;
    final AssetLocator<?> assetLocator;

    public AssetLocation(K assetKey, AssetLocator<?> assetLocator) {
        this.assetKey = assetKey;
        this.assetLocator = assetLocator;
    }

    public abstract InputStream open();
}

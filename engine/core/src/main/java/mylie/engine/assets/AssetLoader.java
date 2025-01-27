package mylie.engine.assets;

import mylie.engine.assets.assets.TextFile;

import java.util.HashSet;
import java.util.Set;

public abstract class AssetLoader<A extends Asset<A,K>,K extends AssetKey<A,K>> {
    final Set<String> fileExtensions=new HashSet<>();

    public AssetLoader(String... fileExtensions) {
        this.fileExtensions.addAll(Set.of(fileExtensions));
    }

    protected boolean isSupported(AssetKey<?,?> key) {
        String path = key.path();
        for (String fileExtension : fileExtensions) {
            if(key.path().endsWith(fileExtension)){
                return true;
            }
        }
        return false;
    }
    protected abstract A loadAsset(AssetLocation<A,K> location);
}

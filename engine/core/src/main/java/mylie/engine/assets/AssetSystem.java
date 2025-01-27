package mylie.engine.assets;

import lombok.extern.slf4j.Slf4j;
import mylie.time.Time;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class AssetSystem {
    private List<AssetLocator<?>> assetLocators=new ArrayList<>();
    private List<AssetLoader<?,?>> assetLoaders=new ArrayList<>();
    private Map<AssetKey<?,?>,Object> assetCache=new WeakHashMap<>();
    public AssetSystem() {

    }

    @SuppressWarnings("unchecked")
    public <A extends Asset<A,K>,K extends AssetKey<A,K>> A loadAsset(AssetKey<A,K> assetKey){
        K key= (K) assetKey;
        AssetLocation<A, K> assetLocation = locateAsset(key);
        if(assetLocation!=null){
            A asset = importAsset(assetLocation);
            if(asset!=null){
                onAssetLoaded(asset,key);
            }
            log.trace("Asset {} loaded",asset);
            return asset;
        }
        return null;
    }


    public <A extends Asset<A,K>,K extends AssetKey<A,K>> AssetLocation<A,K> locateAsset(K assetKey){
        AssetLocation<A,K> assetLocation=null;
        for (AssetLocator<?> assetLocator : assetLocators) {
            assetLocation=assetLocator.locateAsset(assetKey);
            if(assetLocation!=null){
                break;
            }
        }
        if(assetLocation==null){
            log.error("Asset {} not found",assetKey);
        }
        return assetLocation;
    }

    private <A extends Asset<A,K>,K extends AssetKey<A,K>> void onAssetLoaded(A asset,K assetKey){
        asset.key(assetKey);
        assetCache.put(assetKey,asset);
        asset.version(Time.frameId());
    }

    @SuppressWarnings("unchecked")
    public final <A extends Asset<A,K>,K extends AssetKey<A,K>> A importAsset(AssetLocation<A,K> assetLocation){
        K assetKey = assetLocation.assetKey();
        for (AssetLoader<?, ?> assetLoader : assetLoaders) {
            if (assetLoader.isSupported(assetKey)) {
                return ((AssetLoader<A, K>) assetLoader).loadAsset(assetLocation);
            }
        }
        log.error("No loader found for asset {}",assetKey);
        return null;
    }

    public void onUpdate() {
        for (AssetLocator<?> assetLocator : assetLocators) {
            assetLocator.onPollForChanges();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void onAssetChanged(AssetKey<?,?> assetKey) {
        log.trace("Asset {} changed",assetKey);
        if(assetCache.containsKey(assetKey)){
            loadAsset((AssetKey) assetKey);
            assetKey.dependingAssets().forEach(this::onAssetChanged);
        }
    }

    public <L extends AssetLocator<O>,O extends AssetLocator.Options> void addAssetLocator(Class<L> locator,O options,String path) {
        try {
            assetLocators.add(locator.getConstructor(AssetSystem.class,options.getClass(),String.class).newInstance(this,options,path));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Failed to initialized the locator: {}",options.getClass(),e);
            throw new RuntimeException(e);
        }
    }

    public <L extends AssetLoader<A,K>,A extends Asset<A,K>,K extends AssetKey<A,K>> void addAssetLoader(Class<L> loader,String... fileExtensions){
        try {
            assetLoaders.add(loader.getConstructor(String[].class).newInstance((Object) fileExtensions));
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Failed to initialized the loader: {}",loader,e);
        }
    }

    public void addAssetDependency(AssetKey<?,?> key,AssetKey<?,?> dependency){
        key.dependingAssets().add(dependency);
    }
}

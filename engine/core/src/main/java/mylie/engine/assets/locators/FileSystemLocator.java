package mylie.engine.assets.locators;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.engine.assets.*;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class FileSystemLocator extends AssetLocator<FileSystemLocator.FileSystemOptions> {
    private final WatchService watchService;
    private final Set<Path> watchedPaths=new HashSet<>();
    private final Map<String,AssetKey<?,?>> loadedAssets=new HashMap<>();

    public FileSystemLocator(AssetSystem assetSystem, FileSystemOptions options,String path) {
        super(assetSystem, options, path);
        if(!Paths.get(path()).toFile().exists()){
            log.error("Path does not exist: {}",path());
        }
        if(options!=null && options.allowReload()){
            FileSystem fileSystem= FileSystems.getDefault();
            try {
                watchService = fileSystem.newWatchService();
            } catch (IOException e) {
                log.error("Failed to create watch service");
                throw new RuntimeException(e);
            }
        }else{
            watchService=null;
        }
    }

    @Override
    protected void onPollForChanges() {
        WatchKey watchKey;
        while ((watchKey = watchService.poll()) != null){
            for (WatchEvent<?> pollEvent : watchKey.pollEvents()) {
                if(pollEvent.kind()==StandardWatchEventKinds.ENTRY_MODIFY) {
                    if(pollEvent.context() instanceof Path path) {
                        assetSystem().onAssetChanged(loadedAssets.get(path.toString()));
                    }
                }

            }
            watchKey.reset();
        }
    }

    @Override
    public <A extends Asset<A,K>,K extends AssetKey<A,K>> AssetLocation<A,K> locateAsset(K assetKey) {
        String filePath=path()+assetKey.path();
        File file=new File(filePath);
        log.trace("Path: {}", filePath);
        if(file.exists()){
            log.trace("Asset found: {}", assetKey.path());
            if(watchService!=null) {
                try {
                    Path path = new File(file.getParent()).toPath();
                    if(!watchedPaths.contains(path)) {
                        path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                        watchedPaths.add(path);
                    }
                    loadedAssets.put(assetKey.path(), assetKey);
                } catch (IOException e) {
                    log.error("Failed to register file {} to watch service",filePath);
                    throw new RuntimeException(e);
                }
            }
            return new FileAssetLocation<>(assetKey,this,path());
        }
        return null;
    }

    @Getter
    public static class FileSystemOptions extends AssetLocator.Options{
        boolean allowReload=false;

        public FileSystemOptions(boolean allowReload) {
            this.allowReload = allowReload;
        }
    }


    private static class FileAssetLocation<A extends Asset<A,K>,K extends AssetKey<A,K> >extends AssetLocation<A,K>{
        final String basePath;
        public FileAssetLocation(K assetKey, AssetLocator<?> assetLocator, String basePath) {
            super(assetKey, assetLocator);
            this.basePath = basePath;
        }

        @Override
        public InputStream open() {
            File file = new File(basePath + assetKey().path());
            if(file.exists()){
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            throw new IllegalStateException("This should not be possible");
        }
    }
}
package mylie.engine.assets.assets;

import mylie.engine.assets.Asset;
import mylie.engine.assets.AssetKey;

public class TextFile extends Asset<TextFile, TextFile.Key> {
    final String[] content;

    public TextFile(String[] content) {
        this.content = content;
    }

    public static class Key extends AssetKey<TextFile,Key>{
        public Key(String path) {
            super(path);
        }
    }
}

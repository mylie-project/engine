package mylie.engine.assets.loaders;

import lombok.extern.slf4j.Slf4j;
import mylie.engine.assets.AssetLoader;
import mylie.engine.assets.AssetLocation;
import mylie.engine.assets.assets.TextFile;

import java.io.InputStream;
import java.util.*;

@Slf4j
public class TextFileLoader extends AssetLoader<TextFile,TextFile.Key> {
    public TextFileLoader(String... fileExtensions) {
        super(fileExtensions);
    }

    @Override
    protected TextFile loadAsset(AssetLocation<TextFile, TextFile.Key> location) {
        TextFile.Key key = location.assetKey();
        try(InputStream inputStream=location.open()){
            List<String> lines=new ArrayList<>();
            Scanner scanner=new Scanner(inputStream);
            while (scanner.hasNextLine()){
                lines.add(scanner.nextLine());
            }
            return new TextFile(lines.toArray(new String[0]));
        }catch (Exception e){
            log.error("Error while reading from input stream");
        }
        return null;
    }
}

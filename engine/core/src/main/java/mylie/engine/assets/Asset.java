package mylie.engine.assets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Asset<A extends Asset<A,K>,K extends AssetKey<A,K>> {
    @Setter(AccessLevel.PACKAGE)
    private K key;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private long version;
}

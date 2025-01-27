package mylie.engine.assets;

import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

@ToString
public class AssetKey<A extends Asset<A, K>, K extends AssetKey<A, K>> {
	@Getter(AccessLevel.PACKAGE)
	private final Set<AssetKey<?, ?>> dependingAssets = new HashSet<>();
	@Getter
	private final String path;

	public AssetKey(String path) {
		this.path = path;
	}

}

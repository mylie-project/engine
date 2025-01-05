package mylie.core.assets;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
@Getter
public class AssetInfo<K extends AssetId<A>, A> {
	@Setter(AccessLevel.PACKAGE)
	private AssetSystem assetSystem;
	private final AssetLocation assetLocation;
	private final K assetId;

	public AssetInfo(AssetLocation assetLocation, K assetId) {
		this.assetLocation = assetLocation;
		this.assetId = assetId;
	}
}

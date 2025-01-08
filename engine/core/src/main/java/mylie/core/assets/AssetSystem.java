package mylie.core.assets;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.*;

@Slf4j
public class AssetSystem {
	private final List<AssetLocation> assetLocations = new ArrayList<>();
	private final List<AssetImporter<?, ?>> assetImporters = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public <K extends AssetId<A>, A> Result<A> loadAssetAsync(K assetId) {
		return (Result<A>) Async.async(Async.ExecutionMode.ASYNC, Async.Target.Any, Caches.No, -1, LoadAssetAsync,
				assetId, this);
	}

	public <K extends AssetId<A>, A> A loadAsset(K assetId) {
		AssetInfo<K, A> assetInfo = locateAsset(assetId, AssetLocation.Operation.READ);
		if (assetInfo != null) {
			AssetImporter<K, A> importer = locateAssetImporter(assetId);
			if (importer != null) {
				return importer.read(assetInfo);
			} else {
				log.warn("No importer found for asset {}", assetId);
			}
		} else {
			log.warn("Asset {} not found", assetId.id);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <K extends AssetId<A>, A> AssetImporter<K, A> locateAssetImporter(K assetId) {
		for (AssetImporter<?, ?> assetImporter : assetImporters) {
			if (assetImporter.canRead(assetId)) {
				return (AssetImporter<K, A>) assetImporter;
			}
		}
		return null;
	}

	public <K extends AssetId<A>, A> AssetInfo<K, A> locateAsset(K assetId, AssetLocation.Operation operation) {
		AssetInfo<K, A> assetInfo;
		for (AssetLocation assetLocation : assetLocations) {
			if (assetLocation.operations().contains(operation)) {
				assetInfo = assetLocation.locate(assetId, operation);
				if (assetInfo != null) {
					assetInfo.assetSystem(this);
					return assetInfo;
				}
			}
		}
		log.warn("Asset {} for {} operation not found", assetId, operation.name());
		return null;
	}

	public void addAssetLocation(AssetLocation assetLocation) {
		assetLocations.add(assetLocation);
	}

	public void removeAssetLocation(AssetLocation assetLocation) {
		assetLocations.remove(assetLocation);
	}

	public void addAssetImporter(AssetImporter<?, ?> assetImporter) {
		assetImporters.add(assetImporter);
	}

	public void removeAssetImporter(AssetImporter<?, ?> assetImporter) {
		assetImporters.remove(assetImporter);
	}

	private static final Function.F2<AssetId<?>, AssetSystem, ?> LoadAssetAsync = new Function.F2<>("LoadAssetAsync") {
		@Override
		protected Object apply(AssetId<?> assetId, AssetSystem assetSystem) {
			return assetSystem.loadAsset(assetId);
		}
	};
}

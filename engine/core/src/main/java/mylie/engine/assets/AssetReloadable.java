package mylie.engine.assets;

/**
 * Represents an abstract asset that can be reloaded to reflect changes in its source data.
 *
 * @param <A> the specific type of the asset extending this class.
 * @param <K> the type of the key that uniquely identifies the asset.
 *
 * This class extends the functionality of {@link Asset} to include the ability to be updated
 * or reloaded with a new version of the asset. When the new version of the asset is loaded,
 * the instance implementing this class is expected to update its internal state accordingly.
 *
 * Subclasses must implement the abstract {@code onReload} method to define the logic needed
 * to apply changes from the newly loaded asset to the current instance. This mechanism is
 * particularly useful for scenarios where assets are expected to evolve over time, such as
 * reloading from modified files or updated resources.
 */
public abstract class AssetReloadable<A extends Asset<A, K>, K extends AssetKey<A, K>> extends Asset<A, K> {

	/**
	 * Default constructor for the {@code AssetReloadable} class.
	 *
	 * This constructor initializes an instance of the {@code AssetReloadable} class,
	 * which represents an abstract asset capable of being reloaded with a new version
	 * to reflect changes in its source data. Subclasses are expected to implement the
	 * necessary logic for reloading through the abstract {@code onReload} method.
	 */
	protected AssetReloadable() {
	}

	/**
	 * Handles the reloading of the current asset instance with a newly loaded version.
	 * This method is expected to update the state of the current instance based on
	 * the provided new asset.
	 *
	 * @param newAsset the new version of the asset to reload, containing updated state
	 */
	protected abstract void onReload(A newAsset);
}

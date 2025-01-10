package mylie.core.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.core.assets.Asset;
import mylie.math.Quaternionf;
import mylie.math.Vector3f;
import mylie.util.Flags;

@Getter(AccessLevel.PACKAGE)
public class Spatial implements Asset<SpatialId, Spatial> {
	private static final int WORLD_TRANSFORM_CHANGED = 1;
	private static final int WORLD_BOUNDS_CHANGED = 1 << 1;
	private final Transform localTransform = new Transform();
	private final Transform worldTransform = new Transform();
	private final Flags flags = new Flags();
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PACKAGE)
	private Spatial parent;
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private SpatialId assetId;

	protected void onLocalTransformChanged() {
		flags.set(WORLD_TRANSFORM_CHANGED);
		Traverser.traverse(Traverser.ToLeaf, this, spatial -> {
			spatial.flags().set(WORLD_BOUNDS_CHANGED | WORLD_TRANSFORM_CHANGED);
			return true;
		});
		onWorldBoundsChanged();
	}

	protected void onWorldBoundsChanged() {
		Traverser.traverse(Traverser.ToRoot, this, spatial -> {
			spatial.flags().set(WORLD_BOUNDS_CHANGED);
			return true;
		});
	}

	protected Spatial getRoot() {
		if (parent == null) {
			return this;
		} else {
			return parent.getRoot();
		}
	}

	public Transform worldTransform() {
		if (flags().isSet(WORLD_TRANSFORM_CHANGED)) {
			if (parent != null) {
				this.worldTransform.combine(localTransform, parent().worldTransform());
			} else {
				worldTransform.set(localTransform);
			}
		}
		return worldTransform;
	}

	public interface Translatable {
		default void translation(Vector3f newPosition) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().position(newPosition);
				spatial.onLocalTransformChanged();
			}
		}

		default void translate(Vector3f offset) {
			translate(offset.x(), offset.y(), offset.z());
		}

		default void translate(float x, float y, float z) {
			if (this instanceof Spatial spatial) {
				translation(spatial.localTransform().position().add(x, y, z));
			}
		}
	}

	public interface Rotatable {
		default void rotation(Quaternionf newRotation) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().rotation(newRotation);
				spatial.onLocalTransformChanged();
			}
		}

		default void rotate(Quaternionf rotation) {
			if (this instanceof Spatial spatial) {
				rotation(spatial.localTransform().rotation().mul(rotation));
			}
		}

		default void rotateAxis(float angle, Vector3f axis) {
			if (this instanceof Spatial spatial) {
				rotation(spatial.localTransform().rotation().rotateAxis(angle, axis));
			}
		}
	}

	public interface ScalableUniform {
		default void scaling(float scaling) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().scale(new Vector3f(scaling));
				spatial.onLocalTransformChanged();
			}
		}

		default void scale(float scale) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().scale(spatial.localTransform().scale().mul(scale));
				spatial.onLocalTransformChanged();
			}
		}
	}

	public interface Scalable extends ScalableUniform {
		default void scaling(Vector3f scaling) {
			if (this instanceof Spatial spatial) {
				spatial.localTransform().scale(scaling);
				spatial.onLocalTransformChanged();
			}
		}

		default void scale(Vector3f scale) {
			if (this instanceof Spatial spatial) {
				scaling(spatial.localTransform().scale().mul(scale));
			}
		}
	}
}

package mylie.engine.scene;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.engine.assets.Asset;
import mylie.engine.assets.AssetKey;
import mylie.math.*;

/**
 * Represents a spatial entity in a scene graph with hierarchical relationships,
 * transformation, and bounds. It defines local and world transformations and
 * manages changes in position, rotation, and scale for derived classes.
 *
 * @param <S> the specific type of the spatial entity inheriting from this class
 * @param <K> the type of the key uniquely identifying the spatial entity
 */
@Getter(AccessLevel.PROTECTED)
public abstract class Spatial<S extends Spatial<S, K>, K extends AssetKey<S, K>> extends Asset<S, K> {
	private static final int WORLD_TRANSFORM_CHANGED = 1;
	private static final int WORLD_BOUNDS_CHANGED = 1 << 1;

	/**
	 * Default constructor for the Spatial class.
	 *
	 * Initializes the Spatial instance with default values. Specifically, it sets
	 * an internal flags field to 0. This constructor is protected, making it accessible
	 * only to the Spatial class, its subclasses, and other classes within the same package,
	 * ensuring controlled instantiation.
	 */
	protected Spatial() {
		flags = 0;
	}

	/**
	 * The transformation applied locally to this spatial entity.
	 * Defines position, rotation, and scale relative to the parent or origin.
	 */
	private final Transform localTransform = new Transform();
	/**
	 * The transformation of this spatial entity in world space.
	 * Combines the local transform with that of all ancestor nodes.
	 */
	private final Transform worldTransform = new Transform();
	/**
	 * The parent node of this spatial entity.
	 * Determines the hierarchical relationships and transformations in the scene graph.
	 */
	@Setter(AccessLevel.PROTECTED)
	private Node parent;
	private int flags;

	/**
	 * Called when the local transformation is modified.
	 * Marks this spatial and its descendants as requiring world transform
	 * and bounds updates.
	 */
	protected void onLocalTransformChanged() {
		flags |= WORLD_TRANSFORM_CHANGED;
		Traverser.traverse(Traverser.ToLeaf, this, spatial -> {
			spatial.flags |= (WORLD_BOUNDS_CHANGED | WORLD_TRANSFORM_CHANGED);
			return true;
		});
		onWorldBoundsChanged();
	}

	/**
	 * Called when the world bounds of this spatial entity change.
	 * Propagates the updated world bounds status to ancestors.
	 */
	protected void onWorldBoundsChanged() {
		Traverser.traverse(Traverser.ToRoot, this, spatial -> {
			spatial.flags |= WORLD_BOUNDS_CHANGED;
			return true;
		});
	}

	/**
	 * Computes and retrieves the world transformation for this spatial entity.
	 * Applies the parent transformation to the local transform, if a parent exists.
	 *
	 * @return The cached or recalculated world transform for this spatial entity.
	 */
	public Transform worldTransform() {
		if ((flags & WORLD_TRANSFORM_CHANGED) != 0) {
			if (parent != null) {
				this.worldTransform.combine(localTransform, parent().worldTransform());
			} else {
				worldTransform.set(localTransform);
			}
		}
		return worldTransform;
	}

	/**
	 * Interface for spatial entities that support translation operations.
	 * Allows modifying or offsetting the position of the spatial.
	 */
	public interface Translatable {
		/**
		 * Updates the position of the spatial entity to the specified new position.
		 * If the object implementing this method is a {@code Spatial}, it modifies
		 * the local transform's position and triggers the local transform changed event.
		 *
		 * @param newPosition the new position to set for the spatial entity
		 */
		default void translation(Vec3<Float> newPosition) {
			if (this instanceof Spatial<?, ?> spatial) {
				spatial.localTransform().position(newPosition);
				spatial.onLocalTransformChanged();
			}
		}

		/**
		 * Translates the position of the spatial entity by the specified offset.
		 * If the object implementing this method is a {@code Spatial}, it updates
		 * the local transform's position by adding the offset and triggers the
		 * local transform changed event.
		 *
		 * @param offset the vector representing the offset to apply to the position
		 */
		default void translate(Vec3<Float> offset) {
			if (this instanceof Spatial<?, ?> spatial) {
				spatial.localTransform().position(spatial.localTransform().position().add(offset));
				spatial.onLocalTransformChanged();
			}
		}
	}

	/**
	 * Interface for spatial entities that support rotation operations.
	 * Encapsulates functions for modifying the orientation of the spatial.
	 */
	public interface Rotatable {
		/**
		 * Updates the rotation of the spatial entity to the specified new rotation.
		 * If the object implementing this method is a {@code Spatial}, it modifies
		 * the local transform's rotation and triggers the local transform changed event.
		 *
		 * @param newRotation the new rotation to set for the spatial entity
		 */
		default void rotation(Quaternion<Float> newRotation) {
			if (this instanceof Spatial<?, ?> spatial) {
				spatial.localTransform().rotation(newRotation);
				spatial.onLocalTransformChanged();
			}
		}

		/**
		 * Rotates the spatial entity by applying the specified rotation.
		 * Combines the current rotation with the given rotation to compute the new orientation.
		 *
		 * @param rotation the rotation to be applied to the spatial entity
		 */
		default void rotate(Quaternion<Float> rotation) {
			if (this instanceof Spatial<?, ?> spatial) {
				rotation(spatial.localTransform().rotation().mul(rotation));
			}
		}

		/**
		 * Rotates the spatial entity around a given axis by a specified angle.
		 *
		 * @param angle the rotation angle in radians
		 * @param axis the vector representing the axis of rotation
		 */
		default void rotateAxis(float angle, Vec3<Float> axis) {
			if (this instanceof Spatial<?, ?> spatial) {
				rotation(spatial.localTransform().rotation().rotateAxis(angle, axis));
			}
		}
	}

	/**
	 * Interface for spatial entities that support uniform scaling operations.
	 * Encapsulates functions for adjusting size proportionally along all axes.
	 */
	public interface ScalableUniform {
		/**
		 * Applies a uniform scaling transformation to the spatial entity.
		 * Scales the entity proportionally along all axes by the specified scaling factor.
		 * If the current object is an instance of {@code Spatial}, it updates the local transform's
		 * scale and triggers the local transform changed event.
		 *
		 * @param scaling the uniform scaling factor to apply to the spatial entity
		 */
		default void scaling(float scaling) {
			if (this instanceof Spatial<?, ?> spatial) {
				spatial.localTransform().scale(Vec3.of(scaling, scaling, scaling));
				spatial.onLocalTransformChanged();
			}
		}

		/**
		 * Applies a scaling transformation by multiplying the current scale of the spatial entity
		 * with the specified uniform scaling factor. The scaling is applied proportionally along
		 * all axes. If the object invoking this method is an instance of {@code Spatial}, the
		 * local transform's scale is updated, and the local transform changed event is triggered.
		 *
		 * @param scale the uniform scaling factor to multiply with the current scale
		 */
		default void scale(float scale) {
			if (this instanceof Spatial<?, ?> spatial) {
				spatial.localTransform().scale(spatial.localTransform().scale().mul(Vec3.of(scale, scale, scale)));
				spatial.onLocalTransformChanged();
			}
		}
	}

	/**
	 * Interface for spatial entities that support non-uniform scaling operations.
	 * Extends {@link ScalableUniform} to allow axis-specific scaling adjustments.
	 */
	public interface Scalable extends ScalableUniform {

		/**
		 * Applies a scaling transform to the spatial entity using the specified scaling factors
		 * for each axis.
		 *
		 * @param scaling a 3D vector representing the scaling factors along the X, Y, and Z axes
		 */
		default void scaling(Vec3<Float> scaling) {
			if (this instanceof Spatial<?, ?> spatial) {
				spatial.localTransform().scale(scaling);
				spatial.onLocalTransformChanged();
			}
		}

		/**
		 * Applies a scaling transformation to the spatial entity by multiplying its current scaling factors
		 * with the given scaling vector along the X, Y, and Z axes.
		 *
		 * @param scale a 3D vector representing the scaling factors to be multiplied with the current scaling
		 */
		default void scale(Vec3<Float> scale) {
			if (this instanceof Spatial<?, ?> spatial) {
				scaling(spatial.localTransform().scale().mul(scale));
			}
		}
	}
}

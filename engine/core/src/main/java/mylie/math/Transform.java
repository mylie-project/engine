package mylie.math;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a transform with position, scale, and rotation components in 3D space.
 */
@Setter
@Getter
public class Transform {
	Vec3<Float> position = Vec3.of(0f, 0f, 0f);
	Vec3<Float> scale = Vec3.of(1f, 1f, 1f);
	Quaternion<Float> rotation = Quaternion.f(0f, 0f, 0f, 1f);

	/**
	 * Creates a new instance of the Transform class with default position, scale, and rotation values.
	 *
	 * The default values are:
	 * - Position: (0, 0, 0)
	 * - Scale: (1, 1, 1)
	 * - Rotation: Quaternion identity (0, 0, 0, 1)
	 *
	 * This constructor is typically used to initialize a transform with no transformations applied.
	 */
	public Transform() {
		// Empty constructor intentionally
	}

	/**
	 * Combines the local transform with the parent's world transform and updates the current transform.
	 * The resulting transform is computed by applying the parent's scale, rotation, and position
	 * to the local transform.
	 *
	 * @param local the local transform to be combined
	 * @param parentWorld the parent's world transform to combine with
	 */
	public void combine(Transform local, Transform parentWorld) {
		this.rotation = parentWorld.rotation.mul(local.rotation);
		this.scale = parentWorld.scale.mul(local.scale);
		this.position = parentWorld.scale.mul(local.position);
		this.position = parentWorld.rotation.transform(this.position);
		this.position = this.position.add(parentWorld.position);
	}

	/**
	 * Updates the current transform's components (position, rotation, scale) to match those of the specified transform.
	 *
	 * @param localTransform the transform whose components will be copied to the current transform
	 */
	public void set(Transform localTransform) {
		this.position = localTransform.position;
		this.rotation = localTransform.rotation;
		this.scale = localTransform.scale;
	}

}

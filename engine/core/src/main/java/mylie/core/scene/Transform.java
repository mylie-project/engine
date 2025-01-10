package mylie.core.scene;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.math.Matrix4f;
import mylie.math.Quaternionf;
import mylie.math.Vector3f;

@Getter(AccessLevel.PACKAGE)
public class Transform {
	Vector3f position = new Vector3f(0f);
	Quaternionf rotation = new Quaternionf();
	Vector3f scale = new Vector3f(1.0f);
	Matrix4f matrix = new Matrix4f();
	boolean dirty = true;
	void combine(Transform localTransform, Transform parentWorldTransform) {
		this.rotation = parentWorldTransform.rotation.mul(localTransform.rotation);
		this.scale = parentWorldTransform.scale.mul(localTransform.scale);
		this.position = parentWorldTransform.scale.mul(localTransform.position);
		position = parentWorldTransform.rotation.transform(this.position);
		position = position.add(parentWorldTransform.position);
		onTransformChange();
	}

	void set(Transform transform) {
		position = transform.position;
		rotation = transform.rotation;
		scale = transform.scale;
		onTransformChange();
	}

	private void onTransformChange() {
		dirty = true;
	}

	Matrix4f matrix() {
		if (dirty) {
			matrix = Matrix4f.IDENTITY;
			// matrix.identity().translate(position).rotate(rotation).scale(scale);
			dirty = false;
		}
		return matrix;
	}

	void position(Vector3f position) {
		if (position.equals(this.position))
			return;
		this.position = position;
		onTransformChange();
	}

	void rotation(Quaternionf rotation) {
		if (rotation.equals(this.rotation))
			return;
		this.rotation = rotation;
		onTransformChange();
	}

	void scale(Vector3f scale) {
		if (scale.equals(this.scale))
			return;
		this.scale = scale;
		onTransformChange();
	}
}

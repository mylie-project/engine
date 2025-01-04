package mylie.core.scene;

import lombok.AccessLevel;
import lombok.Getter;
import org.joml.*;

@Getter(AccessLevel.PACKAGE)
public class Transform {
	Vector3f position = new Vector3f(0f);
	Quaternionf rotation = new Quaternionf();
	Vector3f scale = new Vector3f(1.0f);
	Matrix4f matrix = new Matrix4f();
	boolean dirty = true;
	void combine(Transform localTransform, Transform parentWorldTransform) {
		parentWorldTransform.rotation.mul(localTransform.rotation, this.rotation);
		parentWorldTransform.scale.mul(localTransform.scale, this.scale);
		parentWorldTransform.scale.mul(localTransform.position, this.position);
		parentWorldTransform.rotation.transform(this.position, this.position);
		this.position.add(parentWorldTransform.position);
		onTransformChange();
	}

	Transform set(Transform transform) {
		position.set(transform.position);
		rotation.set(transform.rotation);
		scale.set(transform.scale);
		onTransformChange();
		return this;
	}

	private void onTransformChange() {
		dirty = true;
	}

	Matrix4f matrix() {
		if (dirty) {
			matrix.identity().translate(position).rotate(rotation).scale(scale);
		}
		return matrix;
	}

	Transform position(Vector3fc position) {
		if (position.equals(this.position))
			return this;
		this.position.set(position);
		onTransformChange();
		return this;
	}

	Transform rotation(Quaternionfc rotation) {
		if (rotation.equals(this.rotation))
			return this;
		this.rotation.set(rotation);
		onTransformChange();
		return this;
	}

	Transform scale(Vector3fc scale) {
		if (scale.equals(this.scale))
			return this;
		this.scale.set(scale);
		onTransformChange();
		return this;
	}
}

package mylie.core.scene;

import lombok.AccessLevel;
import lombok.Getter;
import org.joml.*;

@Getter(AccessLevel.PACKAGE)
public class Transform {
	final Vector3f position = new Vector3f(0f);
	final Quaternionf rotation = new Quaternionf();
	final Vector3f scale = new Vector3f(1.0f);
	final Matrix4f matrix = new Matrix4f();
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
			dirty = false;
		}
		return matrix;
	}

	void position(Vector3fc position) {
		if (position.equals(this.position))
			return;
		this.position.set(position);
		onTransformChange();
	}

	void rotation(Quaternionfc rotation) {
		if (rotation.equals(this.rotation))
			return;
		this.rotation.set(rotation);
		onTransformChange();
	}

	void scale(Vector3fc scale) {
		if (scale.equals(this.scale))
			return;
		this.scale.set(scale);
		onTransformChange();
	}
}

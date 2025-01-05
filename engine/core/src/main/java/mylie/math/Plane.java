package mylie.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector3f;
import org.joml.Vector4f;
@AllArgsConstructor
@Getter
public class Plane implements Shape {
	private Vector4f data; // Stores the plane as (normal.x, normal.y, normal.z, d)

	public float distanceToPoint(Vector3f point) {
		return data.x * point.x + data.y * point.y + data.z * point.z + data.w;
	}

	@Override
	public boolean intersects(Shape other) {
		return switch (other) {
			case AlignedBox alignedBox -> CollisionMath.collides(this, alignedBox);
			case Sphere sphere -> CollisionMath.collides(this, sphere);
			case OrientedBox orientedBox -> CollisionMath.collides(this, orientedBox);
			case Ray ray -> CollisionMath.collides(this, ray);
			case Frustum frustum -> CollisionMath.collides(this, frustum);
			case Plane plane -> CollisionMath.collides(this, plane);
			default -> throw new UnsupportedOperationException(
					this.getClass().getName() + " does not support " + other.getClass().getName());
		};
	}
}

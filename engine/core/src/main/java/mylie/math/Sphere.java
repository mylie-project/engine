package mylie.math;

import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class Sphere implements Shape {
	private Vector3f center;
	private float radius;
	private float radiusSquared;

	public Sphere(Vector3f center, float radius) {
		this.center = center;
		this.radius = radius;
		this.radiusSquared = radius * radius;
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

package mylie.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector3f;

@AllArgsConstructor
@Getter
public class Frustum implements Shape {
	private Plane nearPlane;
	private Plane farPlane;
	private Plane leftPlane;
	private Plane rightPlane;
	private Plane topPlane;
	private Plane bottomPlane;
	private Vector3f[] corners;

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

	public Plane[] planes() {
		return new Plane[]{nearPlane, farPlane, leftPlane, rightPlane, topPlane, bottomPlane};
	}
}

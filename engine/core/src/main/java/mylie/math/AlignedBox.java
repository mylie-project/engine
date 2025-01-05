package mylie.math;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector3f;
@AllArgsConstructor
@Getter
public class AlignedBox implements Shape {
	Vector3f min, max;

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

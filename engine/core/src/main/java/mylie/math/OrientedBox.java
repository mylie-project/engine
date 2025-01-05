package mylie.math;

import lombok.Getter;
import org.joml.Vector3f;
@Getter
public class OrientedBox implements Shape {
	private Vector3f center;
	private Vector3f halfSizes;
	private Vector3f[] axes = new Vector3f[3];
	private Vector3f[] corners = new Vector3f[8];

	public OrientedBox(Vector3f center, Vector3f halfSizes, Vector3f axisX, Vector3f axisY, Vector3f axisZ) {
		this.center = center;
		this.halfSizes = halfSizes;
		this.axes[0] = axisX;
		this.axes[1] = axisY;
		this.axes[2] = axisZ;
	}

	public Vector3f[] corners() {
		// Directions of the half-sizes along axes
		Vector3f xAxis = new Vector3f(axes[0]).mul(halfSizes.x);
		Vector3f yAxis = new Vector3f(axes[1]).mul(halfSizes.y);
		Vector3f zAxis = new Vector3f(axes[2]).mul(halfSizes.z);

		// Compute combinations of +- scaled axes to form corners
		corners[0] = new Vector3f(center).add(xAxis).add(yAxis).add(zAxis); // +x +y +z
		corners[1] = new Vector3f(center).add(xAxis).add(yAxis).sub(zAxis); // +x +y -z
		corners[2] = new Vector3f(center).add(xAxis).sub(yAxis).add(zAxis); // +x -y +z
		corners[3] = new Vector3f(center).add(xAxis).sub(yAxis).sub(zAxis); // +x -y -z
		corners[4] = new Vector3f(center).sub(xAxis).add(yAxis).add(zAxis); // -x +y +z
		corners[5] = new Vector3f(center).sub(xAxis).add(yAxis).sub(zAxis); // -x +y -z
		corners[6] = new Vector3f(center).sub(xAxis).sub(yAxis).add(zAxis); // -x -y +z
		corners[7] = new Vector3f(center).sub(xAxis).sub(yAxis).sub(zAxis); // -x -y -z

		return corners;
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

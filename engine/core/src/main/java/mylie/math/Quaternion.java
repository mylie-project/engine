package mylie.math;

/**
 * Represents a quaternion, a mathematical entity used in three-dimensional space
 * to represent rotations, orientations, and complex numbers in higher dimensions.
 * This interface provides methods for common quaternion operations including addition,
 * subtraction, multiplication, division, and transformation.
 *
 * @param <N> the type of number used for the quaternion components
 */
@SuppressWarnings("unused")
public interface Quaternion<N extends Number> {

	/**
	 * Adds this quaternion to the given quaternion.
	 *
	 * @param other the quaternion to add
	 * @return a new quaternion that is the sum of this quaternion and the given quaternion
	 */
	Quaternion<N> add(Quaternion<N> other);

	/**
	 * Divides this quaternion by the given quaternion.
	 *
	 * @param other the quaternion to divide by
	 * @return a new quaternion resulting from the division
	 */
	Quaternion<N> div(Quaternion<N> other);

	/**
	 * Multiplies this quaternion by the given quaternion.
	 *
	 * @param other the quaternion to multiply with
	 * @return a new quaternion resulting from the multiplication
	 */
	Quaternion<N> mul(Quaternion<N> other);

	/**
	 * Subtracts the given quaternion from this quaternion.
	 *
	 * @param other the quaternion to subtract
	 * @return a new quaternion representing the result of the subtraction
	 */
	Quaternion<N> sub(Quaternion<N> other);

	/**
	 * Calculates the dot product of this quaternion and the given quaternion.
	 *
	 * @param other the quaternion with which to compute the dot product
	 * @return the dot product as a floating-point value
	 */
	float dot(Quaternion<N> other);

	/**
	 * Computes the conjugate of this quaternion.
	 *
	 * @return a new quaternion representing the conjugate
	 */
	Quaternion<N> conjugate();

	/**
	 * Computes the inverse of this quaternion.
	 *
	 * @return a new quaternion that is the inverse of this quaternion
	 */
	Quaternion<N> inverse();

	/**
	 * Computes the normalized form of this quaternion.
	 *
	 * @return a new quaternion that is normalized
	 */
	Quaternion<N> normalize();

	/**
	 * Computes the difference between this quaternion and the given quaternion.
	 *
	 * @param other the quaternion to compare with
	 * @return a new quaternion representing the difference
	 */
	Quaternion<N> difference(Quaternion<N> other);

	/**
	 * Transforms the given 3D vector using this quaternion's rotation.
	 *
	 * @param vector the vector to transform
	 * @return a new vector resulting from the transformation
	 */
	Vec3<N> transform(Vec3<N> vector);

	/**
	 * Rotates this quaternion around the given axis by a specified angle.
	 *
	 * @param angle the angle of rotation in radians
	 * @param axis  the rotation axis
	 * @return a new quaternion representing the result of the rotation
	 */
	Quaternion<N> rotateAxis(float angle, Vec3<N> axis);

	/**
	 * Creates a quaternion representing a rotation around the given axis by a specified angle.
	 *
	 * @param radians the angle of rotation in radians
	 * @param axis    the rotation axis
	 * @return a new quaternion representing the rotation
	 */
	Quaternion<N> rotationAxis(float radians, Vec3<N> axis);

	/**
	 * Creates and returns a new quaternion with the default values representing no rotation.
	 *
	 * @return a new quaternion initialized with values (0, 0, 0, 1)
	 */
	static Quaternion<Float> f() {
		return new Quaternionf(0, 0, 0, 1);
	}

	/**
	 * Creates a new quaternion using the specified components.
	 *
	 * @param x the x-component of the quaternion
	 * @param y the y-component of the quaternion
	 * @param z the z-component of the quaternion
	 * @param w the w-component (scalar) of the quaternion
	 * @return a new Quaternion instance initialized with the provided components
	 */
	static Quaternion<Float> f(float x, float y, float z, float w) {
		return new Quaternionf(x, y, z, w);
	}

}

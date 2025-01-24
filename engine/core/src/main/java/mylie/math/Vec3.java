package mylie.math;

/**
 * Represents a three-dimensional vector with generic numeric components.
 * Provides utility constants and methods to manipulate 3D vectors, along with
 * functionality for swizzling and component-based vector construction.
 *
 * @param <N>
 *            The type of the components of the vector, which must extend
 *            {@link Number}.
 */
@SuppressWarnings("unused")
public interface Vec3<N extends Number> extends Vec<N, Vec3<N>> {

	/**
	 * The X-axis component of the 3D vector. This is a shared reference from the
	 * {@link Vec2#X}.
	 */
	Component X = Vec2.X;

	/**
	 * The Y-axis component of the 3D vector. This is a shared reference from the
	 * {@link Vec2#Y}.
	 */
	Component Y = Vec2.Y;

	/**
	 * The Z-axis component of the 3D vector. Unique to the {@link Vec3}.
	 */
	Component Z = new Component("Z");

	/**
	 * Computes the dot product of this vector and another 3D vector.
	 *
	 * @param other
	 *            The other vector to compute the dot product with.
	 * @return The scalar dot product of the two vectors.
	 */
	float dot(Vec3<Float> other);

	/**
	 * Computes the cross product of this vector and another 3D vector.
	 *
	 * @param other
	 *            The other vector to compute the cross product with.
	 * @return A new {@link Vec3} representing the cross product of the two vectors.
	 */
	Vec3<Float> cross(Vec3<Float> other);

	/**
	 * Creates a 2D vector by swizzling components of this 3D vector.
	 *
	 * @param x
	 *            The component to use for the x-coordinate of the new vector.
	 * @param y
	 *            The component to use for the y-coordinate of the new vector.
	 * @return A new {@link Vec2} with the specified components.
	 */
	Vec2<Float> swizzle(Component x, Component y);

	/**
	 * Creates a 3D vector by swizzling components of this 3D vector.
	 *
	 * @param x
	 *            The component to use for the x-coordinate of the new vector.
	 * @param y
	 *            The component to use for the y-coordinate of the new vector.
	 * @param z
	 *            The component to use for the z-coordinate of the new vector.
	 * @return A new {@link Vec3} with the specified components.
	 */
	Vec3<Float> swizzle(Component x, Component y, Component z);

	/**
	 * Returns the zero vector (0, 0, 0).
	 *
	 * @return A {@link Vec3} instance representing the zero vector.
	 */
	Vec3<Float> zero();

	/**
	 * Returns the vector with all components set to one (1, 1, 1).
	 *
	 * @return A {@link Vec3} instance representing the vector (1, 1, 1).
	 */
	Vec3<Float> one();

	/**
	 * Returns the vector with all components set to negative one (-1, -1, -1).
	 *
	 * @return A {@link Vec3} instance representing the vector (-1, -1, -1).
	 */
	Vec3<Float> negativeOne();

	/**
	 * Returns the unit vector along the X-axis (1, 0, 0).
	 *
	 * @return A {@link Vec3} instance representing the unit vector along the
	 *         X-axis.
	 */
	Vec3<Float> unitX();

	/**
	 * Returns the unit vector along the Y-axis (0, 1, 0).
	 *
	 * @return A {@link Vec3} instance representing the unit vector along the
	 *         Y-axis.
	 */
	Vec3<Float> unitY();

	/**
	 * Returns the unit vector along the Z-axis (0, 0, 1).
	 *
	 * @return A {@link Vec3} instance representing the unit vector along the
	 *         Z-axis.
	 */
	Vec3<Float> unitZ();

	/**
	 * Returns the unit vector in the negative X-axis direction (-1, 0, 0).
	 *
	 * @return A {@link Vec3} instance representing the unit vector in the negative
	 *         X-axis direction.
	 */
	Vec3<Float> negativeUnitX();

	/**
	 * Returns the unit vector in the negative Y-axis direction (0, -1, 0).
	 *
	 * @return A {@link Vec3} instance representing the unit vector in the negative
	 *         Y-axis direction.
	 */
	Vec3<Float> negativeUnitY();

	/**
	 * Returns the unit vector in the negative Z-axis direction (0, 0, -1).
	 *
	 * @return A {@link Vec3} instance representing the unit vector in the negative
	 *         Z-axis direction.
	 */
	Vec3<Float> negativeUnitZ();

	/**
	 * Creates a new 3D vector with the specified x, y, and z components.
	 *
	 * @param x
	 *            The x-component of the vector.
	 * @param y
	 *            The y-component of the vector.
	 * @param z
	 *            The z-component of the vector.
	 * @return A new Vec3 instance with the given components.
	 */
	static Vec3<Float> of(float x, float y, float z) {
		return new Vec3f(x, y, z);
	}
}

package mylie.math;

/**
 * Represents a 2-dimensional vector with generic numeric components. Provides
 * operations to manipulate 2D vectors and utility methods for creating and
 * transforming vector instances.
 *
 * @param <N>
 *            The type of the components of the vector, which must extend
 *            {@link Number}.
 */
@SuppressWarnings("unused")
public interface Vec2<N extends Number> extends Vec<N, Vec2<N>> {

	/**
	 * Represents the X component of the vector, commonly used as the horizontal
	 * axis.
	 */
	Component X = new Component("X");

	/**
	 * Represents the Y component of the vector, commonly used as the vertical axis.
	 */
	Component Y = new Component("Y");

	/**
	 * Creates a new 2D vector by remapping the components of this vector using the
	 * provided components.
	 *
	 * @param x
	 *            The component to use as the new X value.
	 * @param y
	 *            The component to use as the new Y value.
	 * @return A new vector with components remapped to the specified
	 *         {@link Component}s.
	 */
	Vec2<Float> swizzle(Component x, Component y);

	/**
	 * Returns a unit vector representing the X-axis direction. The vector has a
	 * magnitude of 1.0 and is oriented along the positive X-axis.
	 *
	 * @return A new {@link Vec2} instance representing the unit vector along the
	 *         X-axis.
	 */
	Vec2<Float> unitX();

	/**
	 * Returns a unit vector representing the Y-axis direction. The vector has a
	 * magnitude of 1.0 and is oriented along the positive Y-axis.
	 *
	 * @return A new {@link Vec2} instance representing the unit vector along the
	 *         Y-axis.
	 */
	Vec2<Float> unitY();

	/**
	 * Returns a vector with both components set to zero (0, 0).
	 *
	 * @return A new {@link Vec2} instance representing the zero vector.
	 */
	Vec2<Float> zero();

	/**
	 * Returns a vector with both components set to one (1, 1).
	 *
	 * @return A new {@link Vec2} instance with both components set to one.
	 */
	Vec2<Float> one();

	/**
	 * Returns a vector with both components set to negative one (-1, -1).
	 *
	 * @return A new {@link Vec2} instance with both components set to negative one.
	 */
	Vec2<Float> negativeOne();

	/**
	 * Returns a unit vector along the negative X-axis (magnitude 1.0 in -X
	 * direction).
	 *
	 * @return A new {@link Vec2} instance representing the unit vector along the
	 *         negative X-axis.
	 */
	Vec2<Float> negativeUnitX();

	/**
	 * Returns a unit vector along the negative Y-axis (magnitude 1.0 in -Y
	 * direction).
	 *
	 * @return A new {@link Vec2} instance representing the unit vector along the
	 *         negative Y-axis.
	 */
	Vec2<Float> negativeUnitY();

	/**
	 * Creates a new 2D vector with the specified x and y components.
	 *
	 * @param x
	 *            The x-component of the vector.
	 * @param y
	 *            The y-component of the vector.
	 * @return A new Vec2 instance with the given components.
	 */
	static Vec2<Float> of(float x, float y) {
		return new Vec2f(x, y);
	}
}

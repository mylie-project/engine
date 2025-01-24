package mylie.math;

/**
 * Represents a 2D vector with generic numerical components, extending the general
 * {@link Vec} interface with additional behavior specific to two-dimensional vectors.
 *
 * @param <N> The type of number used for the vector components (e.g., Float, Double, Integer).
 */
@SuppressWarnings("unused")
public interface Vec2<N extends Number> extends Vec<N, Vec2<N>> {

	/**
	 * Gets the X component of the vector.
	 *
	 * @return The X component as a value of type {@code N}.
	 */
	N getX();

	/**
	 * Gets the Y component of the vector.
	 *
	 * @return The Y component as a value of type {@code N}.
	 */
	N getY();

	/**
	 * Represents the X axis as a swizzle component.
	 */
	Component X = new Component("X");

	/**
	 * Represents the Y axis as a swizzle component.
	 */
	Component Y = new Component("Y");

	/**
	 * Creates a new 2D vector by reordering components based on the provided axes.
	 *
	 * @param x The component to map to the new vector's X axis.
	 * @param y The component to map to the new vector's Y axis.
	 * @return A new {@link Vec2} instance with reordered components.
	 */
	Vec2<N> swizzle(Component x, Component y);

	/**
	 * Returns a unit vector pointing along the X axis.
	 *
	 * @return A {@link Vec2} instance representing the unit vector (1, 0).
	 */
	Vec2<N> unitX();

	/**
	 * Returns a unit vector pointing along the Y axis.
	 *
	 * @return A {@link Vec2} instance representing the unit vector (0, 1).
	 */
	Vec2<N> unitY();

	/**
	 * Returns the zero vector.
	 *
	 * @return A {@link Vec2} instance with both components equal to 0.
	 */
	Vec2<N> zero();

	/**
	 * Returns a vector with both components equal to 1.
	 *
	 * @return A {@link Vec2} instance with components (1, 1).
	 */
	Vec2<N> one();

	/**
	 * Returns a vector with both components equal to -1.
	 *
	 * @return A {@link Vec2} instance with components (-1, -1).
	 */
	Vec2<N> negativeOne();

	/**
	 * Returns a unit vector pointing along the negative X axis.
	 *
	 * @return A {@link Vec2} instance representing the vector (-1, 0).
	 */
	Vec2<N> negativeUnitX();

	/**
	 * Returns a unit vector pointing along the negative Y axis.
	 *
	 * @return A {@link Vec2} instance representing the vector (0, -1).
	 */
	Vec2<N> negativeUnitY();

	/**
	 * Creates a new 2D vector with the specified X and Y components.
	 *
	 * @param x The X component of the new vector.
	 * @param y The Y component of the new vector.
	 * @return A new {@link Vec2} instance with the specified components.
	 */
	static Vec2<Float> of(float x, float y) {
		return new Vec2f(x, y);
	}

	/**
	 * Creates a new 2D vector with the specified X and Y components.
	 *
	 * @param x The X component of the new vector.
	 * @param y The Y component of the new vector.
	 * @return A new {@link Vec2} instance with the specified components.
	 */
	static Vec2<Double> of(double x, double y) {
		return new Vec2d(x, y);
	}
}

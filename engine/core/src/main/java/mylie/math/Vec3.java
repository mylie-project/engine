package mylie.math;

/**
 * Represents a three-dimensional vector with numeric components.
 *
 * @param <N> the type of number used for the vector components
 */
@SuppressWarnings("unused")
public interface Vec3<N extends Number> extends Vec<N, Vec3<N>> {

    /**
     * Gets the X component of the vector.
     *
     * @return the X component
     */
    N getX();

    /**
     * Gets the Y component of the vector.
     *
     * @return the Y component
     */
    N getY();

    /**
     * Gets the Z component of the vector.
     *
     * @return the Z component
     */
    N getZ();

    /**
     * The X-axis component identifier.
     */
    Component X = Vec2.X;

    /**
     * The Y-axis component identifier.
     */
    Component Y = Vec2.Y;

    /**
     * The Z-axis component identifier.
     */
    Component Z = new Component("Z");

    /**
     * Computes the dot product with another vector.
     *
     * @param other the other vector
     * @return the dot product
     */
    N dot(Vec3<N> other);

    /**
     * Computes the cross product with another vector.
     *
     * @param other the other vector
     * @return a new vector representing the cross product
     */
    Vec3<N> cross(Vec3<N> other);

    /**
     * Swizzles the vector to create a new 2D vector using the specified components.
     *
     * @param x the first component
     * @param y the second component
     * @return a new 2D vector
     */
    Vec2<N> swizzle(Component x, Component y);

    /**
     * Swizzles the vector to create a new 3D vector using the specified components.
     *
     * @param x the first component
     * @param y the second component
     * @param z the third component
     * @return a new 3D vector
     */
    Vec3<N> swizzle(Component x, Component y, Component z);

    /**
     * Creates a vector with all components set to zero.
     *
     * @return a zero vector
     */
    Vec3<N> zero();

    /**
     * Creates a vector with all components set to one.
     *
     * @return a vector with all components set to one
     */
    Vec3<N> one();

    /**
     * Creates a vector with all components set to negative one.
     *
     * @return a vector with all components set to negative one
     */
    Vec3<N> negativeOne();

    /**
     * Creates a vector representing the unit vector along the X-axis.
     *
     * @return the unit X vector
     */
    Vec3<N> unitX();

    /**
     * Creates a vector representing the unit vector along the Y-axis.
     *
     * @return the unit Y vector
     */
    Vec3<N> unitY();

    /**
     * Creates a vector representing the unit vector along the Z-axis.
     *
     * @return the unit Z vector
     */
    Vec3<N> unitZ();

    /**
     * Creates a vector representing the negative unit vector along the X-axis.
     *
     * @return the negative unit X vector
     */
    Vec3<N> negativeUnitX();

    /**
     * Creates a vector representing the negative unit vector along the Y-axis.
     *
     * @return the negative unit Y vector
     */
    Vec3<N> negativeUnitY();

    /**
     * Creates a vector representing the negative unit vector along the Z-axis.
     *
     * @return the negative unit Z vector
     */
    Vec3<N> negativeUnitZ();

    /**
     * Creates a new 3D vector with the specified components.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @return a new vector with the specified values
     */
    static Vec3<Float> of(float x, float y, float z) {
        return new Vec3f(x, y, z);
    }

    /**
     * Creates a new 3D vector with the specified components.
     *
     * @param x the X component
     * @param y the Y component
     * @param z the Z component
     * @return a new vector with the specified values
     */
    static Vec3<Double> of(double x, double y, double z) {
        return new Vec3d(x, y, z);
    }
}

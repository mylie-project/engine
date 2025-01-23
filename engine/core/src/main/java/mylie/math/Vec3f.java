package mylie.math;

@SuppressWarnings({"SuspiciousNameCombination", "unused"})
public record Vec3f(float x, float y, float z) implements Vec3<Vec3f, Vec2f, Vec3f, Float> {

	/**
	 * Computes the cross product of this vector with another vector.
	 *
	 * @param other
	 *            the vector to compute the cross product with
	 * @return a new {@code Vec3f} representing the cross product
	 */
	@Override
	public Vec3f cross(Vec3f other) {
		return new Vec3f(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z,
				this.x * other.y - this.y * other.x);
	}

	/**
	 * Adds this vector to another vector component-wise.
	 *
	 * @param other
	 *            the vector to add
	 * @return a new {@code Vec3f} representing the sum
	 */
	@Override
	public Vec3f add(Vec3f other) {
		return new Vec3f(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	/**
	 * Subtracts another vector from this vector component-wise.
	 *
	 * @param other
	 *            the vector to subtract
	 * @return a new {@code Vec3f} representing the difference
	 */
	@Override
	public Vec3f sub(Vec3f other) {
		return new Vec3f(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	/**
	 * Multiplies this vector by another vector component-wise.
	 *
	 * @param other
	 *            the vector to multiply
	 * @return a new {@code Vec3f} representing the product
	 */
	@Override
	public Vec3f mul(Vec3f other) {
		return new Vec3f(this.x * other.x, this.y * other.y, this.z * other.z);
	}

	/**
	 * Divides this vector by another vector component-wise.
	 *
	 * @param other
	 *            the vector to divide by
	 * @return a new {@code Vec3f} representing the quotient
	 */
	@Override
	public Vec3f div(Vec3f other) {
		return new Vec3f(this.x / other.x, this.y / other.y, this.z / other.z);
	}

	/**
	 * Computes the dot product of this vector with another vector.
	 *
	 * @param other
	 *            the vector to compute the dot product with
	 * @return the scalar dot product
	 */
	@Override
	public float dot(Vec3f other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Computes the length (magnitude) of this vector.
	 *
	 * @return the length of the vector
	 */
	@Override
	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	/**
	 * Normalizes this vector, scaling it to a unit vector.
	 *
	 * @return a new {@code Vec3f} normalized to unit length
	 */
	@Override
	public Vec3f normalize() {
		float length = length();
		return new Vec3f(this.x / length, this.y / length, this.z / length);
	}

	/**
	 * Negates this vector, reversing its direction.
	 *
	 * @return a new {@code Vec3f} representing the negation
	 */
	@Override
	public Vec3f negate() {
		return new Vec3f(-this.x, -this.y, -this.z);
	}

	/**
	 * Creates a 2D vector from the x and y components of this vector.
	 *
	 * @return a new {@code Vec2f} containing the x and y components
	 */
	@Override
	public Vec2f xy() {
		return new Vec2f(this.x, this.y);
	}

	/**
	 * Creates a 2D vector from the x and z components of this vector.
	 *
	 * @return a new {@code Vec2f} containing the x and z components
	 */
	@Override
	public Vec2f xz() {
		return new Vec2f(this.x, this.z);
	}

	/**
	 * Creates a 2D vector from the y and z components of this vector.
	 *
	 * @return a new {@code Vec2f} containing the y and z components
	 */
	@Override
	public Vec2f yz() {
		return new Vec2f(this.y, this.z);
	}

	/**
	 * Creates a 2D vector from the y and x components of this vector.
	 *
	 * @return a new {@code Vec2f} containing the y and x components
	 */
	@Override
	public Vec2f yx() {
		return new Vec2f(this.y, this.x);
	}

	/**
	 * Creates a 2D vector from the z and x components of this vector.
	 *
	 * @return a new {@code Vec2f} containing the z and x components
	 */
	@Override
	public Vec2f zx() {
		return new Vec2f(this.z, this.x);
	}

	/**
	 * Creates a 2D vector from the z and y components of this vector.
	 *
	 * @return a new {@code Vec2f} containing the z and y components
	 */
	@Override
	public Vec2f zy() {
		return new Vec2f(this.z, this.y);
	}

	/**
	 * Creates a 2D vector with both components set to the x-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2f} containing the x-component in both positions
	 */
	@Override
	public Vec2f xx() {
		return new Vec2f(this.x, this.x);
	}

	/**
	 * Creates a 2D vector with both components set to the y-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2f} containing the y-component in both positions
	 */
	@Override
	public Vec2f yy() {
		return new Vec2f(this.y, this.y);
	}

	/**
	 * Creates a 2D vector with both components set to the z-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2f} containing the z-component in both positions
	 */
	@Override
	public Vec2f zz() {
		return new Vec2f(this.z, this.z);
	}
}

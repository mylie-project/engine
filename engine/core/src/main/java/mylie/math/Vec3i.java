package mylie.math;

@SuppressWarnings({"SuspiciousNameCombination", "unused"})
public record Vec3i(int x, int y, int z) implements Vec3<Vec3i, Vec2i, Vec3i, Integer> {

	/**
	 * Computes the cross product of this vector with another vector.
	 *
	 * @param other
	 *            the vector to compute the cross product with
	 * @return a new {@code Vec3f} representing the cross product
	 */
	@Override
	public Vec3i cross(Vec3i other) {
		return new Vec3i(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z,
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
	public Vec3i add(Vec3i other) {
		return new Vec3i(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	/**
	 * Subtracts another vector from this vector component-wise.
	 *
	 * @param other
	 *            the vector to subtract
	 * @return a new {@code Vec3f} representing the difference
	 */
	@Override
	public Vec3i sub(Vec3i other) {
		return new Vec3i(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	/**
	 * Multiplies this vector by another vector component-wise.
	 *
	 * @param other
	 *            the vector to multiply
	 * @return a new {@code Vec3f} representing the product
	 */
	@Override
	public Vec3i mul(Vec3i other) {
		return new Vec3i(this.x * other.x, this.y * other.y, this.z * other.z);
	}

	/**
	 * Divides this vector by another vector component-wise.
	 *
	 * @param other
	 *            the vector to divide by
	 * @return a new {@code Vec3f} representing the quotient
	 */
	@Override
	public Vec3i div(Vec3i other) {
		return new Vec3i(this.x / other.x, this.y / other.y, this.z / other.z);
	}

	/**
	 * Computes the dot product of this vector with another vector.
	 *
	 * @param other
	 *            the vector to compute the dot product with
	 * @return the scalar dot product
	 */
	@Override
	public float dot(Vec3i other) {
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
	public Vec3i normalize() {
		float length = length();
		return new Vec3i((int) (this.x / length), (int) (this.y / length), (int) (this.z / length));
	}

	/**
	 * Negates this vector, reversing its direction.
	 *
	 * @return a new {@code Vec3f} representing the negation
	 */
	@Override
	public Vec3i negate() {
		return new Vec3i(-this.x, -this.y, -this.z);
	}

	/**
	 * Creates a 2D vector from the x and y components of this vector.
	 *
	 * @return a new {@code Vec2i} containing the x and y components
	 */
	@Override
	public Vec2i xy() {
		return new Vec2i(this.x, this.y);
	}

	/**
	 * Creates a 2D vector from the x and z components of this vector.
	 *
	 * @return a new {@code Vec2i} containing the x and z components
	 */
	@Override
	public Vec2i xz() {
		return new Vec2i(this.x, this.z);
	}

	/**
	 * Creates a 2D vector from the y and z components of this vector.
	 *
	 * @return a new {@code Vec2i} containing the y and z components
	 */
	@Override
	public Vec2i yz() {
		return new Vec2i(this.y, this.z);
	}

	/**
	 * Creates a 2D vector from the y and x components of this vector.
	 *
	 * @return a new {@code Vec2i} containing the y and x components
	 */
	@Override
	public Vec2i yx() {
		return new Vec2i(this.y, this.x);
	}

	/**
	 * Creates a 2D vector from the z and x components of this vector.
	 *
	 * @return a new {@code Vec2i} containing the z and x components
	 */
	@Override
	public Vec2i zx() {
		return new Vec2i(this.z, this.x);
	}

	/**
	 * Creates a 2D vector from the z and y components of this vector.
	 *
	 * @return a new {@code Vec2i} containing the z and y components
	 */
	@Override
	public Vec2i zy() {
		return new Vec2i(this.z, this.y);
	}

	/**
	 * Creates a 2D vector with both components set to the x-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2i} containing the x-component in both positions
	 */
	@Override
	public Vec2i xx() {
		return new Vec2i(this.x, this.x);
	}

	/**
	 * Creates a 2D vector with both components set to the y-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2i} containing the y-component in both positions
	 */
	@Override
	public Vec2i yy() {
		return new Vec2i(this.y, this.y);
	}

	/**
	 * Creates a 2D vector with both components set to the z-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2i} containing the z-component in both positions
	 */
	@Override
	public Vec2i zz() {
		return new Vec2i(this.z, this.z);
	}
}

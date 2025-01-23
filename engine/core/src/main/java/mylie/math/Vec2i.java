package mylie.math;

@SuppressWarnings({"SuspiciousNameCombination", "unused"})
public record Vec2i(int x, int y) implements Vec2<Vec2i, Integer> {

	/**
	 * Adds the corresponding components of this vector and the given vector.
	 *
	 * @param other
	 *            the other vector to add
	 * @return a new {@code Vec2f} instance representing the sum
	 */
	@Override
	public Vec2i add(Vec2i other) {
		return new Vec2i(this.x + other.x, this.y + other.y);
	}

	/**
	 * Subtracts the corresponding components of the given vector from this vector.
	 *
	 * @param other
	 *            the other vector to subtract
	 * @return a new {@code Vec2f} instance representing the difference
	 */
	@Override
	public Vec2i sub(Vec2i other) {
		return new Vec2i(this.x - other.x, this.y - other.y);
	}

	/**
	 * Multiplies the corresponding components of this vector and the given vector.
	 *
	 * @param other
	 *            the other vector to multiply
	 * @return a new {@code Vec2f} instance representing the product
	 */
	@Override
	public Vec2i mul(Vec2i other) {
		return new Vec2i(this.x * other.x, this.y * other.y);
	}

	/**
	 * Divides the corresponding components of this vector by the given vector.
	 *
	 * @param other
	 *            the other vector to divide
	 * @return a new {@code Vec2f} instance representing the quotient
	 */
	@Override
	public Vec2i div(Vec2i other) {
		return new Vec2i(this.x / other.x, this.y / other.y);
	}

	/**
	 * Calculates the dot product of this vector and the given vector.
	 *
	 * @param vec2f
	 *            the other vector
	 * @return the dot product as a floating-point value
	 */
	@Override
	public float dot(Vec2i vec2f) {
		return this.x * vec2f.x + this.y * vec2f.y;
	}

	/**
	 * Computes the length (magnitude) of this vector.
	 *
	 * @return the magnitude of the vector as a floating-point value
	 */
	@Override
	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y);
	}

	/**
	 * Normalizes this vector to a unit vector.
	 *
	 * @return a new {@code Vec2f} instance representing the normalized vector, or a
	 *         zero vector if the length is zero
	 */
	@Override
	public Vec2i normalize() {
		float len = length();
		if (len == 0) {
			return new Vec2i(0, 0);
		}
		return new Vec2i((int) (this.x / len), (int) (this.y / len));
	}

	/**
	 * Negates this vector (reverses its direction).
	 *
	 * @return a new {@code Vec2f} instance representing the negated vector
	 */
	@Override
	public Vec2i negate() {
		return new Vec2i(-this.x, -this.y);
	}

	/**
	 * Creates a new vector with both components equal to the x-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2f} instance with components (x, x)
	 */
	@Override
	public Vec2i xx() {
		return new Vec2i(this.x, this.x);
	}

	/**
	 * Creates a new vector with both components equal to the y-component of this
	 * vector.
	 *
	 * @return a new {@code Vec2f} instance with components (y, y)
	 */
	@Override
	public Vec2i yy() {
		return new Vec2i(this.y, this.y);
	}

	/**
	 * Creates a new vector by swapping the components of this vector.
	 *
	 * @return a new {@code Vec2f} instance with components (y, x)
	 */
	@Override
	public Vec2i yx() {
		return new Vec2i(this.y, this.x);
	}
}

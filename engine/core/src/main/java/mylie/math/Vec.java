package mylie.math;

@SuppressWarnings("unused")
public interface Vec<N extends Number, V extends Vec<N, V>> {

	/**
	 * Adds the specified vector to this vector.
	 *
	 * @param other
	 *            The vector to add.
	 * @return A new vector representing the element-wise sum of this vector and the
	 *         provided vector.
	 */
	V add(V other);

	/**
	 * Subtracts the specified vector from this vector.
	 *
	 * @param other
	 *            The vector to subtract.
	 * @return A new vector representing the element-wise difference between this
	 *         vector and the provided vector.
	 */
	V sub(V other);

	/**
	 * Multiplies this vector by the specified vector element-wise.
	 *
	 * @param other
	 *            The vector to multiply by.
	 * @return A new vector representing the element-wise product of this vector and
	 *         the provided vector.
	 */
	V mul(V other);

	/**
	 * Divides this vector by the specified vector element-wise.
	 *
	 * @param other
	 *            The vector to divide by.
	 * @return A new vector representing the element-wise quotient of this vector
	 *         and the provided vector.
	 */
	V div(V other);

	/**
	 * Performs an element-wise multiply-add operation: multiplies the specified
	 * factor vector with the given vector and adds the result to this vector.
	 *
	 * @param other
	 *            The vector to add after multiplication.
	 * @param factor
	 *            The factor vector to multiply.
	 * @return A new vector resulting from the element-wise multiply-add operation.
	 */
	V mulAdd(V other, V factor);

	/**
	 * Negates this vector, reversing the sign of each of its components.
	 *
	 * @return A new vector with each component negated.
	 */
	V negate();

	/**
	 * Normalizes this vector to have a length of 1, maintaining its direction. If
	 * the vector has zero length, the behavior is undefined.
	 *
	 * @return A new normalized vector with the same direction as this vector but
	 *         with a magnitude of 1.
	 */
	V normalize();

}

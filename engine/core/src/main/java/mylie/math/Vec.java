package mylie.math;

@SuppressWarnings("unused")
public interface Vec<V extends Vec<V, N>, N extends Number> {

	/**
	 * Adds the given vector to the current vector.
	 *
	 * @param other the vector to add
	 * @return a new vector representing the result of the addition
	 */
	V add(V other);

	/**
	 * Subtracts the given vector from the current vector.
	 *
	 * @param other the vector to subtract
	 * @return a new vector representing the result of the subtraction
	 */
	V sub(V other);

	/**
	 * Multiplies the current vector by the given vector.
	 *
	 * @param other the vector to multiply by
	 * @return a new vector representing the result of the multiplication
	 */
	V mul(V other);

	/**
	 * Divides the current vector by the given vector.
	 *
	 * @param other the vector to divide by
	 * @return a new vector representing the result of the division
	 */
	V div(V other);

	/**
	 * Computes the dot product of the current vector and the given vector.
	 *
	 * @param other the vector to compute the dot product with
	 * @return the dot product as a float
	 */
	float dot(V other);

	/**
	 * Computes the length (magnitude) of the current vector.
	 *
	 * @return the length of the vector as a float
	 */
	float length();

	/**
	 * Normalizes the current vector to have a length of 1.
	 *
	 * @return a new vector representing the normalized vector
	 */
	V normalize();

	/**
	 * Negates the current vector, reversing its direction.
	 *
	 * @return a new vector representing the negated vector
	 */
	V negate();
}

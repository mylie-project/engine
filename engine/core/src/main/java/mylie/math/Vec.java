package mylie.math;

public interface Vec<N extends Number, V extends Vec<N, V>> {

	/**
	 * Adds the given vector to the current vector and returns the resulting vector.
	 *
	 * @param other the vector to add
	 * @return the resultant vector after addition
	 */
	V add(V other);

	/**
	 * Subtracts the given vector from the current vector and returns the resulting vector.
	 *
	 * @param other the vector to subtract
	 * @return the resultant vector after subtraction
	 */
	V sub(V other);

	/**
	 * Multiplies the current vector with the given vector and returns the resulting vector.
	 *
	 * @param other the vector to multiply
	 * @return the resultant vector after multiplication
	 */
	V mul(V other);

	/**
	 * Divides the current vector by the given vector and returns the resulting vector.
	 *
	 * @param other the vector to divide by
	 * @return the resultant vector after division
	 */
	V div(V other);

	/**
	 * Performs a multiply-add operation where the current vector is multiplied by the given factor and then
	 * added to the other vector.
	 *
	 * @param other  the vector to add after multiplication
	 * @param factor the vector to multiply with the current vector
	 * @return the resultant vector after the multiply-add operation
	 */
	V mulAdd(V other, V factor);

	/**
	 * Negates the current vector and returns the resulting vector.
	 *
	 * @return the negated vector
	 */
	V negate();

	/**
	 * Normalizes the current vector to a unit vector and returns the resulting vector.
	 *
	 * @return the normalized vector
	 */
	V normalize();

	/**
	 * Computes the element-wise maximum of the current vector and the given vector.
	 *
	 * @param other the other vector to compare
	 * @return the resultant vector containing the maximum values for each element
	 */
	V max(V other);

	/**
	 * Computes the element-wise minimum of the current vector and the given vector.
	 *
	 * @param other the other vector to compare
	 * @return the resultant vector containing the minimum values for each element
	 */
	V min(V other);

}

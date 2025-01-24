package mylie.math;

public interface Mat<N extends Number, M extends Mat<N, M>> {

	/**
	 * Adds the given matrix to the current matrix and returns the resultant matrix.
	 *
	 * @param other the matrix to add
	 * @return the resultant matrix after addition
	 */
	M add(M other);

	/**
	 * Subtracts the given matrix from the current matrix and returns the resultant matrix.
	 *
	 * @param other the matrix to subtract
	 * @return the resultant matrix after subtraction
	 */
	M sub(M other);

	/**
	 * Multiplies the current matrix with the given matrix and returns the resultant matrix.
	 *
	 * @param other the matrix to multiply
	 * @return the resultant matrix after multiplication
	 */
	M mul(M other);

	/**
	 * Transposes the current matrix and returns the resulting matrix.
	 *
	 * @return the transposed matrix
	 */
	M transpose();

	/**
	 * Calculates the determinant of the current matrix.
	 *
	 * @return the determinant as a number
	 */
	N determinant();

	/**
	 * Calculates the inverse of the current matrix and returns the resulting matrix.
	 *
	 * @return the inverse of the matrix
	 * @throws ArithmeticException if the matrix is not invertible
	 */
	M inverse() throws ArithmeticException;

	/**
	 * Returns the identity matrix for the current matrix type.
	 *
	 * @return an identity matrix of the same dimensions and type as the current matrix
	 */
	M identity();
}

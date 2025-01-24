package mylie.math;

/**
 * Represents a 4x4 matrix of numerical components, providing methods for transformation
 * and factory methods for creating specific implementations of 4x4 matrices.
 *
 * @param <N> the type of number used for the matrix components
 */
@SuppressWarnings("unused")
public interface Mat4<N extends Number> extends Mat<N, Mat4<N>> {
	/**
	 * Applies the matrix transformation to a given vector.
	 *
	 * @param vec the vector to transform
	 * @return the transformed vector
	 */
	Vec3<N> transform(Vec3<N> vec);

	/**
	 * Creates a new instance of a 4x4 matrix with the specified float values.
	 *
	 * @param m00 the value of the first row, first column
	 * @param m01 the value of the first row, second column
	 * @param m02 the value of the first row, third column
	 * @param m03 the value of the first row, fourth column
	 * @param m10 the value of the second row, first column
	 * @param m11 the value of the second row, second column
	 * @param m12 the value of the second row, third column
	 * @param m13 the value of the second row, fourth column
	 * @param m20 the value of the third row, first column
	 * @param m21 the value of the third row, second column
	 * @param m22 the value of the third row, third column
	 * @param m23 the value of the third row, fourth column
	 * @param m30 the value of the fourth row, first column
	 * @param m31 the value of the fourth row, second column
	 * @param m32 the value of the fourth row, third column
	 * @param m33 the value of the fourth row, fourth column
	 * @return a new 4x4 matrix with the specified values
	 */
	static Mat4<Float> f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		return new Mat4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
	}

	/**
	 * Retrieves the identity matrix for a 4x4 matrix of type Float.
	 *
	 * @return an immutable 4x4 identity matrix with Float components
	 */
	static Mat4<Float> f() {
		return Mat4f.IDENTITY;
	}
}

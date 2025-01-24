package mylie.math;

public interface Mat4<N extends Number> extends Mat<N, Mat4<N>> {
	/**
	 * Applies the matrix transformation to a given vector.
	 *
	 * @param vec the vector to transform
	 * @return the transformed vector
	 */
	Vec3<N> transform(Vec3<N> vec);

	static Mat4<Float> f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13,
			float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		return new Mat4f(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
	}

	static Mat4<Float> f() {
		return Mat4f.IDENTITY;
	}
}

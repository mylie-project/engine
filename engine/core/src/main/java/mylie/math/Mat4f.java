package mylie.math;

record Mat4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20,
		float m21, float m22, float m23, float m30, float m31, float m32, float m33) implements Mat4<Float> {

	static final Mat4f IDENTITY = new Mat4f(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f);

	@Override
	public Vec3<Float> transform(Vec3<Float> vector) {
		Vec3f vec = Vec3f.cast(vector);
		float x = m00 * vec.x() + m01 * vec.y() + m02 * vec.z() + m03;
		float y = m10 * vec.x() + m11 * vec.y() + m12 * vec.z() + m13;
		float z = m20 * vec.x() + m21 * vec.y() + m22 * vec.z() + m23;
		return Vec3.of(x, y, z);
	}

	@Override
	public Mat4<Float> add(Mat4<Float> other) {
		Mat4f o = Mat4f.cast(other);
		return new Mat4f(m00 + o.m00, m01 + o.m01, m02 + o.m02, m03 + o.m03, m10 + o.m10, m11 + o.m11, m12 + o.m12,
				m13 + o.m13, m20 + o.m20, m21 + o.m21, m22 + o.m22, m23 + o.m23, m30 + o.m30, m31 + o.m31, m32 + o.m32,
				m33 + o.m33);
	}

	@Override
	public Mat4<Float> sub(Mat4<Float> other) {
		Mat4f o = Mat4f.cast(other);
		return new Mat4f(m00 - o.m00, m01 - o.m01, m02 - o.m02, m03 - o.m03, m10 - o.m10, m11 - o.m11, m12 - o.m12,
				m13 - o.m13, m20 - o.m20, m21 - o.m21, m22 - o.m22, m23 - o.m23, m30 - o.m30, m31 - o.m31, m32 - o.m32,
				m33 - o.m33);
	}

	@Override
	public Mat4<Float> mul(Mat4<Float> other) {
		Mat4f o = Mat4f.cast(other);
		return new Mat4f(m00 * o.m00 + m01 * o.m10 + m02 * o.m20 + m03 * o.m30,
				m00 * o.m01 + m01 * o.m11 + m02 * o.m21 + m03 * o.m31,
				m00 * o.m02 + m01 * o.m12 + m02 * o.m22 + m03 * o.m32,
				m00 * o.m03 + m01 * o.m13 + m02 * o.m23 + m03 * o.m33,

				m10 * o.m00 + m11 * o.m10 + m12 * o.m20 + m13 * o.m30,
				m10 * o.m01 + m11 * o.m11 + m12 * o.m21 + m13 * o.m31,
				m10 * o.m02 + m11 * o.m12 + m12 * o.m22 + m13 * o.m32,
				m10 * o.m03 + m11 * o.m13 + m12 * o.m23 + m13 * o.m33,

				m20 * o.m00 + m21 * o.m10 + m22 * o.m20 + m23 * o.m30,
				m20 * o.m01 + m21 * o.m11 + m22 * o.m21 + m23 * o.m31,
				m20 * o.m02 + m21 * o.m12 + m22 * o.m22 + m23 * o.m32,
				m20 * o.m03 + m21 * o.m13 + m22 * o.m23 + m23 * o.m33,

				m30 * o.m00 + m31 * o.m10 + m32 * o.m20 + m33 * o.m30,
				m30 * o.m01 + m31 * o.m11 + m32 * o.m21 + m33 * o.m31,
				m30 * o.m02 + m31 * o.m12 + m32 * o.m22 + m33 * o.m32,
				m30 * o.m03 + m31 * o.m13 + m32 * o.m23 + m33 * o.m33);
	}

	@Override
	public Mat4<Float> transpose() {
		return new Mat4f(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
	}

	@Override
	public Float determinant() {
		// Determinant calculation using Laplace expansion
		float subFactor00 = m22 * m33 - m23 * m32;
		float subFactor01 = m21 * m33 - m23 * m31;
		float subFactor02 = m21 * m32 - m22 * m31;
		float subFactor03 = m20 * m33 - m23 * m30;
		float subFactor04 = m20 * m32 - m22 * m30;
		float subFactor05 = m20 * m31 - m21 * m30;

		return m00 * (m11 * subFactor00 - m12 * subFactor01 + m13 * subFactor02)
				- m01 * (m10 * subFactor00 - m12 * subFactor03 + m13 * subFactor04)
				+ m02 * (m10 * subFactor01 - m11 * subFactor03 + m13 * subFactor05)
				- m03 * (m10 * subFactor02 - m11 * subFactor04 + m12 * subFactor05);
	}

	@Override
	public Mat4<Float> inverse() throws ArithmeticException {
		float det = determinant();
		if (det == 0) {
			throw new ArithmeticException("Matrix is not invertible (determinant is zero).");
		}

		float invDet = 1.0f / det;

		// Cofactors and adjugate (transposed cofactors)
		float t00 = m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31);
		float t01 = -(m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31));
		float t02 = m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31) + m03 * (m11 * m32 - m12 * m31);
		float t03 = -(m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21) + m03 * (m11 * m22 - m12 * m21));

		float t10 = -(m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30));
		float t11 = m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30) + m03 * (m20 * m32 - m22 * m30);
		float t12 = -(m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30) + m03 * (m10 * m32 - m12 * m30));
		float t13 = m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20) + m03 * (m10 * m22 - m12 * m20);

		float t20 = m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30);
		float t21 = -(m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30) + m03 * (m20 * m31 - m21 * m30));
		float t22 = m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30) + m03 * (m10 * m31 - m11 * m30);
		float t23 = -(m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20) + m03 * (m10 * m21 - m11 * m20));

		float t30 = -(m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30));
		float t31 = m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30) + m02 * (m20 * m31 - m21 * m30);
		float t32 = -(m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30) + m02 * (m10 * m31 - m11 * m30));
		float t33 = m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20) + m02 * (m10 * m21 - m11 * m20);

		// Multiply by reciprocal of determinant
		return new Mat4f(t00 * invDet, t01 * invDet, t02 * invDet, t03 * invDet, t10 * invDet, t11 * invDet,
				t12 * invDet, t13 * invDet, t20 * invDet, t21 * invDet, t22 * invDet, t23 * invDet, t30 * invDet,
				t31 * invDet, t32 * invDet, t33 * invDet);
	}

	@Override
	public Mat4<Float> identity() {
		return IDENTITY;
	}

	static Mat4f cast(Mat4<Float> mat4) {
		return (Mat4f) mat4;
	}
}

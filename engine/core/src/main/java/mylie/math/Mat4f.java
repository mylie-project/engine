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
		float s0 = m00 * m11 - m10 * m01;
		float s1 = m00 * m12 - m10 * m02;
		float s2 = m00 * m13 - m10 * m03;
		float s3 = m01 * m12 - m11 * m02;
		float s4 = m01 * m13 - m11 * m03;
		float s5 = m02 * m13 - m12 * m03;

		float c5 = m22 * m33 - m32 * m23;
		float c4 = m21 * m33 - m31 * m23;
		float c3 = m21 * m32 - m31 * m22;
		float c2 = m20 * m33 - m30 * m23;
		float c1 = m20 * m32 - m30 * m22;
		float c0 = m20 * m31 - m30 * m21;

		return (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0);
	}

	@Override
	public Mat4<Float> inverse() throws ArithmeticException {
		// Determinant calculation using Laplace expansion
		float s0 = m00 * m11 - m10 * m01;
		float s1 = m00 * m12 - m10 * m02;
		float s2 = m00 * m13 - m10 * m03;
		float s3 = m01 * m12 - m11 * m02;
		float s4 = m01 * m13 - m11 * m03;
		float s5 = m02 * m13 - m12 * m03;

		float c5 = m22 * m33 - m32 * m23;
		float c4 = m21 * m33 - m31 * m23;
		float c3 = m21 * m32 - m31 * m22;
		float c2 = m20 * m33 - m30 * m23;
		float c1 = m20 * m32 - m30 * m22;
		float c0 = m20 * m31 - m30 * m21;

		float determinant = (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0);
		if (determinant == 0) {
			throw new ArithmeticException("Matrix is singular and cannot be inverted");
		}
		float inverseDeterminant = 1f / determinant;
		return new Mat4f((m11 * c5 - m12 * c4 + m13 * c3) * inverseDeterminant,
				(-m01 * c5 + m02 * c4 - m03 * c3) * inverseDeterminant,
				(m31 * s5 - m32 * s4 + m33 * s3) * inverseDeterminant,
				(-m21 * s5 + m22 * s4 - m23 * s3) * inverseDeterminant,
				(-m10 * c5 + m12 * c2 - m13 * c1) * inverseDeterminant,
				(m00 * c5 - m02 * c2 + m03 * c1) * inverseDeterminant,
				(-m30 * s5 + m32 * s2 - m33 * s1) * inverseDeterminant,
				(m20 * s5 - m22 * s2 + m23 * s1) * inverseDeterminant,
				(m10 * c4 - m11 * c2 + m13 * c0) * inverseDeterminant,
				(-m00 * c4 + m01 * c2 - m03 * c0) * inverseDeterminant,
				(m30 * s4 - m31 * s2 + m33 * s0) * inverseDeterminant,
				(-m20 * s4 + m21 * s2 - m23 * s0) * inverseDeterminant,
				(-m10 * c3 + m11 * c1 - m12 * c0) * inverseDeterminant,
				(m00 * c3 - m01 * c1 + m02 * c0) * inverseDeterminant,
				(-m30 * s3 + m31 * s1 - m32 * s0) * inverseDeterminant,
				(m20 * s3 - m21 * s1 + m22 * s0) * inverseDeterminant);
	}

	@Override
	public Mat4<Float> identity() {
		return IDENTITY;
	}

	static Mat4f cast(Mat4<Float> mat4) {
		return (Mat4f) mat4;
	}
}

package mylie.math;

public record Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13,
		float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
	public static final Matrix4f IDENTITY = new Matrix4f();
	public Matrix4f() {
		this(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1);
	}

	public Matrix4f mul(Matrix4f matrix) {
		return new Matrix4f(m00 * matrix.m00 + m01 * matrix.m10 + m02 * matrix.m20 + m03 * matrix.m30,
				m00 * matrix.m01 + m01 * matrix.m11 + m02 * matrix.m21 + m03 * matrix.m31,
				m00 * matrix.m02 + m01 * matrix.m12 + m02 * matrix.m22 + m03 * matrix.m32,
				m00 * matrix.m03 + m01 * matrix.m13 + m02 * matrix.m23 + m03 * matrix.m33,

				m10 * matrix.m00 + m11 * matrix.m10 + m12 * matrix.m20 + m13 * matrix.m30,
				m10 * matrix.m01 + m11 * matrix.m11 + m12 * matrix.m21 + m13 * matrix.m31,
				m10 * matrix.m02 + m11 * matrix.m12 + m12 * matrix.m22 + m13 * matrix.m32,
				m10 * matrix.m03 + m11 * matrix.m13 + m12 * matrix.m23 + m13 * matrix.m33,

				m20 * matrix.m00 + m21 * matrix.m10 + m22 * matrix.m20 + m23 * matrix.m30,
				m20 * matrix.m01 + m21 * matrix.m11 + m22 * matrix.m21 + m23 * matrix.m31,
				m20 * matrix.m02 + m21 * matrix.m12 + m22 * matrix.m22 + m23 * matrix.m32,
				m20 * matrix.m03 + m21 * matrix.m13 + m22 * matrix.m23 + m23 * matrix.m33,

				m30 * matrix.m00 + m31 * matrix.m10 + m32 * matrix.m20 + m33 * matrix.m30,
				m30 * matrix.m01 + m31 * matrix.m11 + m32 * matrix.m21 + m33 * matrix.m31,
				m30 * matrix.m02 + m31 * matrix.m12 + m32 * matrix.m22 + m33 * matrix.m32,
				m30 * matrix.m03 + m31 * matrix.m13 + m32 * matrix.m23 + m33 * matrix.m33);
	}

	public Matrix4f transpose() {
		return new Matrix4f(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
	}

	public Matrix4f invert() {

		float v = m11 * (m22 * m33 - m23 * m32) - m12 * (m21 * m33 - m23 * m31) + m13 * (m21 * m32 - m22 * m31);
		float v1 = m10 * (m22 * m33 - m23 * m32) - m12 * (m20 * m33 - m23 * m30) + m13 * (m20 * m32 - m22 * m30);
		float v2 = m10 * (m21 * m33 - m23 * m31) - m11 * (m20 * m33 - m23 * m30) + m13 * (m20 * m31 - m21 * m30);
		float v3 = m10 * (m21 * m32 - m22 * m31) - m11 * (m20 * m32 - m22 * m30) + m12 * (m20 * m31 - m21 * m30);
		// Calculate the determinant
		float det = m00 * v - m01 * v1 + m02 * v2 - m03 * v3;

		if (det == 0) {
			throw new ArithmeticException("Matrix is not invertible");
		}

		float invDet = 1.0f / det;

		// Calculate the inverse matrix
		return new Matrix4f(invDet * v, invDet
				* -(m01 * (m22 * m33 - m23 * m32) - m02 * (m21 * m33 - m23 * m31) + m03 * (m21 * m32 - m22 * m31)),
				invDet * (m01 * (m12 * m33 - m13 * m32) - m02 * (m11 * m33 - m13 * m31)
						+ m03 * (m11 * m32 - m12 * m31)),
				invDet * -(m01 * (m12 * m23 - m13 * m22) - m02 * (m11 * m23 - m13 * m21)
						+ m03 * (m11 * m22 - m12 * m21)),

				invDet * -v1,
				invDet * (m00 * (m22 * m33 - m23 * m32) - m02 * (m20 * m33 - m23 * m30)
						+ m03 * (m20 * m32 - m22 * m30)),
				invDet * -(m00 * (m12 * m33 - m13 * m32) - m02 * (m10 * m33 - m13 * m30)
						+ m03 * (m10 * m32 - m12 * m30)),
				invDet * (m00 * (m12 * m23 - m13 * m22) - m02 * (m10 * m23 - m13 * m20)
						+ m03 * (m10 * m22 - m12 * m20)),

				invDet * v2,
				invDet * -(m00 * (m21 * m33 - m23 * m31) - m01 * (m20 * m33 - m23 * m30)
						+ m03 * (m20 * m31 - m21 * m30)),
				invDet * (m00 * (m11 * m33 - m13 * m31) - m01 * (m10 * m33 - m13 * m30)
						+ m03 * (m10 * m31 - m11 * m30)),
				invDet * -(m00 * (m11 * m23 - m13 * m21) - m01 * (m10 * m23 - m13 * m20)
						+ m03 * (m10 * m21 - m11 * m20)),

				invDet * -v3,
				invDet * (m00 * (m21 * m32 - m22 * m31) - m01 * (m20 * m32 - m22 * m30)
						+ m02 * (m20 * m31 - m21 * m30)),
				invDet * -(m00 * (m11 * m32 - m12 * m31) - m01 * (m10 * m32 - m12 * m30)
						+ m02 * (m10 * m31 - m11 * m30)),
				invDet * (m00 * (m11 * m22 - m12 * m21) - m01 * (m10 * m22 - m12 * m20)
						+ m02 * (m10 * m21 - m11 * m20)));
	}

	public Matrix4f translationRotationScale(float tx, float ty, float tz, float qx, float qy, float qz, float qw,
			float sx, float sy, float sz) {
		float dqx = qx + qx;
		float dqy = qy + qy;
		float dqz = qz + qz;
		float q00 = dqx * qx;
		float q11 = dqy * qy;
		float q22 = dqz * qz;
		float q01 = dqx * qy;
		float q02 = dqx * qz;
		float q03 = dqx * qw;
		float q12 = dqy * qz;
		float q13 = dqy * qw;
		float q23 = dqz * qw;
		return new Matrix4f(sx - (q11 + q22) * sx, (q01 + q23) * sx, (q02 - q13) * sx, 0.0f, (q01 - q23) * sy,
				sy - (q22 + q00) * sy, (q12 + q03) * sy, 0.0f, (q02 + q13) * sz, (q12 - q03) * sz,
				sz - (q11 + q00) * sz, 0.0f, tx, ty, tz, 1.0f);
	}

	public Matrix4f translationRotationScale(Vector3f translation, Quaternionf quat, Vector3f scale) {
		return translationRotationScale(translation.x(), translation.y(), translation.z(), quat.x(), quat.y(), quat.z(),
				quat.w(), scale.x(), scale.y(), scale.z());
	}

	public Matrix4f worldMatrix(Vector3f position, Quaternionf rotation, Vector3f scale) {
		return new Matrix4f(scale.x(), 0, 0, position.x(), 0, scale.y(), 0, position.y(), 0, scale.z(), 0, position.z(),
				0, 0, 0, 1);
	}
}

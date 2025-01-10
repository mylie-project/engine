package mylie.math;

public record Quaternionf(float x, float y, float z, float w) {
	public Quaternionf() {
		this(0, 0, 0, 1);
	}

	public Quaternionf mul(Quaternionf q) {
		return new Quaternionf(Math.fma(w, q.x(), Math.fma(x, q.w(), Math.fma(y, q.z(), -z * q.y()))),
				Math.fma(w, q.y(), Math.fma(-x, q.z(), Math.fma(y, q.w(), z * q.x()))),
				Math.fma(w, q.z(), Math.fma(x, q.y(), Math.fma(-y, q.x(), z * q.w()))),
				Math.fma(w, q.w(), Math.fma(-x, q.x(), Math.fma(-y, q.y(), -z * q.z()))));

	}

	public Vector3f transform(Vector3f position) {
		return transform(position.x(), position.y(), position.z());
	}

	public Vector3f transform(float x, float y, float z) {
		float xx = this.x * this.x, yy = this.y * this.y, zz = this.z * this.z, ww = this.w * this.w;
		float xy = this.x * this.y, xz = this.x * this.z, yz = this.y * this.z, xw = this.x * this.w;
		float zw = this.z * this.w, yw = this.y * this.w, k = 1 / (xx + yy + zz + ww);
		return new Vector3f(
				Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2 * (xy - zw) * k, y, (2 * (xz + yw) * k) * z)),
				Math.fma(2 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, (2 * (yz - xw) * k) * z)),
				Math.fma(2 * (xz - yw) * k, x, Math.fma(2 * (yz + xw) * k, y, ((zz - xx - yy + ww) * k) * z)));
	}

	public Quaternionf rotateAxis(float angle, float axisX, float axisY, float axisZ) {
		float hangle = angle / 2.0f;
		float sinAngle = Math.sin(hangle);
		float invVLength = Math.invsqrt(Math.fma(axisX, axisX, Math.fma(axisY, axisY, axisZ * axisZ)));
		float rx = axisX * invVLength * sinAngle;
		float ry = axisY * invVLength * sinAngle;
		float rz = axisZ * invVLength * sinAngle;
		float rw = Math.cosFromSin(sinAngle, hangle);
		return new Quaternionf(Math.fma(this.w, rx, Math.fma(this.x, rw, Math.fma(this.y, rz, -this.z * ry))),
				Math.fma(this.w, ry, Math.fma(-this.x, rz, Math.fma(this.y, rw, this.z * rx))),
				Math.fma(this.w, rz, Math.fma(this.x, ry, Math.fma(-this.y, rx, this.z * rw))),
				Math.fma(this.w, rw, Math.fma(-this.x, rx, Math.fma(-this.y, ry, -this.z * rz))));
	}

	public Quaternionf rotateAxis(float angle, Vector3f axis) {
		return rotateAxis(angle, axis.x(), axis.y(), axis.z());
	}

	public Quaternionf rotationAxis(float angle, float axisX, float axisY, float axisZ) {
		float hangle = angle / 2.0f;
		float sinAngle = Math.sin(hangle);
		float invVLength = Math.invsqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
		return new Quaternionf(axisX * invVLength * sinAngle, axisY * invVLength * sinAngle,
				axisZ * invVLength * sinAngle, Math.cosFromSin(sinAngle, hangle));
	}
}

package mylie.math;

record Quaternionf(float x, float y, float z, float w) implements Quaternion<Float> {

	@Override
	public Quaternion<Float> add(Quaternion<Float> other) {
		Quaternionf o = (Quaternionf) other;
		return new Quaternionf(x + o.x, y + o.y, z + o.z, w + o.w);
	}

	@Override
	public Quaternion<Float> div(Quaternion<Float> other) {
		Quaternionf b = (Quaternionf) other;
		float norm = Math.fma(b.x(), b.x(), Math.fma(b.y(), b.y(), Math.fma(b.z(), b.z(), b.w() * b.w())));
		if (norm == 0)
			throw new ArithmeticException("Division by zero");
		float invNorm = 1.0f / norm;
		float x = -b.x() * invNorm;
		float y = -b.y() * invNorm;
		float z = -b.z() * invNorm;
		float w = b.w() * invNorm;
		return Quaternion.f(Math.fma(this.w, x, Math.fma(this.x, w, Math.fma(this.y, z, -this.z * y))),
				Math.fma(this.w, y, Math.fma(-this.x, z, Math.fma(this.y, w, this.z * x))),
				Math.fma(this.w, z, Math.fma(this.x, y, Math.fma(-this.y, x, this.z * w))),
				Math.fma(this.w, w, Math.fma(-this.x, x, Math.fma(-this.y, y, -this.z * z))));
	}

	@Override
	public Quaternion<Float> mul(Quaternion<Float> other) {
		Quaternionf q = (Quaternionf) other;
		return new Quaternionf(Math.fma(w, q.x(), Math.fma(x, q.w(), Math.fma(y, q.z(), -z * q.y()))),
				Math.fma(w, q.y(), Math.fma(-x, q.z(), Math.fma(y, q.w(), z * q.x()))),
				Math.fma(w, q.z(), Math.fma(x, q.y(), Math.fma(-y, q.x(), z * q.w()))),
				Math.fma(w, q.w(), Math.fma(-x, q.x(), Math.fma(-y, q.y(), -z * q.z()))));
	}

	@Override
	public Quaternion<Float> sub(Quaternion<Float> other) {
		Quaternionf o = (Quaternionf) other;
		return new Quaternionf(x - o.x, y - o.y, z - o.z, w - o.w);
	}

	@Override
	public float dot(Quaternion<Float> other) {
		Quaternionf o = (Quaternionf) other;
		return x * o.x + y * o.y + z * o.z + w * o.w;
	}

	@Override
	public Quaternion<Float> conjugate() {
		return new Quaternionf(-x, -y, -z, w);
	}

	@Override
	public Quaternion<Float> inverse() {
		float normSquared = x * x + y * y + z * z + w * w;
		if (normSquared == 0)
			throw new ArithmeticException("Cannot invert a zero quaternion");
		return new Quaternionf(-x / normSquared, -y / normSquared, -z / normSquared, w / normSquared);
	}

	@Override
	public Quaternion<Float> normalize() {
		float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
		if (length == 0)
			throw new ArithmeticException("Cannot normalize a zero quaternion");
		return new Quaternionf(x / length, y / length, z / length, w / length);
	}

	@Override
	public Quaternion<Float> difference(Quaternion<Float> other) {
		return this.mul(other.inverse());
	}

	@Override
	public Vec3<Float> transform(Vec3<Float> vector) {
		Vec3f vec = Vec3f.cast(vector);
		float x = vec.x(), y = vec.y(), z = vec.z();
		float xx = this.x * this.x;
		float yy = this.y * this.y;
		float zz = this.z * this.z;
		float ww = this.w * this.w;
		float xy = this.x * this.y;
		float xz = this.x * this.z;
		float yz = this.y * this.z;
		float xw = this.x * this.w;
		float zw = this.z * this.w;
		float yw = this.y * this.w;
		float k = 1 / (xx + yy + zz + ww);
		return new Vec3f(Math.fma((xx - yy - zz + ww) * k, x, Math.fma(2 * (xy - zw) * k, y, (2 * (xz + yw) * k) * z)),
				Math.fma(2 * (xy + zw) * k, x, Math.fma((yy - xx - zz + ww) * k, y, (2 * (yz - xw) * k) * z)),
				Math.fma(2 * (xz - yw) * k, x, Math.fma(2 * (yz + xw) * k, y, ((zz - xx - yy + ww) * k) * z)));
	}

	@Override
	public Quaternion<Float> rotateAxis(float angle, Vec3<Float> axis) {
		Vec3f axisV = Vec3f.cast(axis);
		float axisX = axisV.x(), axisY = axisV.y(), axisZ = axisV.z();
		float hangle = angle / 2.0f;
		float sinAngle = FastMath.sin(hangle);
		float invVLength = FastMath.invsqrt(Math.fma(axisX, axisX, Math.fma(axisY, axisY, axisZ * axisZ)));
		float rx = axisX * invVLength * sinAngle;
		float ry = axisY * invVLength * sinAngle;
		float rz = axisZ * invVLength * sinAngle;
		float rw = FastMath.cosFromSin(hangle);
		return Quaternion.f(Math.fma(this.w, rx, Math.fma(this.x, rw, Math.fma(this.y, rz, -this.z * ry))),
				Math.fma(this.w, ry, Math.fma(-this.x, rz, Math.fma(this.y, rw, this.z * rx))),
				Math.fma(this.w, rz, Math.fma(this.x, ry, Math.fma(-this.y, rx, this.z * rw))),
				Math.fma(this.w, rw, Math.fma(-this.x, rx, Math.fma(-this.y, ry, -this.z * rz))));
	}

	@Override
	public Quaternion<Float> rotationAxis(float angle, Vec3<Float> axis) {
		Vec3f axisV = Vec3f.cast(axis);
		float axisX = axisV.x();
		float axisY = axisV.y();
		float axisZ = axisV.z();
		float hangle = angle / 2.0f;
		float sinAngle = FastMath.sin(hangle);
		float invVLength = FastMath.invsqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
		return Quaternion.f(axisX * invVLength * sinAngle, axisY * invVLength * sinAngle, axisZ * invVLength * sinAngle,
				FastMath.cosFromSin(hangle));
	}
}

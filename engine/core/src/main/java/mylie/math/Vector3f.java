package mylie.math;

@SuppressWarnings("unused")
public record Vector3f(float x, float y, float z) {
	public Vector3f(float value) {
		this(value, value, value);
	}

	public Vector3f add(float x, float y, float z) {
		return new Vector3f(this.x + x, this.y + y, this.z + z);
	}

	public Vector3f add(Vector3f vector) {
		return add(vector.x, vector.y, vector.z);
	}

	public Vector3f sub(float x, float y, float z) {
		return new Vector3f(this.x - x, this.y - y, this.z - z);
	}

	public Vector3f sub(Vector3f vector) {
		return sub(vector.x, vector.y, vector.z);
	}

	public Vector3f mul(float x, float y, float z) {
		return new Vector3f(this.x * x, this.y * y, this.z * z);
	}

	public Vector3f mul(Vector3f vector) {
		return mul(vector.x, vector.y, vector.z);
	}

	public Vector3f mul(float scalar) {
		return mul(scalar, scalar, scalar);
	}

	public Vector3f div(float x, float y, float z) {
		return new Vector3f(this.x / x, this.y / y, this.z / z);
	}

	public Vector3f div(Vector3f vector) {
		return div(vector.x, vector.y, vector.z);
	}

	public Vector3f div(float scalar) {
		return div(scalar, scalar, scalar);
	}

	public Vector3f normalize() {
		if (x == 0 && y == 0 && z == 0) {
			throw new ArithmeticException("Cannot normalize the zero vector");
		}
		float length = Math.sqrt(x * x + y * y + z * z);
		return new Vector3f(x / length, y / length, z / length);
	}

	public float length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public float dot(Vector3f vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	public float angle(Vector3f vector) {
		return Math.acos(dot(vector) / (length() * vector.length()));
	}

	public Vector3f negate() {
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}

	public Vector3f clamp(float min, float max) {
		return new Vector3f(Math.max(min, Math.min(max, x)), Math.max(min, Math.min(max, y)),
				Math.max(min, Math.min(max, z)));
	}

	public Vector3f clamp(Vector3f min, Vector3f max) {
		return new Vector3f(Math.max(min.x, Math.min(max.x, x)), Math.max(min.y, Math.min(max.y, y)),
				Math.max(min.z, Math.min(max.z, z)));
	}

	public Vector3f lerp(float x, float y, float z, float alpha) {
		return new Vector3f(this.x + alpha * (x - this.x), this.y + alpha * (y - this.y),
				this.z + alpha * (z - this.z));
	}

	public Vector3f lerp(Vector3f vector, float alpha) {
		return lerp(vector.x, vector.y, vector.z, alpha);
	}

	public Vector3f cross(Vector3f vector) {
		return new Vector3f(y * vector.z - z * vector.y, z * vector.x - x * vector.z, x * vector.y - y * vector.x);
	}

	public float lengthSquared() {
		return x * x + y * y + z * z;
	}

	public float distanceSquared(Vector3f vector) {
		return Math.fma(x - vector.x, x - vector.x, Math.fma(y - vector.y, y - vector.y, z - vector.z));
	}

	public float distance(Vector3f vector) {
		return Math.sqrt(distanceSquared(vector));
	}

	public Vector3f min(Vector3f vector) {
		return new Vector3f(Math.min(x, vector.x), Math.min(y, vector.y), Math.min(z, vector.z));
	}

	public Vector3f max(Vector3f vector) {
		return new Vector3f(Math.max(x, vector.x), Math.max(y, vector.y), Math.max(z, vector.z));
	}
}

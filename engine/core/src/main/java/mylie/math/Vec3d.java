package mylie.math;

@SuppressWarnings({"unused"})
record Vec3d(double x, double y, double z) implements Vec3<Double> {
	private static final Vec3d ZERO = new Vec3d(0, 0, 0);
	private static final Vec3d ONE = new Vec3d(1, 1, 1);
	private static final Vec3d UNIT_X = new Vec3d(1, 0, 0);
	private static final Vec3d UNIT_Y = new Vec3d(0, 1, 0);
	private static final Vec3d UNIT_Z = new Vec3d(0, 0, 1);
	private static final Vec3d NEGATIVE_ONE = new Vec3d(-1, -1, -1);
	private static final Vec3d NEGATIVE_UNIT_X = new Vec3d(-1, 0, 0);
	private static final Vec3d NEGATIVE_UNIT_Y = new Vec3d(0, -1, 0);
	private static final Vec3d NEGATIVE_UNIT_Z = new Vec3d(0, 0, -1);
	@Override
	public Vec3<Double> add(Vec3<Double> other) {
		Vec3d b = cast(other);
		return Vec3.of(x + b.x, y + b.y, z + b.z);
	}

	@Override
	public Vec3<Double> sub(Vec3<Double> other) {
		Vec3d b = cast(other);
		return Vec3.of(x - b.x, y - b.y, z - b.z);
	}

	@Override
	public Vec3<Double> mul(Vec3<Double> other) {
		Vec3d b = cast(other);
		return Vec3.of(x * b.x, y * b.y, z * b.z);
	}

	@Override
	public Vec3<Double> div(Vec3<Double> other) {
		Vec3d b = cast(other);
		if (b.x == 0 || b.y == 0 || b.z == 0) {
			throw new ArithmeticException("Division by Zero");
		}
		return Vec3.of(x / b.x, y / b.y, z / b.z);
	}

	@Override
	public Vec3<Double> mulAdd(Vec3<Double> other, Vec3<Double> factor) {
		Vec3d b = cast(other);
		Vec3d c = cast(factor);
		return Vec3.of(x + b.x * c.x, y + b.y * c.y, z + b.z * c.z);
	}

	@Override
	public Vec3<Double> negate() {
		return Vec3.of(-x, -y, -z);
	}

	@Override
	public Vec3<Double> normalize() {
		float length = (float) Math.sqrt(x * x + y * y + z * z);
		if (length == 0) {
			throw new ArithmeticException("Cannot normalize a zero-length vector.");
		}
		return Vec3.of(x / length, y / length, z / length);
	}

	@Override
	public Vec3<Double> max(Vec3<Double> other) {
		Vec3d b = cast(other);
		return Vec3.of(Math.max(x, b.x), Math.max(y, b.y), Math.max(z, b.z));
	}

	@Override
	public Vec3<Double> min(Vec3<Double> other) {
		Vec3d b = cast(other);
		return Vec3.of(Math.min(x, b.x), Math.min(y, b.y), Math.min(z, b.z));
	}

	@Override
	public Double getX() {
		return x;
	}

	@Override
	public Double getY() {
		return y;
	}

	@Override
	public Double getZ() {
		return z;
	}

	@Override
	public Double dot(Vec3<Double> other) {
		Vec3d b = cast(other);
		return (x * b.x + y * b.y + z * b.z);
	}

	@Override
	public Vec3<Double> cross(Vec3<Double> other) {
		Vec3d b = cast(other);
		return Vec3.of(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x);
	}

	@Override
	public Vec2<Double> swizzle(Component x, Component y) {
		return Vec2.of(componentValue(x), componentValue(y));
	}

	@Override
	public Vec3<Double> swizzle(Component x, Component y, Component z) {
		return Vec3.of(componentValue(x), componentValue(y), componentValue(z));
	}

	@Override
	public Vec3<Double> zero() {
		return ZERO;
	}

	@Override
	public Vec3<Double> one() {
		return ONE;
	}

	@Override
	public Vec3<Double> negativeOne() {
		return NEGATIVE_ONE;
	}

	@Override
	public Vec3<Double> unitX() {
		return UNIT_X;
	}

	@Override
	public Vec3<Double> unitY() {
		return UNIT_Y;
	}

	@Override
	public Vec3<Double> unitZ() {
		return UNIT_Z;
	}

	@Override
	public Vec3<Double> negativeUnitX() {
		return NEGATIVE_UNIT_X;
	}

	@Override
	public Vec3<Double> negativeUnitY() {
		return NEGATIVE_UNIT_Y;
	}

	@Override
	public Vec3<Double> negativeUnitZ() {
		return NEGATIVE_UNIT_Z;
	}

	private double componentValue(Component c) {
		if (c == X)
			return x;
		if (c == Y)
			return y;
		if (c == Z)
			return z;
		throw new IllegalArgumentException("Component must be X, Y or Z");
	}

	Vec3d cast(Vec3<Double> other) {
		return (Vec3d) other;
	}
}

package mylie.math;

@SuppressWarnings("unused")
record Vec2d(double x, double y) implements Vec2<Double> {
	private static final Vec2d ZERO = new Vec2d(0, 0);
	private static final Vec2d ONE = new Vec2d(1, 1);
	private static final Vec2d UNIT_X = new Vec2d(1, 0);
	private static final Vec2d UNIT_Y = new Vec2d(0, 1);
	private static final Vec2d NEGATIVE_ONE = new Vec2d(-1, -1);
	private static final Vec2d NEGATIVE_UNIT_X = new Vec2d(-1, 0);
	private static final Vec2d NEGATIVE_UNIT_Y = new Vec2d(0, -1);

	@Override
	public Vec2<Double> add(Vec2<Double> other) {
		Vec2d b = cast(other);
		return Vec2.of(x + b.x, y + b.y);
	}

	@Override
	public Vec2<Double> sub(Vec2<Double> other) {
		Vec2d b = cast(other);
		return Vec2.of(x - b.x, y - b.y);
	}

	@Override
	public Vec2<Double> mul(Vec2<Double> other) {
		Vec2d b = cast(other);
		return Vec2.of(x * b.x, y * b.y);
	}

	@Override
	public Vec2<Double> div(Vec2<Double> other) {
		Vec2d b = cast(other);
		if (b.x == 0 || b.y == 0) {
			throw new ArithmeticException("Division by Zero");
		}
		return Vec2.of(x / b.x, y / b.y);
	}

	@Override
	public Vec2<Double> mulAdd(Vec2<Double> other, Vec2<Double> factor) {
		Vec2d b = cast(other);
		Vec2d c = cast(factor);
		return Vec2.of(x + b.x * c.x, y + b.y * c.y);
	}

	@Override
	public Vec2<Double> negate() {
		return Vec2.of(-x, -y);
	}

	@Override
	public Vec2<Double> normalize() {
		float magnitude = (float) Math.sqrt(x * x + y * y);
		if (magnitude == 0) {
			throw new ArithmeticException("Cannot normalize a zero-length vector.");
		}
		return Vec2.of(x / magnitude, y / magnitude);
	}

	@Override
	public Vec2<Double> max(Vec2<Double> other) {
		Vec2d b = cast(other);
		return Vec2.of(Math.max(x, b.x), Math.max(y, b.y));
	}

	@Override
	public Vec2<Double> min(Vec2<Double> other) {
		Vec2d b = cast(other);
		return Vec2.of(Math.min(x, b.x), Math.min(y, b.y));
	}

	Vec2d cast(Vec2<Double> other) {
		return (Vec2d) other;
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
	public Vec2<Double> swizzle(Component x, Component y) {
		return Vec2.of(x == Vec2.X ? x() : y(), y == Vec2.X ? x() : y());
	}

	@Override
	public Vec2<Double> unitX() {
		return UNIT_X;
	}

	@Override
	public Vec2<Double> unitY() {
		return UNIT_Y;
	}

	@Override
	public Vec2<Double> zero() {
		return ZERO;
	}

	@Override
	public Vec2<Double> one() {
		return ONE;
	}

	@Override
	public Vec2<Double> negativeOne() {
		return NEGATIVE_ONE;
	}

	@Override
	public Vec2<Double> negativeUnitX() {
		return NEGATIVE_UNIT_X;
	}

	@Override
	public Vec2<Double> negativeUnitY() {
		return NEGATIVE_UNIT_Y;
	}
}

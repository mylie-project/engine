package mylie.math;

@SuppressWarnings("unused")
record Vec2f(float x, float y) implements Vec2<Float> {
	private static final Vec2f ZERO = new Vec2f(0, 0);
	private static final Vec2f ONE = new Vec2f(1, 1);
	private static final Vec2f UNIT_X = new Vec2f(1, 0);
	private static final Vec2f UNIT_Y = new Vec2f(0, 1);
	private static final Vec2f NEGATIVE_ONE = new Vec2f(-1, -1);
	private static final Vec2f NEGATIVE_UNIT_X = new Vec2f(-1, 0);
	private static final Vec2f NEGATIVE_UNIT_Y = new Vec2f(0, -1);

	@Override
	public Vec2<Float> add(Vec2<Float> other) {
		Vec2f b = cast(other);
		return Vec2.of(x + b.x, y + b.y);
	}

	@Override
	public Vec2<Float> sub(Vec2<Float> other) {
		Vec2f b = cast(other);
		return Vec2.of(x - b.x, y - b.y);
	}

	@Override
	public Vec2<Float> mul(Vec2<Float> other) {
		Vec2f b = cast(other);
		return Vec2.of(x * b.x, y * b.y);
	}

	@Override
	public Vec2<Float> div(Vec2<Float> other) {
		Vec2f b = cast(other);
		if (b.x == 0 || b.y == 0) {
			throw new ArithmeticException("Division by Zero");
		}
		return Vec2.of(x / b.x, y / b.y);
	}

	@Override
	public Vec2<Float> mulAdd(Vec2<Float> other, Vec2<Float> factor) {
		Vec2f b = cast(other);
		Vec2f c = cast(factor);
		return Vec2.of(x + b.x * c.x, y + b.y * c.y);
	}

	@Override
	public Vec2<Float> negate() {
		return Vec2.of(-x, -y);
	}

	@Override
	public Vec2<Float> normalize() {
		float magnitude = (float) Math.sqrt(x * x + y * y);
		if (magnitude == 0) {
			throw new ArithmeticException("Cannot normalize a zero-length vector.");
		}
		return Vec2.of(x / magnitude, y / magnitude);
	}

	@Override
	public Vec2<Float> max(Vec2<Float> other) {
		Vec2f b = cast(other);
		return Vec2.of(Math.max(x, b.x), Math.max(y, b.y));
	}

	@Override
	public Vec2<Float> min(Vec2<Float> other) {
		Vec2f b = cast(other);
		return Vec2.of(Math.min(x, b.x), Math.min(y, b.y));
	}

	Vec2f cast(Vec2<Float> other) {
		return (Vec2f) other;
	}

	@Override
	public Float getX() {
		return x;
	}

	@Override
	public Float getY() {
		return y;
	}

	@Override
	public Vec2<Float> swizzle(Component x, Component y) {
		return Vec2.of(x == Vec2.X ? x() : y(), y == Vec2.X ? x() : y());
	}

	@Override
	public Vec2<Float> unitX() {
		return UNIT_X;
	}

	@Override
	public Vec2<Float> unitY() {
		return UNIT_Y;
	}

	@Override
	public Vec2<Float> zero() {
		return ZERO;
	}

	@Override
	public Vec2<Float> one() {
		return ONE;
	}

	@Override
	public Vec2<Float> negativeOne() {
		return NEGATIVE_ONE;
	}

	@Override
	public Vec2<Float> negativeUnitX() {
		return NEGATIVE_UNIT_X;
	}

	@Override
	public Vec2<Float> negativeUnitY() {
		return NEGATIVE_UNIT_Y;
	}
}

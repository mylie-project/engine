package mylie.math;

@SuppressWarnings("unused")
public class Math {
	public static final double PI = java.lang.Math.PI;
	public static final double PI_TIMES_2 = PI * 2.0;
	public static final float PI_f = (float) java.lang.Math.PI;
	public static final float PI_TIMES_2_f = PI_f * 2.0f;
	public static final double PI_OVER_2 = PI * 0.5;
	public static final float PI_OVER_2_f = (float) (PI * 0.5);
	public static final double PI_OVER_4 = PI * 0.25;
	public static final float PI_OVER_4_f = (float) (PI * 0.25);
	public static final double ONE_OVER_PI = 1.0 / PI;
	public static final float ONE_OVER_PI_f = (float) (1.0 / PI);

	private static final double c1 = Double.longBitsToDouble(-4628199217061079772L);
	private static final double c2 = Double.longBitsToDouble(4575957461383582011L);
	private static final double c3 = Double.longBitsToDouble(-4671919876300759001L);
	private static final double c4 = Double.longBitsToDouble(4523617214285661942L);
	private static final double c5 = Double.longBitsToDouble(-4730215272828025532L);
	private static final double c6 = Double.longBitsToDouble(4460272573143870633L);
	private static final double c7 = Double.longBitsToDouble(-4797767418267846529L);

	/**
	 * @author theagentd
	 */
	static double sin_theagentd_arith(double x) {
		double xi = floor((x + PI_OVER_4) * ONE_OVER_PI);
		double x_ = x - xi * PI;
		double sign = ((int) xi & 1) * -2 + 1D;
		double x2 = x_ * x_;
		double sin = x_;
		double tx = x_ * x2;
		sin += tx * c1;
		tx *= x2;
		sin += tx * c2;
		tx *= x2;
		sin += tx * c3;
		tx *= x2;
		sin += tx * c4;
		tx *= x2;
		sin += tx * c5;
		tx *= x2;
		sin += tx * c6;
		tx *= x2;
		sin += tx * c7;
		return sign * sin;
	}

	/**
	 * Reference: <a href=
	 * "http://www.java-gaming.org/topics/joml-1-8-0-release/37491/msg/361718/view.html#msg361718">http://www.java-gaming.org/</a>
	 */
	static double sin_roquen_arith(double x) {
		double xi = Math.floor((x + PI_OVER_4) * ONE_OVER_PI);
		double x_ = x - xi * PI;
		double sign = ((int) xi & 1) * -2 + 1D;
		double x2 = x_ * x_;

		double sin;
		x_ = sign * x_;
		sin = c7;
		sin = sin * x2 + c6;
		sin = sin * x2 + c5;
		sin = sin * x2 + c4;
		sin = sin * x2 + c3;
		sin = sin * x2 + c2;
		sin = sin * x2 + c1;
		return x_ + x_ * x2 * sin;
	}

	private static final double s5 = Double.longBitsToDouble(4523227044276562163L);
	private static final double s4 = Double.longBitsToDouble(-4671934770969572232L);
	private static final double s3 = Double.longBitsToDouble(4575957211482072852L);
	private static final double s2 = Double.longBitsToDouble(-4628199223918090387L);
	private static final double s1 = Double.longBitsToDouble(4607182418589157889L);

	/**
	 * Reference: <a href=
	 * "http://www.java-gaming.org/topics/joml-1-8-0-release/37491/msg/361815/view.html#msg361815">http://www.java-gaming.org/</a>
	 */
	static double sin_roquen_9(double v) {
		double i = java.lang.Math.rint(v * ONE_OVER_PI);
		double x = v - i * Math.PI;
		double qs = 1D - 2 * ((int) i & 1);
		double x2 = x * x;
		double r;
		x = qs * x;
		r = s5;
		r = r * x2 + s4;
		r = r * x2 + s3;
		r = r * x2 + s2;
		r = r * x2 + s1;
		return x * r;
	}

	private static final double k1 = Double.longBitsToDouble(-4628199217061079959L);
	private static final double k2 = Double.longBitsToDouble(4575957461383549981L);
	private static final double k3 = Double.longBitsToDouble(-4671919876307284301L);
	private static final double k4 = Double.longBitsToDouble(4523617213632129738L);
	private static final double k5 = Double.longBitsToDouble(-4730215344060517252L);
	private static final double k6 = Double.longBitsToDouble(4460268259291226124L);
	private static final double k7 = Double.longBitsToDouble(-4798040743777455072L);

	/**
	 * Reference: <a href=
	 * "http://www.java-gaming.org/topics/joml-1-8-0-release/37491/msg/361815/view.html#msg361815">http://www.java-gaming.org/</a>
	 */
	static double sin_roquen_newk(double v) {
		double i = java.lang.Math.rint(v * ONE_OVER_PI);
		double x = v - i * Math.PI;
		double qs = 1D - 2 * ((int) i & 1);
		double x2 = x * x;
		double r;
		x = qs * x;
		r = k7;
		r = r * x2 + k6;
		r = r * x2 + k5;
		r = r * x2 + k4;
		r = r * x2 + k3;
		r = r * x2 + k2;
		r = r * x2 + k1;
		return x + x * x2 * r;
	}

	public static float sin(float rad) {
		return (float) java.lang.Math.sin(rad);
	}
	public static double sin(double rad) {
		return java.lang.Math.sin(rad);
	}

	public static float cos(float rad) {
		return (float) java.lang.Math.cos(rad);
	}
	public static double cos(double rad) {
		return java.lang.Math.cos(rad);
	}

	public static float cosFromSin(float sin, float angle) {
		return cosFromSinInternal(sin, angle);
	}
	private static float cosFromSinInternal(float sin, float angle) {
		// sin(x)^2 + cos(x)^2 = 1
		float cos = sqrt(1.0f - sin * sin);
		float a = angle + PI_OVER_2_f;
		float b = a - (int) (a / PI_TIMES_2_f) * PI_TIMES_2_f;
		if (b < 0.0)
			b = PI_TIMES_2_f + b;
		if (b >= PI_f)
			return -cos;
		return cos;
	}
	public static double cosFromSin(double sin, double angle) {
		// sin(x)^2 + cos(x)^2 = 1
		double cos = sqrt(1.0 - sin * sin);
		double a = angle + PI_OVER_2;
		double b = a - (int) (a / PI_TIMES_2) * PI_TIMES_2;
		if (b < 0.0)
			b = PI_TIMES_2 + b;
		if (b >= PI)
			return -cos;
		return cos;
	}

	/* Other math functions not yet approximated */

	public static float sqrt(float r) {
		return (float) java.lang.Math.sqrt(r);
	}
	public static double sqrt(double r) {
		return java.lang.Math.sqrt(r);
	}

	public static float invsqrt(float r) {
		return 1.0f / (float) java.lang.Math.sqrt(r);
	}
	public static double invsqrt(double r) {
		return 1.0 / java.lang.Math.sqrt(r);
	}

	public static float tan(float r) {
		return (float) java.lang.Math.tan(r);
	}
	public static double tan(double r) {
		return java.lang.Math.tan(r);
	}

	public static float acos(float r) {
		return (float) java.lang.Math.acos(r);
	}
	public static double acos(double r) {
		return java.lang.Math.acos(r);
	}

	public static float safeAcos(float v) {
		if (v < -1.0f)
			return Math.PI_f;
		else if (v > 1.0f)
			return 0.0f;
		else
			return acos(v);
	}
	public static double safeAcos(double v) {
		if (v < -1.0)
			return Math.PI;
		else if (v > 1.0)
			return 0.0;
		else
			return acos(v);
	}

	/**
	 * https://math.stackexchange.com/questions/1098487/atan2-faster-approximation/1105038#answer-1105038
	 */
	private static double fastAtan2(double y, double x) {
		double ax = x >= 0.0 ? x : -x, ay = y >= 0.0 ? y : -y;
		double a = ay > ax ? ax / ay : ay / ax;
		double s = a * a;
		double r = fma(fma(fma(-0.0464964749, s, 0.15931422), s, -0.327622764) * s, a, a);
		if (ay > ax)
			r = PI_OVER_2 - r;
		if (x < 0.0)
			r = PI - r;
		return y >= 0 ? r : -r;
	}

	public static float atan2(float y, float x) {
		return (float) java.lang.Math.atan2(y, x);
	}
	public static double atan2(double y, double x) {
		return java.lang.Math.atan2(y, x);
	}

	public static float asin(float r) {
		return (float) java.lang.Math.asin(r);
	}
	public static double asin(double r) {
		return java.lang.Math.asin(r);
	}
	public static float safeAsin(float r) {
		return r <= -1.0f ? -PI_OVER_2_f : r >= 1.0f ? PI_OVER_2_f : asin(r);
	}
	public static double safeAsin(double r) {
		return r <= -1.0 ? -PI_OVER_2 : r >= 1.0 ? PI_OVER_2 : asin(r);
	}

	public static float abs(float r) {
		return java.lang.Math.abs(r);
	}
	public static double abs(double r) {
		return java.lang.Math.abs(r);
	}

	static boolean absEqualsOne(float r) {
		return (Float.floatToRawIntBits(r) & 0x7FFFFFFF) == 0x3F800000;
	}
	static boolean absEqualsOne(double r) {
		return (Double.doubleToRawLongBits(r) & 0x7FFFFFFFFFFFFFFFL) == 0x3FF0000000000000L;
	}

	public static int abs(int r) {
		return java.lang.Math.abs(r);
	}
	public static long abs(long r) {
		return java.lang.Math.abs(r);
	}

	public static int max(int x, int y) {
		return java.lang.Math.max(x, y);
	}
	public static int min(int x, int y) {
		return java.lang.Math.min(x, y);
	}

	public static long max(long x, long y) {
		return java.lang.Math.max(x, y);
	}
	public static long min(long x, long y) {
		return java.lang.Math.min(x, y);
	}

	public static double min(double a, double b) {
		return a < b ? a : b;
	}
	public static float min(float a, float b) {
		return a < b ? a : b;
	}

	public static float max(float a, float b) {
		return a > b ? a : b;
	}
	public static double max(double a, double b) {
		return a > b ? a : b;
	}

	public static float clamp(float a, float b, float val) {
		return max(a, min(b, val));
	}
	public static double clamp(double a, double b, double val) {
		return max(a, min(b, val));
	}
	public static int clamp(int a, int b, int val) {
		return max(a, min(b, val));
	}
	public static long clamp(long a, long b, long val) {
		return max(a, min(b, val));
	}

	public static float toRadians(float angles) {
		return (float) java.lang.Math.toRadians(angles);
	}
	public static double toRadians(double angles) {
		return java.lang.Math.toRadians(angles);
	}

	public static float toDegrees(float angles) {
		return (float) java.lang.Math.toDegrees(angles);
	}
	public static double toDegrees(double angles) {
		return java.lang.Math.toDegrees(angles);
	}

	public static double floor(double v) {
		return java.lang.Math.floor(v);
	}

	public static float floor(float v) {
		return (float) java.lang.Math.floor(v);
	}

	public static double ceil(double v) {
		return java.lang.Math.ceil(v);
	}

	public static float ceil(float v) {
		return (float) java.lang.Math.ceil(v);
	}

	public static long round(double v) {
		return java.lang.Math.round(v);
	}

	public static int round(float v) {
		return java.lang.Math.round(v);
	}

	public static double exp(double a) {
		return java.lang.Math.exp(a);
	}

	public static boolean isFinite(double d) {
		return abs(d) <= Double.MAX_VALUE;
	}

	public static boolean isFinite(float f) {
		return abs(f) <= Float.MAX_VALUE;
	}

	public static float fma(float a, float b, float c) {
		return a * b + c;
	}

	public static double fma(double a, double b, double c) {
		return a * b + c;
	}

	public static int roundUsing(float v, int mode) {
		return switch (mode) {
			case RoundingMode.TRUNCATE -> (int) v;
			case RoundingMode.CEILING -> (int) java.lang.Math.ceil(v);
			case RoundingMode.FLOOR -> (int) java.lang.Math.floor(v);
			case RoundingMode.HALF_DOWN -> roundHalfDown(v);
			case RoundingMode.HALF_UP -> roundHalfUp(v);
			case RoundingMode.HALF_EVEN -> roundHalfEven(v);
			default -> throw new UnsupportedOperationException();
		};
	}
	public static int roundUsing(double v, int mode) {
		return switch (mode) {
			case RoundingMode.TRUNCATE -> (int) v;
			case RoundingMode.CEILING -> (int) java.lang.Math.ceil(v);
			case RoundingMode.FLOOR -> (int) java.lang.Math.floor(v);
			case RoundingMode.HALF_DOWN -> roundHalfDown(v);
			case RoundingMode.HALF_UP -> roundHalfUp(v);
			case RoundingMode.HALF_EVEN -> roundHalfEven(v);
			default -> throw new UnsupportedOperationException();
		};
	}
	public static long roundLongUsing(double v, int mode) {
		return switch (mode) {
			case RoundingMode.TRUNCATE -> (long) v;
			case RoundingMode.CEILING -> (long) java.lang.Math.ceil(v);
			case RoundingMode.FLOOR -> (long) java.lang.Math.floor(v);
			case RoundingMode.HALF_DOWN -> roundHalfDown(v);
			case RoundingMode.HALF_UP -> roundHalfUp(v);
			case RoundingMode.HALF_EVEN -> roundHalfEven(v);
			default -> throw new UnsupportedOperationException();
		};
	}

	public static float lerp(float a, float b, float t) {
		return Math.fma(b - a, t, a);
	}
	public static double lerp(double a, double b, double t) {
		return Math.fma(b - a, t, a);
	}

	public static float biLerp(float q00, float q10, float q01, float q11, float tx, float ty) {
		float lerpX1 = lerp(q00, q10, tx);
		float lerpX2 = lerp(q01, q11, tx);
		return lerp(lerpX1, lerpX2, ty);
	}

	public static double biLerp(double q00, double q10, double q01, double q11, double tx, double ty) {
		double lerpX1 = lerp(q00, q10, tx);
		double lerpX2 = lerp(q01, q11, tx);
		return lerp(lerpX1, lerpX2, ty);
	}

	public static float triLerp(float q000, float q100, float q010, float q110, float q001, float q101, float q011,
			float q111, float tx, float ty, float tz) {
		float x00 = lerp(q000, q100, tx);
		float x10 = lerp(q010, q110, tx);
		float x01 = lerp(q001, q101, tx);
		float x11 = lerp(q011, q111, tx);
		float y0 = lerp(x00, x10, ty);
		float y1 = lerp(x01, x11, ty);
		return lerp(y0, y1, tz);
	}

	public static double triLerp(double q000, double q100, double q010, double q110, double q001, double q101,
			double q011, double q111, double tx, double ty, double tz) {
		double x00 = lerp(q000, q100, tx);
		double x10 = lerp(q010, q110, tx);
		double x01 = lerp(q001, q101, tx);
		double x11 = lerp(q011, q111, tx);
		double y0 = lerp(x00, x10, ty);
		double y1 = lerp(x01, x11, ty);
		return lerp(y0, y1, tz);
	}

	public static int roundHalfEven(float v) {
		return (int) java.lang.Math.rint(v);
	}
	public static int roundHalfDown(float v) {
		return (v > 0) ? (int) java.lang.Math.ceil(v - 0.5d) : (int) java.lang.Math.floor(v + 0.5d);
	}
	public static int roundHalfUp(float v) {
		return (v > 0) ? (int) java.lang.Math.floor(v + 0.5d) : (int) java.lang.Math.ceil(v - 0.5d);
	}

	public static int roundHalfEven(double v) {
		return (int) java.lang.Math.rint(v);
	}
	public static int roundHalfDown(double v) {
		return (v > 0) ? (int) java.lang.Math.ceil(v - 0.5d) : (int) java.lang.Math.floor(v + 0.5d);
	}
	public static int roundHalfUp(double v) {
		return (v > 0) ? (int) java.lang.Math.floor(v + 0.5d) : (int) java.lang.Math.ceil(v - 0.5d);
	}

	public static long roundLongHalfEven(double v) {
		return (long) java.lang.Math.rint(v);
	}
	public static long roundLongHalfDown(double v) {
		return (v > 0) ? (long) java.lang.Math.ceil(v - 0.5d) : (long) java.lang.Math.floor(v + 0.5d);
	}
	public static long roundLongHalfUp(double v) {
		return (v > 0) ? (long) java.lang.Math.floor(v + 0.5d) : (long) java.lang.Math.ceil(v - 0.5d);
	}

	public static double random() {
		return java.lang.Math.random();
	}

	public static double signum(double v) {
		return java.lang.Math.signum(v);
	}
	public static float signum(float v) {
		return java.lang.Math.signum(v);
	}
	public static int signum(int v) {
		return (v >> 31) | (-v >>> 31);
	}
	public static int signum(long v) {
		return (int) ((v >> 63) | (-v >>> 63));
	}
}

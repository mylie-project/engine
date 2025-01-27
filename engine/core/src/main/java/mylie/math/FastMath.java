package mylie.math;

/**
 * Utility class FastMath providing optimized mathematical computations. This class includes
 * static methods for calculating trigonometric functions, inverse square root, and related
 * mathematical operations.
 */
public class FastMath {
	/**
	 * Private constructor to prevent instantiation of the utility class FastMath.
	 * This class is designed to provide static methods for mathematical computations
	 * and should not be instantiated.
	 */
	private FastMath() {
	}

	/**
	 * The mathematical constant π (pi), defined as the ratio of a circle's circumference
	 * to its diameter, approximately equal to 3.14159. This constant is sourced directly
	 * from {@link Math#PI} to ensure precision and consistency.
	 * <p>
	 * π is a fundamental constant used in various trigonometric, geometric, and analytical
	 * calculations throughout the utilities provided by the {@code FastMath} class.
	 */
	private static final double PI = java.lang.Math.PI;
	/**
	 * A constant representing half of the mathematical constant π (pi) as a single-precision
	 * floating-point number. π/2 is commonly used in trigonometric calculations, such as determining
	 * the phase shift between sine and cosine functions or working with angles in radians.
	 *
	 * This value is precomputed and cast to float to provide performance benefits
	 * and to align with operations requiring single-precision arithmetic.
	 */
	private static final float PI_OVER_2_f = (float) (PI * 0.5);

	/**
	 * Computes the sine of an angle provided in radians.
	 *
	 * @param angle the angle in radians for which the sine value is to be computed
	 * @return the sine of the specified angle as a single-precision floating-point value
	 */
	public static float sin(float angle) {
		return (float) Math.sin(angle);
	}

	/**
	 * Computes the inverse square root of the given value.
	 * The inverse square root is calculated as 1 divided by the square root of the input value.
	 *
	 * @param value the value for which to compute the inverse square root;
	 *              must be positive to produce a defined result
	 * @return the inverse square root of the specified value as a single-precision floating-point number
	 */
	public static float invsqrt(float value) {
		return 1.0f / (float) Math.sqrt(value);
	}

	/**
	 * Computes the cosine of an angle using its sine value and the given angle in radians.
	 * This method utilizes the phase shift property of sine and cosine, where
	 * cos(x) = sin(x + π/2).
	 *
	 * @param sinAngle the sine of the angle, used as input to compute the cosine
	 * @param angle the angle in radians corresponding to the sine value
	 * @return the cosine of the specified angle as a single-precision floating-point value
	 */
	public static float cosFromSin(float sinAngle, float angle) {
		return sin(angle + PI_OVER_2_f);
	}
}

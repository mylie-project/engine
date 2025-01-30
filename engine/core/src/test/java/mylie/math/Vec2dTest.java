package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vec2dTest {

	/**
	 * The class Vec2d represents a two-dimensional vector with double precision
	 * and implements several mathematical operations. The `mul` method multiplies
	 * the components of the current vector by the respective components of
	 * another vector, returning a new resulting vector.
	 */

	@Test
	void testMulWithPositiveValues() {
		Vec2d vec1 = new Vec2d(3.0, 4.0);
		Vec2d vec2 = new Vec2d(2.0, 5.0);

		Vec2<Double> result = vec1.mul(vec2);

		assertEquals(6.0, result.getX());
		assertEquals(20.0, result.getY());
	}

	@Test
	void testMulWithNegativeValues() {
		Vec2d vec1 = new Vec2d(-3.0, -4.0);
		Vec2d vec2 = new Vec2d(2.0, -5.0);

		Vec2<Double> result = vec1.mul(vec2);

		assertEquals(-6.0, result.getX());
		assertEquals(20.0, result.getY());
	}

	@Test
	void testMulWithZeroValues() {
		Vec2d vec1 = new Vec2d(0.0, 4.0);
		Vec2d vec2 = new Vec2d(2.0, 0.0);

		Vec2<Double> result = vec1.mul(vec2);

		assertEquals(0.0, result.getX());
		assertEquals(0.0, result.getY());
	}

	@Test
	void testMulWithOneValues() {
		Vec2d vec1 = new Vec2d(1.0, 1.0);
		Vec2d vec2 = new Vec2d(5.5, -3.3);

		Vec2<Double> result = vec1.mul(vec2);

		assertEquals(5.5, result.getX());
		assertEquals(-3.3, result.getY());
	}

	@Test
	void testMulWithMixedSignValues() {
		Vec2d vec1 = new Vec2d(-7.0, 3.0);
		Vec2d vec2 = new Vec2d(2.0, -4.0);

		Vec2<Double> result = vec1.mul(vec2);

		assertEquals(-14.0, result.getX());
		assertEquals(-12.0, result.getY());
	}

	@Test
	void testAddWithPositiveValues() {
		Vec2d vec1 = new Vec2d(1.0, 2.0);
		Vec2d vec2 = new Vec2d(3.0, 4.0);

		Vec2<Double> result = vec1.add(vec2);

		assertEquals(4.0, result.getX());
		assertEquals(6.0, result.getY());
	}

	@Test
	void testAddWithNegativeValues() {
		Vec2d vec1 = new Vec2d(-1.0, -2.0);
		Vec2d vec2 = new Vec2d(-3.0, 4.0);

		Vec2<Double> result = vec1.add(vec2);

		assertEquals(-4.0, result.getX());
		assertEquals(2.0, result.getY());
	}

	@Test
	void testSubWithPositiveValues() {
		Vec2d vec1 = new Vec2d(5.0, 7.0);
		Vec2d vec2 = new Vec2d(3.0, 2.0);

		Vec2<Double> result = vec1.sub(vec2);

		assertEquals(2.0, result.getX());
		assertEquals(5.0, result.getY());
	}

	@Test
	void testSubWithNegativeValues() {
		Vec2d vec1 = new Vec2d(-5.0, -7.0);
		Vec2d vec2 = new Vec2d(-3.0, -2.0);

		Vec2<Double> result = vec1.sub(vec2);

		assertEquals(-2.0, result.getX());
		assertEquals(-5.0, result.getY());
	}

	@Test
	void testDivWithPositiveValues() {
		Vec2d vec1 = new Vec2d(6.0, 8.0);
		Vec2d vec2 = new Vec2d(2.0, 4.0);

		Vec2<Double> result = vec1.div(vec2);

		assertEquals(3.0, result.getX());
		assertEquals(2.0, result.getY());
	}

	@Test
	void testDivWithDivisionByZero() {
		Vec2d vec1 = new Vec2d(6.0, 8.0);
		Vec2d vec2 = new Vec2d(2.0, 0.0);

		assertThrows(ArithmeticException.class, () -> vec1.div(vec2));
	}

	@Test
	void testNegate() {
		Vec2d vec1 = new Vec2d(3.0, -4.0);

		Vec2<Double> result = vec1.negate();

		assertEquals(-3.0, result.getX());
		assertEquals(4.0, result.getY());
	}

	@Test
	void testNormalize() {
		Vec2d vec1 = new Vec2d(3.0, 4.0);

		Vec2<Double> result = vec1.normalize();

		assertEquals(0.6, result.getX(), 0.0001);
		assertEquals(0.8, result.getY(), 0.0001);
	}

	@Test
	void testNormalizeWithZeroVector() {
		Vec2d vec1 = new Vec2d(0.0, 0.0);

		assertThrows(ArithmeticException.class, vec1::normalize);
	}

	@Test
	void testMax() {
		Vec2d vec1 = new Vec2d(3.0, 5.0);
		Vec2d vec2 = new Vec2d(4.0, 2.0);

		Vec2<Double> result = vec1.max(vec2);

		assertEquals(4.0, result.getX());
		assertEquals(5.0, result.getY());
	}

	@Test
	void testMin() {
		Vec2d vec1 = new Vec2d(3.0, 5.0);
		Vec2d vec2 = new Vec2d(4.0, 2.0);

		Vec2<Double> result = vec1.min(vec2);

		assertEquals(3.0, result.getX());
		assertEquals(2.0, result.getY());
	}
}

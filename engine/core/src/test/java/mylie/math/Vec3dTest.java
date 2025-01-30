package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vec3dTest {

	/**
	 * Tests for the normalize method in the Vec3d class.
	 * <p>
	 * The normalize method returns a unit vector in the same direction
	 * as the current vector. If the vector has zero length (magnitude),
	 * it throws an ArithmeticException.
	 */

	@Test
	void testNormalizeUnitVector() {
		// Given: A unit vector along the x-axis
		Vec3d vec = Vec3d.UNIT_X;

		// When: We normalize the vector
		Vec3d result = (Vec3d) vec.normalize();

		// Then: The result should be the same unit vector
		assertEquals(1.0, result.x(), 1e-6);
		assertEquals(0.0, result.y(), 1e-6);
		assertEquals(0.0, result.z(), 1e-6);
	}

	@Test
	void testNormalizeNonUnitVector() {
		// Given: A non-unit vector
		Vec3d vec = new Vec3d(3, 4, 0);

		// When: We normalize the vector
		Vec3d result = (Vec3d) vec.normalize();

		// Then: The result should be a unit vector
		double length = Math.sqrt(result.x() * result.x() + result.y() * result.y() + result.z() * result.z());
		assertEquals(1.0, length, 1e-6);
		assertEquals(3.0 / 5.0, result.x(), 1e-6);
		assertEquals(4.0 / 5.0, result.y(), 1e-6);
		assertEquals(0.0, result.z(), 1e-6);
	}

	@Test
	void testNormalizeSmallValues() {
		// Given: A vector with very small values
		Vec3d vec = new Vec3d(1e-10, 0, 0);

		// When: We normalize the vector
		Vec3d result = (Vec3d) vec.normalize();

		// Then: The result should be a unit vector
		double length = Math.sqrt(result.x() * result.x() + result.y() * result.y() + result.z() * result.z());
		assertEquals(1.0, length, 1e-6);
		assertEquals(1.0, result.x(), 1e-6);
		assertEquals(0.0, result.y(), 1e-6);
		assertEquals(0.0, result.z(), 1e-6);
	}

	@Test
	void testNormalizeNegativeValues() {
		// Given: A vector with negative values
		Vec3d vec = new Vec3d(-2, -3, -6);

		// When: We normalize the vector
		Vec3d result = (Vec3d) vec.normalize();

		// Then: The result should be a unit vector
		double length = Math.sqrt(result.x() * result.x() + result.y() * result.y() + result.z() * result.z());
		assertEquals(1.0, length, 1e-6);
		assertEquals(-2.0 / 7.0, result.x(), 1e-6);
		assertEquals(-3.0 / 7.0, result.y(), 1e-6);
		assertEquals(-6.0 / 7.0, result.z(), 1e-6);
	}

	@Test
	void testNormalizeThrowsForZeroVector() {
		// Given: A zero vector
		Vec3d vec = Vec3d.ZERO;

		// When & Then: We normalize the vector, an exception should be thrown
		assertThrows(ArithmeticException.class, vec::normalize);
	}

	@Test
	void testAdd() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(1, 2, 3);
		Vec3d vec2 = new Vec3d(4, 5, 6);

		// When: We add the vectors
		Vec3d result = (Vec3d) vec1.add(vec2);

		// Then: The result has summed components
		assertEquals(5.0, result.x(), 1e-6);
		assertEquals(7.0, result.y(), 1e-6);
		assertEquals(9.0, result.z(), 1e-6);
	}

	@Test
	void testSub() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(4, 5, 6);
		Vec3d vec2 = new Vec3d(1, 2, 3);

		// When: We subtract the vectors
		Vec3d result = (Vec3d) vec1.sub(vec2);

		// Then: The result has the difference of components
		assertEquals(3.0, result.x(), 1e-6);
		assertEquals(3.0, result.y(), 1e-6);
		assertEquals(3.0, result.z(), 1e-6);
	}

	@Test
	void testMul() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(1, 2, 3);
		Vec3d vec2 = new Vec3d(4, 5, 6);

		// When: We multiply the vectors element-wise
		Vec3d result = (Vec3d) vec1.mul(vec2);

		// Then: The result has multiplied components
		assertEquals(4.0, result.x(), 1e-6);
		assertEquals(10.0, result.y(), 1e-6);
		assertEquals(18.0, result.z(), 1e-6);
	}

	@Test
	void testDiv() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(4, 10, 6);
		Vec3d vec2 = new Vec3d(2, 2, 3);

		// When: We divide the vectors element-wise
		Vec3d result = (Vec3d) vec1.div(vec2);

		// Then: The result has divided components
		assertEquals(2.0, result.x(), 1e-6);
		assertEquals(5.0, result.y(), 1e-6);
		assertEquals(2.0, result.z(), 1e-6);

		// Given: A vector with zero in one component
		Vec3d vec3 = new Vec3d(1, 0, 2);

		// When & Then: Division by zero should throw an exception
		assertThrows(ArithmeticException.class, () -> vec1.div(vec3));
	}

	@Test
	void testMulAdd() {
		// Given: Two vectors and a factor
		Vec3d vec1 = new Vec3d(1, 2, 3);
		Vec3d vec2 = new Vec3d(4, 5, 6);
		Vec3d factor = new Vec3d(2, 2, 2);

		// When: We apply mulAdd
		Vec3d result = (Vec3d) vec1.mulAdd(vec2, factor);

		// Then: The result matches the formula (x + y*z)
		assertEquals(9.0, result.x(), 1e-6);
		assertEquals(12.0, result.y(), 1e-6);
		assertEquals(15.0, result.z(), 1e-6);
	}

	@Test
	void testNegate() {
		// Given: A vector
		Vec3d vec = new Vec3d(3, -4, 5);

		// When: We negate the vector
		Vec3d result = (Vec3d) vec.negate();

		// Then: The result is the negation of the original vector
		assertEquals(-3.0, result.x(), 1e-6);
		assertEquals(4.0, result.y(), 1e-6);
		assertEquals(-5.0, result.z(), 1e-6);
	}

	@Test
	void testDot() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(1, 2, 3);
		Vec3d vec2 = new Vec3d(4, 5, 6);

		// When: We calculate the dot product
		double result = vec1.dot(vec2);

		// Then: The result matches x1 * x2 + y1 * y2 + z1 * z2
		assertEquals(32.0, result, 1e-6);
	}

	@Test
	void testCross() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(1, 2, 3);
		Vec3d vec2 = new Vec3d(4, 5, 6);

		// When: We calculate the cross product
		Vec3d result = (Vec3d) vec1.cross(vec2);

		// Then: The result matches the cross product formula
		assertEquals(-3.0, result.x(), 1e-6);
		assertEquals(6.0, result.y(), 1e-6);
		assertEquals(-3.0, result.z(), 1e-6);
	}

	@Test
	void testMax() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(1, 7, 3);
		Vec3d vec2 = new Vec3d(4, 5, 8);

		// When: We calculate the max of the vectors
		Vec3d result = (Vec3d) vec1.max(vec2);

		// Then: The result has the maximum components
		assertEquals(4.0, result.x(), 1e-6);
		assertEquals(7.0, result.y(), 1e-6);
		assertEquals(8.0, result.z(), 1e-6);
	}

	@Test
	void testMin() {
		// Given: Two vectors
		Vec3d vec1 = new Vec3d(1, 7, 3);
		Vec3d vec2 = new Vec3d(4, 5, 8);

		// When: We calculate the min of the vectors
		Vec3d result = (Vec3d) vec1.min(vec2);

		// Then: The result has the minimum components
		assertEquals(1.0, result.x(), 1e-6);
		assertEquals(5.0, result.y(), 1e-6);
		assertEquals(3.0, result.z(), 1e-6);
	}

	@Test
	void testSwizzle() {
		// Given: A vector
		Vec3d vec = new Vec3d(1, 2, 3);

		// When: We swizzle the components
		Vec3d result = (Vec3d) vec.swizzle(Vec3.X, Vec3.Z, Vec3.Y);

		// Then: The result should have the components rearranged
		assertEquals(1.0, result.x(), 1e-6);
		assertEquals(3.0, result.y(), 1e-6);
		assertEquals(2.0, result.z(), 1e-6);
	}
}

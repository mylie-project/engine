package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vector3fTest {

	/**
	 * Class under test: {@link Vector3f} Method under test:
	 * {@link Vector3f#normalize()}
	 * <p>
	 * This method computes the normalized vector of the invoking {@link Vector3f}
	 * instance. The returned vector will have the same direction but a length of 1.
	 */

	@Test
	void testNormalizeWithNonZeroVector() {
		// Arrange
		Vector3f vector = new Vector3f(3, 4, 0);

		// Act
		Vector3f normalized = vector.normalize();

		// Assert
		assertEquals(1.0, normalized.length(), 1e-6, "Length of normalized vector should be 1");
		assertEquals(3.0 / 5.0, normalized.x(), 1e-6, "X component is not normalized correctly");
		assertEquals(4.0 / 5.0, normalized.y(), 1e-6, "Y component is not normalized correctly");
		assertEquals(0.0, normalized.z(), 1e-6, "Z component is not normalized correctly");
	}

	@Test
	void testNormalizeWithSmallValues() {
		// Arrange
		Vector3f vector = new Vector3f(1e-7f, 1e-7f, 0);

		// Act
		Vector3f normalized = vector.normalize();

		// Assert
		assertEquals(1.0, normalized.length(), 1e-6, "Length of normalized vector should be 1");
		assertEquals(1.0 / Math.sqrt(2), normalized.x(), 1e-6, "X component is not normalized correctly");
		assertEquals(1.0 / Math.sqrt(2), normalized.y(), 1e-6, "Y component is not normalized correctly");
		assertEquals(0.0, normalized.z(), 1e-6, "Z component is not normalized correctly");
	}

	@Test
	void testAddWithZeroVector() {
		// Arrange
		Vector3f vector = new Vector3f(1.0f, 2.0f, 3.0f);

		// Act
		Vector3f result = vector.add(new Vector3f(0.0f, 0.0f, 0.0f));

		// Assert
		assertEquals(1.0f, result.x(), 1e-6, "X component is incorrect");
		assertEquals(2.0f, result.y(), 1e-6, "Y component is incorrect");
		assertEquals(3.0f, result.z(), 1e-6, "Z component is incorrect");
	}

	@Test
	void testSubtractWithZeroVector() {
		// Arrange
		Vector3f vector = new Vector3f(1.0f, 2.0f, 3.0f);

		// Act
		Vector3f result = vector.sub(new Vector3f(0.0f, 0.0f, 0.0f));

		// Assert
		assertEquals(1.0f, result.x(), 1e-6, "X component is incorrect");
		assertEquals(2.0f, result.y(), 1e-6, "Y component is incorrect");
		assertEquals(3.0f, result.z(), 1e-6, "Z component is incorrect");
	}

	@Test
	void testDotWithItself() {
		// Arrange
		Vector3f vector = new Vector3f(3, 4, 5);

		// Act
		float result = vector.dot(vector);

		// Assert
		assertEquals(vector.lengthSquared(), result, 1e-6, "Dot product with itself should equal squared length");
	}

	@Test
	void testDotWithOrthogonalVector() {
		// Arrange
		Vector3f vector1 = new Vector3f(1.0f, 0.0f, 0.0f);
		Vector3f vector2 = new Vector3f(0.0f, 1.0f, 0.0f);

		// Act
		float result = vector1.dot(vector2);

		// Assert
		assertEquals(0.0f, result, 1e-6, "Dot product of orthogonal vectors should be zero");
	}

	@Test
	void testCrossWithItself() {
		// Arrange
		Vector3f vector = new Vector3f(1.0f, 2.0f, 3.0f);

		// Act
		Vector3f result = vector.cross(vector);

		// Assert
		assertEquals(0.0f, result.x(), 1e-6, "X component of cross product with itself should be zero");
		assertEquals(0.0f, result.y(), 1e-6, "Y component of cross product with itself should be zero");
		assertEquals(0.0f, result.z(), 1e-6, "Z component of cross product with itself should be zero");
	}

	@Test
	void testNormalizeWithVectorOfLengthOne() {
		// Arrange
		Vector3f vector = new Vector3f(0.0f, 1.0f, 0.0f);

		// Act
		Vector3f normalized = vector.normalize();

		// Assert
		assertEquals(1.0, normalized.length(), 1e-6, "Length of normalized vector should still be 1");
		assertEquals(0.0, normalized.x(), 1e-6, "X component should remain the same");
		assertEquals(1.0, normalized.y(), 1e-6, "Y component should remain the same");
		assertEquals(0.0, normalized.z(), 1e-6, "Z component should remain the same");
	}

	@Test
	void testNormalizeWithZeroVector() {
		// Arrange
		Vector3f vector = new Vector3f(0, 0, 0);

		// Act and Assert
		assertThrows(ArithmeticException.class, vector::normalize, "Normalizing a zero vector should throw");
	}

	@Test
	void testNormalizeWithNegativeValues() {
		// Arrange
		Vector3f vector = new Vector3f(-3, -4, 0);

		// Act
		Vector3f normalized = vector.normalize();

		// Assert
		assertEquals(1.0, normalized.length(), 1e-6, "Length of normalized vector should be 1");
		assertEquals(-3.0 / 5.0, normalized.x(), 1e-6, "X component is not normalized correctly for negative vector");
		assertEquals(-4.0 / 5.0, normalized.y(), 1e-6, "Y component is not normalized correctly for negative vector");
		assertEquals(0.0, normalized.z(), 1e-6, "Z component is not normalized correctly for negative vector");
	}

	@Test
	void testNormalizeWithLargeValues() {
		// Arrange
		Vector3f vector = new Vector3f(1e6f, 0.0f, 0.0f);

		// Act
		Vector3f normalized = vector.normalize();

		// Assert
		assertEquals(1.0, normalized.length(), 1e-6, "Length of normalized vector should be 1");
		assertEquals(1.0, normalized.x(), 1e-6, "X component should be normalized to 1");
		assertEquals(0.0, normalized.y(), 1e-6, "Y component is not normalized correctly");
		assertEquals(0.0, normalized.z(), 1e-6, "Z component is not normalized correctly");
	}

	@Test
	void testAdd() {
		// Arrange
		Vector3f vector1 = new Vector3f(1, 2, 3);
		Vector3f vector2 = new Vector3f(4, 5, 6);

		// Act
		Vector3f result = vector1.add(vector2);

		// Assert
		assertEquals(5, result.x(), 1e-6, "X component is incorrect");
		assertEquals(7, result.y(), 1e-6, "Y component is incorrect");
		assertEquals(9, result.z(), 1e-6, "Z component is incorrect");
	}

	@Test
	void testSubtract() {
		// Arrange
		Vector3f vector1 = new Vector3f(5, 7, 9);
		Vector3f vector2 = new Vector3f(1, 2, 3);

		// Act
		Vector3f result = vector1.sub(vector2);

		// Assert
		assertEquals(4, result.x(), 1e-6, "X component is incorrect");
		assertEquals(5, result.y(), 1e-6, "Y component is incorrect");
		assertEquals(6, result.z(), 1e-6, "Z component is incorrect");
	}

	@Test
	void testDot() {
		// Arrange
		Vector3f vector1 = new Vector3f(1, 2, 3);
		Vector3f vector2 = new Vector3f(4, 5, 6);

		// Act
		float result = vector1.dot(vector2);

		// Assert
		assertEquals(32, result, 1e-6, "Dot product is incorrect");
	}

	@Test
	void testCross() {
		// Arrange
		Vector3f vector1 = new Vector3f(1, 2, 3);
		Vector3f vector2 = new Vector3f(4, 5, 6);

		// Act
		Vector3f result = vector1.cross(vector2);

		// Assert
		assertEquals(-3, result.x(), 1e-6, "X component is incorrect");
		assertEquals(6, result.y(), 1e-6, "Y component is incorrect");
		assertEquals(-3, result.z(), 1e-6, "Z component is incorrect");
	}

	@Test
	void testScale() {
		// Arrange
		Vector3f vector = new Vector3f(1, 2, 3);
		float scaleFactor = 2.5f;

		// Act
		Vector3f result = vector.mul(scaleFactor);

		// Assert
		assertEquals(2.5, result.x(), 1e-6, "X component is incorrect");
		assertEquals(5.0, result.y(), 1e-6, "Y component is incorrect");
		assertEquals(7.5, result.z(), 1e-6, "Z component is incorrect");
	}

	@Test
	void testLength() {
		// Arrange
		Vector3f vector = new Vector3f(3, 4, 0);

		// Act
		float length = vector.length();

		// Assert
		assertEquals(5.0, length, 1e-6, "Length is incorrect");
	}

	@Test
	void testLengthSquared() {
		// Arrange
		Vector3f vector = new Vector3f(3, 4, 0);

		// Act
		float lengthSquared = vector.lengthSquared();

		// Assert
		assertEquals(25, lengthSquared, 1e-6, "Length squared is incorrect");
	}

}

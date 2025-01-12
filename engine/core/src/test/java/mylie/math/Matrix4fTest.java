package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Matrix4fTest {

	@Test
	void testInvertIdentityMatrix() {
		// Arrange
		Matrix4f identity = Matrix4f.IDENTITY;

		// Act
		Matrix4f result = identity.invert();

		// Assert
		assertEquals(identity, result, "Inversion of the identity matrix should result in the identity matrix");
	}

	@Test
	void testInvertSimpleMatrix() {
		// Arrange
		Matrix4f matrix = new Matrix4f(2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2);

		// Act
		Matrix4f result = matrix.invert();

		// Assert
		Matrix4f expected = new Matrix4f(0.5f, 0, 0, 0, 0, 0.5f, 0, 0, 0, 0, 0.5f, 0, 0, 0, 0, 0.5f);
		assertEquals(expected, result, "Inversion of matrix failed");
	}

	@Test
	void testInvertNonInvertibleMatrix() {
		// Arrange
		Matrix4f nonInvertibleMatrix = new Matrix4f(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		// Act & Assert
		assertThrows(ArithmeticException.class, nonInvertibleMatrix::invert,
				"Non-invertible matrix should throw an exception");
	}

	@Test
	void testInvertComplexMatrix() {
		// Arrange
		Matrix4f matrix = new Matrix4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

		// Act & Assert
		assertThrows(ArithmeticException.class, matrix::invert, "Matrix with determinant 0 should be non-invertible");
	}

}

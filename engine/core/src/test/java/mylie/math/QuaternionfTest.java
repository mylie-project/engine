package mylie.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class QuaternionfTest {

	/**
	 * Tests for the rotateAxis method in the Quaternionf class.
	 * <p>
	 * Method Description: - rotateAxis(float angle, float axisX, float axisY, float
	 * axisZ): This method rotates the current quaternion by a given angle around a
	 * specified axis and returns the resulting quaternion.
	 */

	@Test
	void testRotateAxisAroundX() {
		// Arrange
		float angle = (float) Math.PI / 2; // 90 degrees
		float axisX = 1.0f, axisY = 0.0f, axisZ = 0.0f;
		Quaternionf initial = new Quaternionf();

		// Act
		Quaternionf result = initial.rotateAxis(angle, axisX, axisY, axisZ);

		// Assert
		float expectedX = Math.sin(angle / 2);
		float expectedW = Math.cos(angle / 2);
		assertEquals(expectedX, result.x(), 1e-6f);
		assertEquals(0.0f, result.y(), 1e-6f);
		assertEquals(0.0f, result.z(), 1e-6f);
		assertEquals(expectedW, result.w(), 1e-6f);
	}

	@Test
	void testRotateAxisAroundY() {
		// Arrange
		float angle = (float) Math.PI; // 180 degrees
		float axisX = 0.0f, axisY = 1.0f, axisZ = 0.0f;
		Quaternionf initial = new Quaternionf();

		// Act
		Quaternionf result = initial.rotateAxis(angle, axisX, axisY, axisZ);

		// Assert
		float expectedY = Math.sin(angle / 2);
		float expectedW = Math.cos(angle / 2);
		assertEquals(0.0f, result.x(), 1e-6f);
		assertEquals(expectedY, result.y(), 1e-6f);
		assertEquals(0.0f, result.z(), 1e-6f);
		assertEquals(expectedW, result.w(), 1e-6f);
	}

	@Test
	void testRotateAxisAroundZ() {
		// Arrange
		float angle = (float) (Math.PI / 4); // 45 degrees
		float axisX = 0.0f, axisY = 0.0f, axisZ = 1.0f;
		Quaternionf initial = new Quaternionf();

		// Act
		Quaternionf result = initial.rotateAxis(angle, axisX, axisY, axisZ);

		// Assert
		float expectedZ = Math.sin(angle / 2);
		float expectedW = Math.cos(angle / 2);
		assertEquals(0.0f, result.x(), 1e-6f);
		assertEquals(0.0f, result.y(), 1e-6f);
		assertEquals(expectedZ, result.z(), 1e-6f);
		assertEquals(expectedW, result.w(), 1e-6f);
	}

	@Test
	void testRotateAxisDiagonalAxis() {
		// Arrange
		float angle = (float) Math.PI / 3; // 60 degrees
		float axisX = 1.0f, axisY = 1.0f, axisZ = 1.0f;
		Quaternionf initial = new Quaternionf();

		// Act
		Quaternionf result = initial.rotateAxis(angle, axisX, axisY, axisZ);

		// Assert
		float invLength = (float) (1.0 / Math.sqrt(3.0));
		float sinAngle = Math.sin(angle / 2);
		float expectedX = axisX * invLength * sinAngle;
		float expectedY = axisY * invLength * sinAngle;
		float expectedZ = axisZ * invLength * sinAngle;
		float expectedW = Math.cos(angle / 2);
		assertEquals(expectedX, result.x(), 1e-6f);
		assertEquals(expectedY, result.y(), 1e-6f);
		assertEquals(expectedZ, result.z(), 1e-6f);
		assertEquals(expectedW, result.w(), 1e-6f);
	}

	@Test
	void testRotateAxisZeroAngle() {
		// Arrange
		float angle = 0.0f;
		float axisX = 1.0f, axisY = 0.0f, axisZ = 0.0f;
		Quaternionf initial = new Quaternionf();

		// Act
		Quaternionf result = initial.rotateAxis(angle, axisX, axisY, axisZ);

		// Assert
		assertEquals(0.0f, result.x(), 1e-6f);
		assertEquals(0.0f, result.y(), 1e-6f);
		assertEquals(0.0f, result.z(), 1e-6f);
		assertEquals(1.0f, result.w(), 1e-6f);
	}
}

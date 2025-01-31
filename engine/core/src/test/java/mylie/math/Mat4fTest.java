package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Mat4fTest {

	@Test
	void testAdd() {
		Mat4<Float> matrix1 = Mat4.f(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f,
				14.0f, 15.0f, 16.0f);
		Mat4<Float> matrix2 = Mat4.f(16.0f, 15.0f, 14.0f, 13.0f, 12.0f, 11.0f, 10.0f, 9.0f, 8.0f, 7.0f, 6.0f, 5.0f,
				4.0f, 3.0f, 2.0f, 1.0f);
		Mat4<Float> result = matrix1.add(matrix2);

		Mat4<Float> expected = new Mat4f(17.0f, 17.0f, 17.0f, 17.0f, 17.0f, 17.0f, 17.0f, 17.0f, 17.0f, 17.0f, 17.0f,
				17.0f, 17.0f, 17.0f, 17.0f, 17.0f);
		assertEquals(expected, result);
	}

	@Test
	void testSub() {
		Mat4<Float> matrix1 = Mat4.f(10.0f, 9.0f, 8.0f, 7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f, 0.0f, -1.0f, -2.0f,
				-3.0f, -4.0f, -5.0f);
		Mat4<Float> matrix2 = Mat4.f(5.0f, 4.0f, 3.0f, 2.0f, 1.0f, 0.0f, -1.0f, -2.0f, -3.0f, -4.0f, -5.0f, -6.0f,
				-7.0f, -8.0f, -9.0f, -10.0f);
		Mat4<Float> result = matrix1.sub(matrix2);

		Mat4<Float> expected = new Mat4f(5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f,
				5.0f, 5.0f, 5.0f);
		assertEquals(expected, result);
	}

	@Test
	void testMul() {
		Mat4<Float> matrix1 = Mat4.f(2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f);
		Mat4<Float> matrix2 = Mat4.f(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f,
				14.0f, 15.0f, 16.0f);
		Mat4<Float> result = matrix1.mul(matrix2);

		Mat4<Float> expected = new Mat4f(2.0f, 4.0f, 6.0f, 8.0f, 10.0f, 12.0f, 14.0f, 16.0f, 18.0f, 20.0f, 22.0f, 24.0f,
				13.0f, 14.0f, 15.0f, 16.0f);
		assertEquals(expected, result);
	}

	@Test
	void testTranspose() {
		Mat4<Float> matrix = Mat4.f(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f,
				14.0f, 15.0f, 16.0f);
		Mat4<Float> result = matrix.transpose();

		Mat4<Float> expected = new Mat4f(1.0f, 5.0f, 9.0f, 13.0f, 2.0f, 6.0f, 10.0f, 14.0f, 3.0f, 7.0f, 11.0f, 15.0f,
				4.0f, 8.0f, 12.0f, 16.0f);
		assertEquals(expected, result);
	}

	@Test
	void testDeterminant() {
		Mat4<Float> matrix = Mat4.f(6.0f, 1.0f, 1.0f, 0.0f, 4.0f, -2.0f, 5.0f, 0.0f, 2.0f, 8.0f, 7.0f, 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f);
		float determinant = matrix.determinant();

		assertEquals(-306.0f, determinant);
	}

	@Test
	void testInverseIdentity() {
		Mat4<Float> matrix = Mat4.f();
		Mat4<Float> result = matrix.inverse();
		assertEquals(matrix, result);
	}

	@Test
	void testInverseSingularMatrix() {
		Mat4<Float> singularMatrix = Mat4.f(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f,
				13.0f, 14.0f, 15.0f, 16.0f);

		assertThrows(ArithmeticException.class, singularMatrix::inverse);
	}

	@Test
	void testTransformWithIdentityMatrix() {
		Mat4f identity = Mat4f.IDENTITY;
		Vec3<Float> vector = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> result = identity.transform(vector);

		assertEquals(1.0f, result.getX());
		assertEquals(2.0f, result.getY());
		assertEquals(3.0f, result.getZ());
	}

	@Test
	void testTransformWithTranslationMatrix() {
		Mat4f translationMatrix = new Mat4f(1.0f, 0.0f, 0.0f, 10.0f, 0.0f, 1.0f, 0.0f, 20.0f, 0.0f, 0.0f, 1.0f, 30.0f,
				0.0f, 0.0f, 0.0f, 1.0f);

		Vec3<Float> vector = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> result = translationMatrix.transform(vector);

		assertEquals(11.0f, result.getX());
		assertEquals(22.0f, result.getY());
		assertEquals(33.0f, result.getZ());
	}

	@Test
	void testTransformWithScalingMatrix() {
		Mat4f scalingMatrix = new Mat4f(2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f);

		Vec3<Float> vector = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> result = scalingMatrix.transform(vector);

		assertEquals(2.0f, result.getX());
		assertEquals(6.0f, result.getY());
		assertEquals(12.0f, result.getZ());
	}

	@Test
	void testTransformWithRotationMatrix() {
		Mat4f rotationMatrix = new Mat4f(0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f); // 90-degree rotation around Z-axis

		Vec3<Float> vector = Vec3.of(1.0f, 0.0f, 0.0f);
		Vec3<Float> result = rotationMatrix.transform(vector);

		assertEquals(0.0f, result.getX());
		assertEquals(1.0f, result.getY());
		assertEquals(0.0f, result.getZ());
	}

	@Test
	void testTransformWithCustomMatrix() {
		Mat4f customMatrix = new Mat4f(2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f,
				14.0f, 15.0f, 16.0f, 17.0f);

		Vec3<Float> vector = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> result = customMatrix.transform(vector);

		float expectedX = 2 + 3 * 2 + 4 * 3 + 5;
		float expectedY = 6 + 7 * 2 + 8 * 3 + 9;
		float expectedZ = 10 + 11 * 2 + 12 * 3 + 13;

		assertEquals(expectedX, result.getX());
		assertEquals(expectedY, result.getY());
		assertEquals(expectedZ, result.getZ());
	}
}

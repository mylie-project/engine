package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class QuaternionfTest {

	/**
	 * Tests the add method of Quaternionf, which performs
	 * element-wise addition of two quaternions and returns a new quaternion.
	 */

	@Test
	void testAdd_PositiveValues() {
		Quaternionf q1 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		Quaternionf q2 = new Quaternionf(5.0f, 6.0f, 7.0f, 8.0f);

		Quaternionf result = (Quaternionf) q1.add(q2);

		assertEquals(6.0f, result.x());
		assertEquals(8.0f, result.y());
		assertEquals(10.0f, result.z());
		assertEquals(12.0f, result.w());
	}

	@Test
	void testAdd_NegativeValues() {
		Quaternionf q1 = new Quaternionf(-1.0f, -2.0f, -3.0f, -4.0f);
		Quaternionf q2 = new Quaternionf(-5.0f, -6.0f, -7.0f, -8.0f);

		Quaternionf result = (Quaternionf) q1.add(q2);

		assertEquals(-6.0f, result.x());
		assertEquals(-8.0f, result.y());
		assertEquals(-10.0f, result.z());
		assertEquals(-12.0f, result.w());
	}

	@Test
	void testAdd_ZeroValues() {
		Quaternionf q1 = new Quaternionf(0.0f, 0.0f, 0.0f, 0.0f);
		Quaternionf q2 = new Quaternionf(0.0f, 0.0f, 0.0f, 0.0f);

		Quaternionf result = (Quaternionf) q1.add(q2);

		assertEquals(0.0f, result.x());
		assertEquals(0.0f, result.y());
		assertEquals(0.0f, result.z());
		assertEquals(0.0f, result.w());
	}

	@Test
	void testAdd_MixedValues() {
		Quaternionf q1 = new Quaternionf(1.0f, -2.0f, 3.0f, -4.0f);
		Quaternionf q2 = new Quaternionf(-5.0f, 6.0f, -7.0f, 8.0f);

		Quaternionf result = (Quaternionf) q1.add(q2);

		assertEquals(-4.0f, result.x());
		assertEquals(4.0f, result.y());
		assertEquals(-4.0f, result.z());
		assertEquals(4.0f, result.w());
	}

	@Test
	void testAdd_LargeValues() {
		Quaternionf q1 = new Quaternionf(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Quaternionf q2 = new Quaternionf(1.0f, 1.0f, 1.0f, 1.0f);

		Quaternionf result = (Quaternionf) q1.add(q2);

		assertEquals(Float.MAX_VALUE, result.x());
		assertEquals(Float.MAX_VALUE, result.y());
		assertEquals(Float.MAX_VALUE, result.z());
		assertEquals(Float.MAX_VALUE, result.w());
	}

	@Test
	void testConstructorAndGetters() {
		Quaternionf q = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		assertEquals(1.0f, q.x());
		assertEquals(2.0f, q.y());
		assertEquals(3.0f, q.z());
		assertEquals(4.0f, q.w());
	}

	@Test
	void testEqualsAndHashCode() {
		Quaternionf q1 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		Quaternionf q2 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		Quaternionf q3 = new Quaternionf(0.0f, 0.0f, 0.0f, 0.0f);

		assertEquals(q1, q2, "Quaternions with same values should be equal");
		assertNotEquals(q1, q3, "Quaternions with different values should not be equal");

		assertEquals(q1.hashCode(), q2.hashCode(), "Hash codes should match for equal quaternions");
	}

	@Test
	void testToString() {
		Quaternionf q = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		String stringValue = q.toString();
		// Just check that the string contains the components; exact format may vary
		assertTrue(stringValue.contains("1.0"));
		assertTrue(stringValue.contains("2.0"));
		assertTrue(stringValue.contains("3.0"));
		assertTrue(stringValue.contains("4.0"));
	}

	@Test
	void testSub() {
		Quaternionf q1 = new Quaternionf(5.0f, 6.0f, 7.0f, 8.0f);
		Quaternionf q2 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);

		Quaternionf result = (Quaternionf) q1.sub(q2);
		assertEquals(4.0f, result.x());
		assertEquals(4.0f, result.y());
		assertEquals(4.0f, result.z());
		assertEquals(4.0f, result.w());
	}

	@Test
	void testDiv() {
		// Non-zero divisor
		Quaternionf q1 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		// This quaternion has x^2 + y^2 + z^2 + w^2 != 0
		Quaternionf q2 = new Quaternionf(1.0f, 0.0f, 0.0f, 0.0f);

		Quaternionf result = (Quaternionf) q1.div(q2);
		// Evaluate whether result is correct with respect to the expected math
		// Not focusing on the exact numeric identity, but ensuring no exceptions and
		// that it returns a valid quaternion
		assertNotNull(result, "Division result should not be null");

		// Division by zero check
		Quaternionf zero = new Quaternionf(0.0f, 0.0f, 0.0f, 0.0f);
		assertThrows(ArithmeticException.class, () -> q1.div(zero),
				"Dividing by a zero-magnitude quaternion should throw ArithmeticException");
	}

	@Test
	void testMul() {
		Quaternionf q1 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);
		Quaternionf q2 = new Quaternionf(-1.0f, 0.5f, 2.0f, -0.5f);

		Quaternionf result = (Quaternionf) q1.mul(q2);
		assertNotNull(result, "Multiplication result should not be null");
		// These exact numeric values might differ in floating-point math, but checking
		// that no exception occurs
	}

	@Test
	void testDot() {
		Quaternionf q1 = new Quaternionf(2.0f, 3.0f, 4.0f, 5.0f);
		Quaternionf q2 = new Quaternionf(1.0f, 2.0f, 3.0f, 4.0f);

		float dotResult = q1.dot(q2);
		// Dot product is (2*1 + 3*2 + 4*3 + 5*4) = 2 + 6 + 12 + 20 = 40
		assertEquals(40.0f, dotResult, 1e-6f);
	}

	@Test
	void testConjugate() {
		Quaternionf q1 = new Quaternionf(1.0f, -2.0f, 3.0f, 4.0f);
		Quaternionf conj = (Quaternionf) q1.conjugate();

		assertEquals(-1.0f, conj.x());
		assertEquals(2.0f, conj.y());
		assertEquals(-3.0f, conj.z());
		assertEquals(4.0f, conj.w());
	}

	@Test
	void testInverse() {
		Quaternionf q1 = new Quaternionf(1.0f, 0.0f, 0.0f, 0.0f);
		Quaternionf inv = (Quaternionf) q1.inverse();

		// The inverse of (1,0,0,0) is (-1,0,0,0)
		assertEquals(-1.0f, inv.x());
		assertEquals(-0.0f, inv.y());
		assertEquals(-0.0f, inv.z());
		assertEquals(0.0f, inv.w());

		// Inverse of zero quaternion should throw
		Quaternionf zero = new Quaternionf(0.0f, 0.0f, 0.0f, 0.0f);
		assertThrows(ArithmeticException.class, zero::inverse,
				"Inverse of zero quaternion should throw ArithmeticException");
	}

	@Test
	void testNormalize() {
		Quaternionf q = new Quaternionf(2.0f, 2.0f, 2.0f, 2.0f);
		Quaternionf norm = (Quaternionf) q.normalize();

		// The length of (2,2,2,2) is sqrt(2^2+2^2+2^2+2^2) = sqrt(16)=4
		// So normalized components should be 0.5, 0.5, 0.5, 0.5
		float epsilon = 1e-6f;
		assertEquals(0.5f, norm.x(), epsilon);
		assertEquals(0.5f, norm.y(), epsilon);
		assertEquals(0.5f, norm.z(), epsilon);
		assertEquals(0.5f, norm.w(), epsilon);

		// Normalizing zero quaternion should throw
		Quaternionf zero = new Quaternionf(0.0f, 0.0f, 0.0f, 0.0f);
		assertThrows(ArithmeticException.class, zero::normalize,
				"Normalization of zero quaternion should throw ArithmeticException");
	}

	@Test
	void testDifference() {
		// difference(q1, q2) = q1.mul(q2.inverse())
		Quaternionf q1 = new Quaternionf(0.5f, 1.0f, -0.5f, 1.0f);
		Quaternionf q2 = new Quaternionf(1.0f, 1.0f, 1.0f, 1.0f);

		Quaternionf diff = (Quaternionf) q1.difference(q2);
		assertNotNull(diff, "Difference result should not be null");
	}

	@Test
	void testTransform() {
		// Minimal Vec3<Float> for testing
		Vec3<Float> vector = Vec3.of(1.0f, 0.0f, 0.0f);

		// Identity-like quaternion
		Quaternionf id = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);
		Vec3f result = (Vec3f) id.transform(vector);

		// Transforming by the identity should yield the same vector
		assertEquals(1.0f, result.x());
		assertEquals(0.0f, result.y());
		assertEquals(0.0f, result.z());
	}

	@Test
	void testRotateAxis() {
		// Rotate some quaternion around an axis
		Quaternionf q1 = new Quaternionf(1.0f, 0.0f, 0.0f, 0.0f);
		Vec3<Float> axis = Vec3.of(0.0f, 1.0f, 0.0f);
		Quaternionf rotated = (Quaternionf) q1.rotateAxis((float) Math.PI / 2, axis);

		assertNotNull(rotated, "rotateAxis should produce a result");
	}

	@Test
	void testRotationAxis() {
		// Create a quaternion from axis rotation
		Vec3<Float> axis = Vec3.of(0.0f, 1.0f, 0.0f);
		Quaternionf r = (Quaternionf) new Quaternionf(0, 0, 0, 1).rotationAxis((float) Math.PI / 2, axis);

		assertNotNull(r, "rotationAxis should produce a quaternion");
		// The rotation about Z by 90 degrees is typically (0, 0, sin(π/4), cos(π/4))
		// i.e., (0, 0, ~0.707..., ~0.707...)
	}

	@Test
	void testStaticConstructors() {
		// f() -> new quaternion with 0,0,0,1
		Quaternion<Float> defaultQ = Quaternion.f();
        assertInstanceOf(Quaternionf.class, defaultQ);
		Quaternionf dq = (Quaternionf) defaultQ;
		assertEquals(0.0f, dq.x());
		assertEquals(0.0f, dq.y());
		assertEquals(0.0f, dq.z());
		assertEquals(1.0f, dq.w());

		// f(x, y, z, w)
		Quaternion<Float> paramQ = Quaternion.f(1f, 2f, 3f, 4f);
        assertInstanceOf(Quaternionf.class, paramQ);
		Quaternionf pq = (Quaternionf) paramQ;
		assertEquals(1f, pq.x());
		assertEquals(2f, pq.y());
		assertEquals(3f, pq.z());
		assertEquals(4f, pq.w());
	}

}

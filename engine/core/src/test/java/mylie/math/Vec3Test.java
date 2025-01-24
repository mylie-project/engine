// Vec3Test.java

package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vec3Test {

	@Test
	void testAdd() {
		Vec3<Float> v1 = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> v2 = Vec3.of(4.0f, 5.0f, 6.0f);

		Vec3<Float> result = v1.add(v2);
		assertEquals(5.0f, result.getX());
		assertEquals(7.0f, result.getY());
		assertEquals(9.0f, result.getZ());
	}

	@Test
	void testAddLargeValues() {
		Vec3<Float> v1 = Vec3.of(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vec3<Float> v2 = Vec3.of(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

		Vec3<Float> result = v1.add(v2);
		assertTrue(Float.isInfinite(result.getX()));
		assertTrue(Float.isInfinite(result.getY()));
		assertTrue(Float.isInfinite(result.getZ()));
	}

	@Test
	void testSub() {
		Vec3<Float> v1 = Vec3.of(10.0f, 8.0f, 6.0f);
		Vec3<Float> v2 = Vec3.of(1.0f, 2.0f, 3.0f);

		Vec3<Float> result = v1.sub(v2);
		assertEquals(9.0f, result.getX());
		assertEquals(6.0f, result.getY());
		assertEquals(3.0f, result.getZ());
	}

	@Test
	void testMul() {
		Vec3<Float> v1 = Vec3.of(2.0f, 3.0f, 4.0f);
		Vec3<Float> v2 = Vec3.of(1.0f, 2.0f, 3.0f);

		Vec3<Float> result = v1.mul(v2);
		assertEquals(2.0f, result.getX());
		assertEquals(6.0f, result.getY());
		assertEquals(12.0f, result.getZ());
	}

	@Test
	void testDiv() {
		Vec3<Float> v1 = Vec3.of(8.0f, 6.0f, 4.0f);
		Vec3<Float> v2 = Vec3.of(2.0f, 3.0f, 4.0f);

		Vec3<Float> result = v1.div(v2);
		assertEquals(4.0f, result.getX());
		assertEquals(2.0f, result.getY());
		assertEquals(1.0f, result.getZ());
	}

	@Test
	void testDivByZero() {
		Vec3<Float> v1 = Vec3.of(8.0f, 6.0f, 4.0f);
		Vec3<Float> v2 = Vec3.of(0.0f, 0.0f, 0.0f);

		assertThrows(ArithmeticException.class, () -> v1.div(v2));
	}

	@Test
	void testMulAdd() {
		Vec3<Float> v1 = Vec3.of(1.0f, 1.0f, 1.0f);
		Vec3<Float> v2 = Vec3.of(2.0f, 3.0f, 4.0f);
		Vec3<Float> factor = Vec3.of(2.0f, 2.0f, 2.0f);

		// v1 + (v2 * factor) = (1,1,1) + (4,6,8) = (5,7,9)
		Vec3<Float> result = v1.mulAdd(v2, factor);
		assertEquals(5.0f, result.getX());
		assertEquals(7.0f, result.getY());
		assertEquals(9.0f, result.getZ());
	}

	@Test
	void testNegate() {
		Vec3<Float> v1 = Vec3.of(1.0f, -2.0f, 3.5f);
		Vec3<Float> result = v1.negate();
		assertEquals(-1.0f, result.getX());
		assertEquals(2.0f, result.getY());
		assertEquals(-3.5f, result.getZ());
	}

	@Test
	void testNormalize() {
		// For vector (3, 4, 0), length is 5
		Vec3<Float> v1 = Vec3.of(3.0f, 4.0f, 0.0f);
		Vec3<Float> vNorm = v1.normalize();
		float epsilon = 1e-6f;

		// The normalized form of (3,4,0) should be (0.6, 0.8, 0.0)
		assertEquals(0.6f, vNorm.getX(), epsilon);
		assertEquals(0.8f, vNorm.getY(), epsilon);
		assertEquals(0.0f, vNorm.getZ(), epsilon);
	}

	@Test
	void testNormalizeZeroLength() {
		Vec3<Float> v1 = Vec3.of(0.0f, 0.0f, 0.0f);

		assertThrows(ArithmeticException.class, v1::normalize);
	}

	@Test
	void testDot() {
		Vec3<Float> v1 = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> v2 = Vec3.of(4.0f, -2.0f, 1.0f);

		// Dot product: (1 * 4) + (2 * -2) + (3 * 1) = 4 - 4 + 3 = 3
		float dot = v1.dot(v2);
		assertEquals(3.0f, dot);
	}

	@Test
	void testDotOrthogonal() {
		Vec3<Float> v1 = Vec3.of(1.0f, 0.0f, 0.0f);
		Vec3<Float> v2 = Vec3.of(0.0f, 1.0f, 0.0f);

		// Dot product of orthogonal vectors should be 0
		float dot = v1.dot(v2);
		assertEquals(0.0f, dot);
	}

	@Test
	void testMin() {
		Vec3<Float> v1 = Vec3.of(1.0f, 5.0f, 3.0f);
		Vec3<Float> v2 = Vec3.of(4.0f, 2.0f, 6.0f);

		Vec3<Float> result = v1.min(v2);
		assertEquals(1.0f, result.getX());
		assertEquals(2.0f, result.getY());
		assertEquals(3.0f, result.getZ());
	}

	@Test
	void testMax() {
		Vec3<Float> v1 = Vec3.of(1.0f, 5.0f, 3.0f);
		Vec3<Float> v2 = Vec3.of(4.0f, 2.0f, 6.0f);

		Vec3<Float> result = v1.max(v2);
		assertEquals(4.0f, result.getX());
		assertEquals(5.0f, result.getY());
		assertEquals(6.0f, result.getZ());
	}

	@Test
	void testCross() {
		// Cross product of (1,2,3) and (4,5,6) = ( (2*6 - 3*5), (3*4 - 1*6), (1*5 - 2*4
		// ) )
		// = (12 - 15, 12 - 6, 5 - 8) = (-3, 6, -3)
		Vec3<Float> v1 = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> v2 = Vec3.of(4.0f, 5.0f, 6.0f);

		Vec3<Float> cross = v1.cross(v2);
		assertEquals(-3.0f, cross.getX());
		assertEquals(6.0f, cross.getY());
		assertEquals(-3.0f, cross.getZ());
	}

	@Test
	void testSwizzle2D() {
		// For (x=1,y=2,z=3), swizzle(X, Z) -> (1,3) in 2D
		Vec3<Float> v1 = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec2<Float> swz = v1.swizzle(Vec3.X, Vec3.Z);

		assertEquals(1.0f, swz.getX());
		assertEquals(3.0f, swz.getY());
	}

	@Test
	void testSwizzle3D() {
		// For (x=1,y=2,z=3), swizzle(Y, Z, X) -> (2,3,1)
		Vec3<Float> v1 = Vec3.of(1.0f, 2.0f, 3.0f);
		Vec3<Float> swz = v1.swizzle(Vec3.Y, Vec3.Z, Vec3.X);

		assertEquals(2.0f, swz.getX());
		assertEquals(3.0f, swz.getY());
		assertEquals(1.0f, swz.getZ());
	}

	@Test
	void testUnitVectorsAndConstants() {
		Vec3<Float> zero = Vec3.of(0.0f, 0.0f, 0.0f);
		Vec3<Float> one = Vec3.of(1.0f, 1.0f, 1.0f);
		Vec3<Float> negOne = Vec3.of(-1.0f, -1.0f, -1.0f);
		Vec3<Float> ux = Vec3.of(1.0f, 0.0f, 0.0f);
		Vec3<Float> uy = Vec3.of(0.0f, 1.0f, 0.0f);
		Vec3<Float> uz = Vec3.of(0.0f, 0.0f, 1.0f);
		Vec3<Float> nx = Vec3.of(-1.0f, 0.0f, 0.0f);
		Vec3<Float> ny = Vec3.of(0.0f, -1.0f, 0.0f);
		Vec3<Float> nz = Vec3.of(0.0f, 0.0f, -1.0f);

		// Adjust the checks below to match your actual methods:
		assertEquals(zero.getX(), v3().zero().getX());
		assertEquals(one.getY(), v3().one().getY());
		assertEquals(negOne.getZ(), v3().negativeOne().getZ());
		assertEquals(ux.getX(), v3().unitX().getX());
		assertEquals(uy.getY(), v3().unitY().getY());
		assertEquals(uz.getZ(), v3().unitZ().getZ());
		assertEquals(nx.getX(), v3().negativeUnitX().getX());
		assertEquals(ny.getY(), v3().negativeUnitY().getY());
		assertEquals(nz.getZ(), v3().negativeUnitZ().getZ());
	}

	// Placeholder helper to illustrate calls (replace with your actual usage)
	private Vec3<Float> v3() {
		return Vec3.of(0.0f, 0.0f, 0.0f); // Replace this with an actual instance or factory method
	}
}

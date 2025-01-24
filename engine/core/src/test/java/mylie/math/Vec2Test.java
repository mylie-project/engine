// Vec2Test.java

package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Vec2Test {

    @Test
    void testAdd() {
        Vec2<Float> v1 = Vec2.of(1.0f, 2.0f);
        Vec2<Float> v2 = Vec2.of(3.0f, 4.0f);

        Vec2<Float> result = v1.add(v2);
        assertEquals(4.0f, result.getX());
        assertEquals(6.0f, result.getY());

        // Edge case: Addition with a vector containing extreme values
        Vec2<Float> maxVector = Vec2.of(Float.MAX_VALUE, Float.MAX_VALUE);
        Vec2<Float> minVector = Vec2.of(-Float.MAX_VALUE, -Float.MAX_VALUE);
        Vec2<Float> resultExtreme = maxVector.add(minVector);
        assertEquals(0.0f, resultExtreme.getX());
        assertEquals(0.0f, resultExtreme.getY());
    }

    @Test
    void testSub() {
        Vec2<Float> v1 = Vec2.of(5.0f, 7.0f);
        Vec2<Float> v2 = Vec2.of(2.0f, 3.0f);

        Vec2<Float> result = v1.sub(v2);
        assertEquals(3.0f, result.getX());
        assertEquals(4.0f, result.getY());
    }

    @Test
    void testMul() {
        Vec2<Float> v1 = Vec2.of(2.0f, 3.0f);
        Vec2<Float> v2 = Vec2.of(4.0f, 5.0f);

        Vec2<Float> result = v1.mul(v2);
        assertEquals(8.0f, result.getX());
        assertEquals(15.0f, result.getY());
    }

    @Test
    void testDiv() {
        Vec2<Float> v1 = Vec2.of(6.0f, 8.0f);
        Vec2<Float> v2 = Vec2.of(2.0f, 4.0f);

        Vec2<Float> result = v1.div(v2);
        assertEquals(3.0f, result.getX());
        assertEquals(2.0f, result.getY());

        // Test division by zero
        Vec2<Float> v3 = Vec2.of(6.0f, 8.0f);
        Vec2<Float> zeroVector = Vec2.of(0.0f, 0.0f);
        assertThrows(ArithmeticException.class, () -> v3.div(zeroVector));
    }

    @Test
    void testMulAdd() {
        Vec2<Float> v1 = Vec2.of(1.0f, 2.0f);
        Vec2<Float> v2 = Vec2.of(3.0f, 4.0f);
        Vec2<Float> factor = Vec2.of(2.0f, 5.0f);

        Vec2<Float> result = v1.mulAdd(v2, factor);
        // result = v1 + (v2 * factor) = (1,2) + (3*2, 4*5) = (1,2) + (6,20) = (7,22)
        assertEquals(7.0f, result.getX());
        assertEquals(22.0f, result.getY());
    }

    @Test
    void testNegate() {
        Vec2<Float> v1 = Vec2.of(2.5f, -3.0f);
        Vec2<Float> result = v1.negate();
        assertEquals(-2.5f, result.getX());
        assertEquals(3.0f, result.getY());
    }

    @Test
    void testNormalize() {
        // Example with a non-zero length vector
        Vec2<Float> v1 = Vec2.of(3.0f, 4.0f);
        Vec2<Float> norm = v1.normalize();
        // The vector (3,4) has length 5, so normalized result should be (0.6, 0.8)
        float epsilon = 1e-6f;
        assertEquals(0.6f, norm.getX(), epsilon);
        assertEquals(0.8f, norm.getY(), epsilon);

        // Edge case: Normalizing a zero-length vector
        Vec2<Float> zeroVector = Vec2.of(0.0f, 0.0f);
        assertThrows(ArithmeticException.class, zeroVector::normalize);
    }

    @Test
    void testSwizzle() {
        Vec2<Float> v1 = Vec2.of(5.0f, 7.0f);
        Vec2<Float> swapped = v1.swizzle(Vec2.Y, Vec2.X);

        assertEquals(7.0f, swapped.getX());
        assertEquals(5.0f, swapped.getY());
    }

    @Test
    void testUnitVectorsAndConstants() {
        Vec2<Float> ux = Vec2.of(1.0f, 0.0f);
        Vec2<Float> uy = Vec2.of(0.0f, 1.0f);
        Vec2<Float> zero = Vec2.of(0.0f, 0.0f);
        Vec2<Float> one = Vec2.of(1.0f, 1.0f);

        // Depending on your actual methods:
        assertEquals(ux.getX(), v1().unitX().getX());
        assertEquals(uy.getY(), v1().unitY().getY());
        assertEquals(zero.getX(), v1().zero().getX());
        assertEquals(one.getX(), v1().one().getX());
        // etc. Adjust as needed based on your actual methods.
    }

    // Placeholder helper to illustrate calls (replace with your actual usage)
    private Vec2<Float> v1() {
        return Vec2.of(0.0f, 0.0f); // Replace this with an actual instance or factory method
    }

    @Test
    void testMin() {
        Vec2<Float> v1 = Vec2.of(3.0f, 7.0f);
        Vec2<Float> v2 = Vec2.of(5.0f, 4.0f);

        Vec2<Float> result = v1.min(v2);
        assertEquals(3.0f, result.getX());
        assertEquals(4.0f, result.getY());

        // Edge case: min with equal components
        Vec2<Float> v3 = Vec2.of(1.0f, 1.0f);
        Vec2<Float> resultEqual = v3.min(v3);
        assertEquals(1.0f, resultEqual.getX());
        assertEquals(1.0f, resultEqual.getY());
    }

    @Test
    void testMax() {
        Vec2<Float> v1 = Vec2.of(3.0f, 7.0f);
        Vec2<Float> v2 = Vec2.of(5.0f, 4.0f);

        Vec2<Float> result = v1.max(v2);
        assertEquals(5.0f, result.getX());
        assertEquals(7.0f, result.getY());

        // Edge case: max with equal components
        Vec2<Float> v3 = Vec2.of(1.0f, 1.0f);
        Vec2<Float> resultEqual = v3.max(v3);
        assertEquals(1.0f, resultEqual.getX());
        assertEquals(1.0f, resultEqual.getY());
    }
}

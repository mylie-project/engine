package mylie.math;

import mylie.math.primitives.AlignedBox;
import mylie.math.primitives.Ray;
import mylie.math.primitives.Shape;
import mylie.math.primitives.Sphere;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollisionTest {

    @Test
    void testAlignedBoxAlignedBoxCollision() {
        AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        AlignedBox box2 = new AlignedBox(new Vector3f(3, 3, 3), new Vector3f(8, 8, 8));
        assertTrue(Collision.collides(box1, box2));
    }

    @Test
    void testAlignedBoxAlignedBoxNoCollision() {
        AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        AlignedBox box2 = new AlignedBox(new Vector3f(6, 6, 6), new Vector3f(10, 10, 10));
        assertFalse(Collision.collides(box1, box2));
    }

    @Test
    void testRayAlignedBoxCollision() {
        Ray ray = new Ray(new Vector3f(-1, 2.5f, 2.5f), new Vector3f(1, 0, 0));
        AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertTrue(Collision.collides(ray, box));
    }

    @Test
    void testRayAlignedBoxNoCollision() {
        Ray ray = new Ray(new Vector3f(-6, 6, 6), new Vector3f(1, 0, 0));
        AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertFalse(Collision.collides(ray, box));
    }

    @Test
    void testSphereAlignedBoxCollision() {
        Sphere sphere = new Sphere(new Vector3f(3, 3, 3), 2);
        AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertTrue(Collision.collides(box, sphere));
    }

    @Test
    void testSphereAlignedBoxNoCollision() {
        Sphere sphere = new Sphere(new Vector3f(8, 8, 8), 2);
        AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
        assertFalse(Collision.collides(box, sphere));
    }

    @Test
    void testSphereSphereCollision() {
        Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 3);
        Sphere sphere2 = new Sphere(new Vector3f(4, 0, 0), 2);
        assertTrue(Collision.collides(sphere1, sphere2));
    }

    @Test
    void testSphereSphereNoCollision() {
        Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 3);
        Sphere sphere2 = new Sphere(new Vector3f(10, 10, 10), 2);
        assertFalse(Collision.collides(sphere1, sphere2));
    }

    @Test
    void testRaySphereCollision() {
        Ray ray = new Ray(new Vector3f(-5, 0, 0), new Vector3f(1, 0, 0));
        Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 3);
        assertTrue(Collision.collides(ray, sphere));
    }

    @Test
    void testRaySphereNoCollision() {
        Ray ray = new Ray(new Vector3f(-5, 5, 0), new Vector3f(1, 0, 0));
        Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 3);
        assertFalse(Collision.collides(ray, sphere));
    }

    @Test
    void testUnsupportedCollision() {
        Shape unsupportedShape = new Shape() {
        };
        Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 2);
        assertThrows(UnsupportedOperationException.class, () -> Collision.collides(unsupportedShape, sphere));
    }
}
package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

public class CollisionMathTest {

	@Test
	void testCollisionBetweenTwoPlanes() {
		// Plane 1: x + 2y + 3z + 4 = 0
		Plane plane1 = new Plane(new Vector4f(1, 2, 3, 4));
		// Plane 2: -x - 2y - 3z - 4 = 0 (same as Plane 1 but with opposite direction)
		Plane plane2 = new Plane(new Vector4f(-1, -2, -3, 4));

		// Planes are coincident (same position and orientation)
		assertTrue(CollisionMath.collides(plane1, plane2), "Coincident planes should collide.");
	}

	@Test
	void testRaySphereCollision() {
		// Ray: origin at (0, 0, 0), direction (1, 0, 0)
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));
		// Sphere: Center at (5, 0, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(5, 0, 0), 1);

		// Ray should collide with the sphere
		assertTrue(CollisionMath.collides(ray, sphere), "Ray should intersect with the sphere.");
	}

	@Test
	void testSpherePlaneCollision() {
		// Plane: x + y + z + 1 = 0 (normal is [1, 1, 1])
		Plane plane = new Plane(new Vector4f(1, 1, 1, 1));
		// Sphere: center (0, 0, 0), radius 3
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 3);

		// Sphere should collide with the plane
		assertTrue(CollisionMath.collides(sphere, plane), "Sphere should intersect with the plane.");
	}

	@Test
	void testOrientedBoxSphereCollision() {
		// Oriented Box: centered at (0, 0, 0), size 2x2x2 in local space with axes
		// aligned
		OrientedBox box = new OrientedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0), // x-axis
				new Vector3f(0, 1, 0), // y-axis
				new Vector3f(0, 0, 1) // z-axis
		);
		// Sphere: center (1.5, 0, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(1.5f, 0, 0), 1);

		// Sphere should collide with the oriented box
		assertTrue(CollisionMath.collides(box, sphere), "Sphere should intersect with the oriented box.");
	}

	@Test
	void testAlignedBoxAlignedBoxCollision() {
		// Aligned Box 1: center (0, 0, 0), half extents (1, 1, 1)
		AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		// Aligned Box 2: center (1, 1, 1), half extents (1, 1, 1)
		AlignedBox box2 = new AlignedBox(new Vector3f(1, 1, 1), new Vector3f(1, 1, 1));

		// Boxes should collide as their volumes overlap
		assertTrue(CollisionMath.collides(box1, box2), "Aligned boxes should collide.");
	}

	@Test
	void testNoCollisionBetweenTwoAlignedBoxes() {
		// Aligned Box 1: center (0, 0, 0), half extents (1, 1, 1)
		AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		// Aligned Box 2: center (5, 5, 5), half extents (1, 1, 1)
		AlignedBox box2 = new AlignedBox(new Vector3f(5, 5, 5), new Vector3f(1, 1, 1));

		// Boxes should not collide as they are far apart
		assertFalse(CollisionMath.collides(box1, box2), "Aligned boxes should not collide.");
	}

	@Test
	void testRayAlignedBoxCollision() {
		// Ray: origin at (0, 0, 0), direction (1, 0, 0)
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));
		// Aligned Box: center (5, 0, 0), half extents (1, 1, 1)
		AlignedBox box = new AlignedBox(new Vector3f(5, 0, 0), new Vector3f(1, 1, 1));

		// Ray should collide with the box
		assertTrue(CollisionMath.collides(ray, box), "Ray should intersect with the aligned box.");
	}

	@Test
	void testSphereSphereCollision() {
		// Sphere 1: center (0, 0, 0), radius 2
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 2);
		// Sphere 2: center (3, 0, 0), radius 2
		Sphere sphere2 = new Sphere(new Vector3f(3, 0, 0), 2);

		// Spheres should collide as their radii overlap
		assertTrue(CollisionMath.collides(sphere1, sphere2), "Spheres should collide.");
	}

	@Test
	void testNoCollisionBetweenTwoSpheres() {
		// Sphere 1: center (0, 0, 0), radius 1
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 1);
		// Sphere 2: center (5, 5, 5), radius 1
		Sphere sphere2 = new Sphere(new Vector3f(5, 5, 5), 1);

		// Spheres should not collide as they are far apart
		assertFalse(CollisionMath.collides(sphere1, sphere2), "Spheres should not collide.");
	}

	@Test
	void testRayBarelyTouchesSphere() {
		// Ray: origin at (-5, 0, 0), direction (1, 0, 0)
		Ray ray = new Ray(new Vector3f(-5, 0, 0), new Vector3f(1, 0, 0));
		// Sphere: center (0, 1, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(0, 1, 0), 1);

		// Ray should barely graze/touch the sphere at one single point
		assertTrue(CollisionMath.collides(ray, sphere), "Ray should barely touch the sphere.");
	}

	@Test
	void testAlignedBoxFullyContainsSphere() {
		// Aligned Box: center (0, 0, 0), half extents (5, 5, 5)
		AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
		// Sphere: center (0, 0, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1);

		// Sphere is fully contained within the box; they should collide
		assertTrue(CollisionMath.collides(box, sphere), "Sphere fully contained within a box should collide.");
	}

	@Test
	void testSphereMissesOrientedBox() {
		// Oriented Box: centered at (10, 0, 0), size 2x2x2
		OrientedBox box = new OrientedBox(new Vector3f(10, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0),
				new Vector3f(0, 1, 0), new Vector3f(0, 0, 1));
		// Sphere: center (0, 0, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1);

		// Sphere is far away from the box; they should not collide
		assertFalse(CollisionMath.collides(box, sphere), "Sphere far from an oriented box should not collide.");
	}

	@Test
	void testPlaneIntersectsAlignedBox() {
		// Plane: x + y + z - 1 = 0 (normal is [1, 1, 1])
		Plane plane = new Plane(new Vector4f(1, 1, 1, -1));
		// Aligned Box: center at (0, 0, 0), half extents (1, 1, 1)
		AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

		// Plane should intersect with the aligned box
		assertTrue(CollisionMath.collides(plane, box), "Plane should intersect with aligned box.");
	}

	@Test
	void testPlaneMissesAlignedBox() {
		// Plane: x + y + z - 10 = 0 (normal is [1, 1, 1])
		Plane plane = new Plane(new Vector4f(1, 1, 1, -10));
		// Aligned Box: center at (1, 1, 1), half extents (0.5, 0.5, 0.5)
		AlignedBox box = new AlignedBox(new Vector3f(1, 1, 1), new Vector3f(0.5f, 0.5f, 0.5f));

		// Plane is far away from the box; they should not collide
		assertFalse(CollisionMath.collides(plane, box), "Plane far away from aligned box should not collide.");
	}

	@Test
	void testAlignedBoxTouchesSphere() {
		// Aligned Box: center (0, 0, 0), half extents (1, 1, 1)
		AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		// Sphere: center (2, 0, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(2, 0, 0), 1);

		// Sphere just touches the box at one point
		assertTrue(CollisionMath.collides(box, sphere), "Sphere should just touch the aligned box.");
	}

	@Test
	void testRayIntersectsOrientedBoxAtAngle() {
		// Ray: origin (0, 0, -10), direction (0, 0, 1) (pointing toward the box)
		Ray ray = new Ray(new Vector3f(0, 0, -10), new Vector3f(0, 0, 1));
		// Oriented Box: center (0, 0, 0), size 2x2x2
		OrientedBox box = new OrientedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0),
				new Vector3f(0, 1, 0), new Vector3f(0, 0, 1));

		// The ray should intersect the oriented box
		assertTrue(CollisionMath.collides(ray, box), "Ray should intersect the oriented box at an angle.");
	}

	@Test
	void testCollisionBetweenIdenticalAlignedBoxes() {
		// Aligned Box 1: center (0, 0, 0), half extents (1, 1, 1)
		AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		// Aligned Box 2: identical to Box 1
		AlignedBox box2 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));

		// Identical boxes should collide
		assertTrue(CollisionMath.collides(box1, box2), "Identical aligned boxes should collide.");
	}

	@Test
	void testNoCollisionBetweenParallelPlanes() {
		// Plane 1: x + y + z - 1 = 0
		Plane plane1 = new Plane(new Vector4f(1, 1, 1, -1));
		// Plane 2: x + y + z - 10 = 0 (parallel to Plane 1 but farther away)
		Plane plane2 = new Plane(new Vector4f(1, 1, 1, -10));

		// Parallel planes should not collide
		assertFalse(CollisionMath.collides(plane1, plane2), "Parallel planes should not collide.");
	}

	@Test
	void testRayParallelToSphere() {
		// Ray: origin (0, 2, 0), direction (1, 0, 0)
		Ray ray = new Ray(new Vector3f(0, 2, 0), new Vector3f(1, 0, 0));
		// Sphere: center at (0, 0, 0), radius 1
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1);

		// Ray should not collide with the sphere as it is parallel to the sphere and
		// outside its radius
		assertFalse(CollisionMath.collides(ray, sphere), "Ray parallel to sphere should not collide.");
	}

	@Test
	void testRayIntersectsSphere() {
		Ray ray = new Ray(new Vector3f(-5, 0, 0), new Vector3f(1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1);
		assertTrue(CollisionMath.collides(ray, sphere), "Ray collides with a sphere.");
	}

	@Test
	void testRayMissesSphere() {
		Ray ray = new Ray(new Vector3f(-5, 5, 0), new Vector3f(1, 0, 0)); // Parallel to sphere
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1);
		assertFalse(CollisionMath.collides(ray, sphere), "Ray misses sphere completely.");
	}

	@Test
	void testRayStartsInsideSphereAndPointsOut() {
		Ray ray = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0)); // Ray origin is inside the sphere
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 5);
		assertTrue(CollisionMath.collides(ray, sphere), "Ray starting inside sphere collides.");
	}

	// Additional tests vary the ray's direction, the sphere's position, and the
	// distances:
	@Test
	void testRayExactlyTouchesSphere() {
		Ray ray = new Ray(new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0));
		Sphere sphere = new Sphere(new Vector3f(0, 0, 0), 1); // Sphere's edge at ray start
		assertTrue(CollisionMath.collides(ray, sphere), "Ray tangentially touches the sphere.");
	}

	@Test
	void testRayMissesAlignedBox() {
		Ray ray = new Ray(new Vector3f(-10, -10, 0), new Vector3f(1, 0, 0)); // Never intersects
		AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		assertFalse(CollisionMath.collides(ray, box), "Ray should not collide with box.");
	}

	@Test
	void testRayIntersectsAlignedBox() {
		Ray ray = new Ray(new Vector3f(0, 0, -5), new Vector3f(0, 0, 1)); // Ray moves along z-axis
		AlignedBox box = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(2, 2, 2));
		assertTrue(CollisionMath.collides(ray, box), "Ray collides with aligned box.");
	}

	@Test
	void testAlignedBoxesCollide() {
		AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		AlignedBox box2 = new AlignedBox(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1, 1, 1));
		assertTrue(CollisionMath.collides(box1, box2), "Aligned boxes overlap and collide.");
	}

	@Test
	void testAlignedBoxesTouchEdges() {
		AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		AlignedBox box2 = new AlignedBox(new Vector3f(2, 0, 0), new Vector3f(1, 1, 1)); // Touches along one edge
		assertFalse(CollisionMath.collides(box1, box2), "Aligned boxes touching edges should not collide.");
	}

	@Test
	void testAlignedBoxesSeparate() {
		AlignedBox box1 = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		AlignedBox box2 = new AlignedBox(new Vector3f(5, 5, 5), new Vector3f(1, 1, 1));
		assertFalse(CollisionMath.collides(box1, box2), "Aligned boxes far apart should not collide.");
	}

	@Test
	void testAlignedBoxCompletelyInsideAnother() {
		AlignedBox outer = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(5, 5, 5));
		AlignedBox inner = new AlignedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		assertTrue(CollisionMath.collides(outer, inner), "Aligned box fully containing another should collide.");
	}

	@Test
	void testSpheresFullyOverlap() {
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 5);
		Sphere sphere2 = new Sphere(new Vector3f(1, 1, 1), 5);
		assertTrue(CollisionMath.collides(sphere1, sphere2), "Fully overlapping spheres collide.");
	}

	@Test
	void testSpheresTouchAtPoint() {
		Sphere sphere1 = new Sphere(new Vector3f(0, 0, 0), 5);
		Sphere sphere2 = new Sphere(new Vector3f(10, 0, 0), 5); // Touching at (5, 0, 0)
		assertTrue(CollisionMath.collides(sphere1, sphere2), "Spheres touching at one point collide.");
	}

	@Test
	void testOrientedBoxCollidesWithAlignedBox() {
		OrientedBox oBox = new OrientedBox(new Vector3f(0, 0, 0), new Vector3f(1, 1, 1), new Vector3f(1, 0, 0),
				new Vector3f(0, 1, 0), new Vector3f(0, 0, 1));
		AlignedBox aBox = new AlignedBox(new Vector3f(-1, -1, -1), new Vector3f(1, 1, 1));
		assertTrue(CollisionMath.collides(oBox, aBox), "Oriented box collides with aligned box.");
	}

	// Additional sample: 20 tests should vary the positions, sizes, directions, and
	// initial conditions
}

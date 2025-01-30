package mylie.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransformTest {

	@Test
	void testCombineWithDefaultTransforms() {
		Transform local = new Transform();
		Transform world = new Transform();
		Transform result = new Transform();

		result.combine(local, world);

		assertEquals(Vec3.of(0f, 0f, 0f), result.position);
		assertEquals(Vec3.of(1f, 1f, 1f), result.scale);
		assertEquals(Quaternion.f(0f, 0f, 0f, 1f), result.rotation);
	}

	@Test
	void testCombineWithScaledWorldTransform() {
		Transform local = new Transform();
		Transform world = new Transform();
		world.scale = Vec3.of(2f, 2f, 2f);

		Transform result = new Transform();
		result.combine(local, world);

		assertEquals(Vec3.of(0f, 0f, 0f), result.position);
		assertEquals(Vec3.of(2f, 2f, 2f), result.scale);
		assertEquals(Quaternion.f(0f, 0f, 0f, 1f), result.rotation);
	}

	@Test
	void testCombineWithTranslatedWorldTransform() {
		Transform local = new Transform();
		Transform world = new Transform();
		world.position = Vec3.of(5f, 5f, 5f);

		Transform result = new Transform();
		result.combine(local, world);

		assertEquals(Vec3.of(5f, 5f, 5f), result.position);
		assertEquals(Vec3.of(1f, 1f, 1f), result.scale);
		assertEquals(Quaternion.f(0f, 0f, 0f, 1f), result.rotation);
	}

	@Test
	void testCombineWithRotatedWorldTransform() {
		Transform local = new Transform();
		Transform world = new Transform();
		world.rotation = Quaternion.f(0f, 0f, 0.707f, 0.707f);

		Transform result = new Transform();
		result.combine(local, world);

		assertEquals(Vec3.of(0f, 0f, 0f), result.position);
		assertEquals(Vec3.of(1f, 1f, 1f), result.scale);
		assertEquals(Quaternion.f(0f, 0f, 0.707f, 0.707f), result.rotation);
	}

	@Test
	void testCombineWithLocalTransformAppliedToWorldTransform() {
		Transform world = new Transform();
		world.position = Vec3.of(5f, 5f, 5f);
		world.scale = Vec3.of(3f, 3f, 3f);
		world.rotation = Quaternion.f(0f, 0f, 0.707f, 0.707f);

		Transform local = new Transform();
		local.position = Vec3.of(1f, 0f, 0f);
		local.scale = Vec3.of(2f, 2f, 2f);
		local.rotation = Quaternion.f(0f, 0.707f, 0f, 0.707f);

		Transform result = new Transform();
		result.combine(local, world);

		assertEquals(Vec3.of(5f, 8f, 5f), result.position);
		assertEquals(Vec3.of(6f, 6f, 6f), result.scale);
		assertEquals(world.rotation.mul(local.rotation), result.rotation);
	}

	@Test
	void testCombineWithNonUniformScaledLocalTransform() {
		Transform world = new Transform();
		world.position = Vec3.of(0f, 0f, 0f);
		world.scale = Vec3.of(1f, 1f, 1f);

		Transform local = new Transform();
		local.position = Vec3.of(0f, 0f, 0f);
		local.scale = Vec3.of(1f, 2f, 0.5f);

		Transform result = new Transform();
		result.combine(local, world);

		assertEquals(Vec3.of(0f, 0f, 0f), result.position);
		assertEquals(Vec3.of(1f, 2f, 0.5f), result.scale);
		assertEquals(Quaternion.f(0f, 0f, 0f, 1f), result.rotation);
	}

	@Test
	void testCombineWithNonUniformRotatedLocalTransform() {
		Transform world = new Transform();
		world.position = Vec3.of(0f, 0f, 0f);
		world.rotation = Quaternion.f(0f, 0.707f, 0f, 0.707f);

		Transform local = new Transform();
		local.position = Vec3.of(1f, 0f, 0f);
		local.rotation = Quaternion.f(0.5f, 0.5f, 0.5f, 0.5f);

		Transform result = new Transform();
		result.combine(local, world);

		Vec3<Float> expectedPosition = Quaternion.f(0f, 0.707f, 0f, 0.707f).transform(Vec3.of(1f, 0f, 0f));
		assertEquals(expectedPosition, result.position);
		assertEquals(Quaternion.f(0f, 0.707f, 0f, 0.707f).mul(local.rotation), result.rotation);
		assertEquals(Vec3.of(1f, 1f, 1f), result.scale);
	}

	@Test
	void testCombineWithFullyTransformedParentAndLocal() {
		Transform world = new Transform();
		world.position = Vec3.of(10f, 5f, -2f);
		world.scale = Vec3.of(2f, 2f, 2f);
		world.rotation = Quaternion.f(0f, 0.707f, 0f, 0.707f);

		Transform local = new Transform();
		local.position = Vec3.of(1f, 1f, 0f);
		local.scale = Vec3.of(3f, 3f, 1f);
		local.rotation = Quaternion.f(0.707f, 0f, 0f, 0.707f);

		Transform result = new Transform();
		result.combine(local, world);

		Vec3<Float> expectedPosition = Quaternion.f(0f, 0.707f, 0f, 0.707f).transform(Vec3.of(2f, 2f, 0f))
				.add(world.position);
		assertEquals(expectedPosition, result.position);
		assertEquals(Vec3.of(6f, 6f, 2f), result.scale);
		assertEquals(world.rotation.mul(local.rotation), result.rotation);
	}
}

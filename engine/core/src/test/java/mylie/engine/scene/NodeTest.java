package mylie.engine.scene;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import mylie.math.Quaternion;
import mylie.math.Vec3;
import org.junit.jupiter.api.Test;

class NodeTest {

	@Test
	void testNodePosition() {
		Node node = new Node();
		Node child = new Node();
		node.translation(Vec3.of(1, 1, 1));
		node.child(child);
		assertEquals(Vec3.of(1, 1, 1), child.worldTransform().position());
	}

	@Test
	void testNodePositionAndRotation() {
		Node parent = new Node();
		Node child = new Node();
		parent.translation(Vec3.of(3, 3, 3));
		Quaternion<Float> rotation = Quaternion.f(0, 0, 0, 1).rotationAxis((float) Math.toRadians(45),
				Vec3.of(0, 1, 0));
		parent.rotation(rotation);
		parent.child(child);

		assertEquals(Vec3.of(3, 3, 3), child.worldTransform().position());
		assertEquals(rotation, child.worldTransform().rotation());
	}

	@Test
	void testComplexNodeTransformations() {
		Node root = new Node();
		Node intermediate = new Node();
		Node leaf = new Node();
		root.translation(Vec3.of(4, 4, 4));
		root.scale(2);
		Quaternion<Float> rootRotation = Quaternion.f().rotationAxis((float) Math.toRadians(90), Vec3.of(0, 1, 0));
		root.rotation(rootRotation);

		root.child(intermediate);
		intermediate.translation(Vec3.of(1, 1, 1));
		intermediate.scale(0.5f);

		intermediate.child(leaf);
		leaf.translation(Vec3.of(1, 0, 0));

		assertEquals(Vec3.of(6, 6, 2), intermediate.worldTransform().position());
		assertEquals(Vec3.of(6, 6, 1), leaf.worldTransform().position());
		assertEquals(rootRotation, intermediate.worldTransform().rotation());
	}

	@Test
	void testMultipleChildren() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();

		parent.translation(Vec3.of(2, 2, 2));
		parent.child(child1);
		parent.child(child2);

		child1.translation(Vec3.of(1, 0, 0));
		child2.translation(Vec3.of(0, 1, 0));

		assertEquals(Vec3.of(3, 2, 2), child1.worldTransform().position());
		assertEquals(Vec3.of(2, 3, 2), child2.worldTransform().position());
	}

	@Test
	void testNodeHierarchy() {
		Node root = new Node();
		Node parent = new Node();
		Node child = new Node();
		root.translation(Vec3.of(5, 5, 5));
		parent.translation(Vec3.of(2, 2, 2));
		root.child(parent);
		parent.child(child);
		assertEquals(Vec3.of(5, 5, 5), root.worldTransform().position());
		assertEquals(Vec3.of(7, 7, 7), parent.worldTransform().position());
		assertEquals(Vec3.of(7, 7, 7), child.worldTransform().position());
	}

	@Test
	void testNodeRotation() {
		Node node = new Node();
		Node child = new Node();
		Quaternion<Float> quaternionf = Quaternion.f().rotationAxis(90, Vec3.of(1, 0, 0));
		node.rotation(quaternionf);
		node.child(child);
		assertEquals(quaternionf, child.worldTransform().rotation());
	}

	@Test
	void testNodeScale() {
		Node node = new Node();
		Node child = new Node();
		node.scale(2);
		node.child(child);
		assertEquals(Vec3.of(2, 2, 2), child.worldTransform().scale());
	}

	@Test
	void testUpdateNodeTranslation() {
		Node node = new Node();
		node.translation(Vec3.of(1, 2, 3));
		assertEquals(Vec3.of(1, 2, 3), node.worldTransform().position());

		node.translation(Vec3.of(4, 5, 6));
		assertEquals(Vec3.of(4, 5, 6), node.worldTransform().position());
	}

	@Test
	void testUpdateNodeRotation() {
		Node node = new Node();
		Quaternion<Float> initialRotation = Quaternion.f().rotationAxis(45, Vec3.of(0, 1, 0));
		node.rotation(initialRotation);
		assertEquals(initialRotation, node.worldTransform().rotation());

		Quaternion<Float> updatedRotation = Quaternion.f().rotationAxis(90, Vec3.of(0, 1, 0));
		node.rotation(updatedRotation);
		assertEquals(updatedRotation, node.worldTransform().rotation());
	}

	@Test
	void testUpdateNodeScale() {
		Node node = new Node();
		Node child = new Node();

		node.scale(2);
		node.child(child);

		node.scale(3);
		assertEquals(Vec3.of(6, 6, 6), child.worldTransform().scale());
	}

	@Test
	void testTraversalThroughChildNodes() {
		Node root = new Node();
		Node child1 = new Node();
		Node child2 = new Node();

		root.child(child1, child2);
		child1.translation(Vec3.of(1, 0, 0));
		child2.translation(Vec3.of(0, 1, 0));

		List<Spatial<?, ?>> visited = new ArrayList<>();
		Traverser.traverse(Traverser.ToLeaf, root, visited::add);

		assertTrue(visited.contains(child1));
		assertTrue(visited.contains(child2));
	}

	@Test
	void testParentTransformPropagation() {
		Node parent = new Node();
		Node child = new Node();

		parent.child(child);
		parent.translation(Vec3.of(3, 3, 3));

		assertEquals(Vec3.of(3, 3, 3), child.worldTransform().position());

		parent.translation(Vec3.of(5, 5, 5));
		assertEquals(Vec3.of(5, 5, 5), child.worldTransform().position());
	}

	@Test
	void testTranslation() {
		Node parent = new Node();
		Node child = new Node();
		parent.child(child);
		assertEquals(Vec3.of(0, 0, 0), child.worldTransform().position());
		parent.translation(Vec3.of(1, 1, 1));
		assertEquals(Vec3.of(1, 1, 1), child.worldTransform().position());
		child.translation(Vec3.of(2, 2, 2));
		assertEquals(Vec3.of(3, 3, 3), child.worldTransform().position());
		assertEquals(Vec3.of(1, 1, 1), parent.worldTransform().position());
	}

	@Test
	void testScale() {
		Node parent = new Node();
		Node child = new Node();
		parent.child(child);
		parent.scale(2);
		child.translation(Vec3.of(1, 1, 1));
		assertEquals(Vec3.of(2, 2, 2), child.worldTransform().position());
		parent.scaling(1.0f);
		assertEquals(Vec3.of(1, 1, 1), child.worldTransform().position());
		child.scaling(0.5f);
		assertEquals(Vec3.of(1, 1, 1), child.worldTransform().position());
	}

	@Test
	void testRotation() {
		Node parent = new Node();
		Node child = new Node();
		parent.child(child);
		parent.rotation(Quaternion.f().rotationAxis((float) Math.toRadians(90), Vec3.of(1, 0, 0)));
		child.translation(Vec3.of(0, 1, 0));
		assertEquals(Vec3.of(0, 0, 1), child.worldTransform().position());
		parent.rotate(Quaternion.f().rotationAxis((float) Math.toRadians(90), Vec3.of(1, 0, 0)));
		assertEquals(Vec3.of(0, -1, child.worldTransform().position().getZ()), child.worldTransform().position());
		child.rotate(Quaternion.f().rotationAxis((float) Math.toRadians(90), Vec3.of(1, 0, 0)));
		assertEquals(Vec3.of(0, -1, child.worldTransform().position().getZ()), child.worldTransform().position());
	}

	@Test
	void testTraverseParallel() {
		Node root = new Node();
		Node child1 = new Node();
		Node child2 = new Node();
		Node child3 = new Node();
		Node child4 = new Node();

		root.child(child1, child2);
		child1.child(child3);
		child2.child(child4);
		List<Spatial<?, ?>> visited = new ArrayList<>();
		Traverser.parallelTraverse(Traverser.ToLeaf, root, visited::add);
		assertTrue(visited.contains(root));
		assertTrue(visited.contains(child1));
		assertTrue(visited.contains(child2));
		assertTrue(visited.contains(child3));
		assertTrue(visited.contains(child4));
		assertEquals(5, visited.size());
		assertTrue(visited.indexOf(child1) < visited.indexOf(child3));
		assertTrue(visited.indexOf(child2) < visited.indexOf(child4));
	}

}

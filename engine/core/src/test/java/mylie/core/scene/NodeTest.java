package mylie.core.scene;

import static org.junit.jupiter.api.Assertions.*;

import mylie.math.Quaternionf;
import mylie.math.Vector3f;
import org.junit.jupiter.api.Test;

class NodeTest {

	@Test
	void testNodePosition() {
		Node node = new Node();
		Node child = new Node();
		node.translation(new Vector3f(1, 1, 1));
		node.child(child);
		assertEquals(new Vector3f(1, 1, 1), child.worldTransform().position());
	}

	@Test
	void testNodePositionAndRotation() {
		Node parent = new Node();
		Node child = new Node();
		parent.translation(new Vector3f(3, 3, 3));
		Quaternionf rotation = new Quaternionf().rotationAxis((float) Math.toRadians(45), 0, 1, 0);
		parent.rotation(rotation);
		parent.child(child);

		assertEquals(new Vector3f(3, 3, 3), child.worldTransform().position());
		assertEquals(rotation, child.worldTransform().rotation());
	}

	@Test
	void testComplexNodeTransformations() {
		Node root = new Node();
		Node intermediate = new Node();
		Node leaf = new Node();
		root.translation(new Vector3f(4, 4, 4));
		root.scale(2);
		Quaternionf rootRotation = new Quaternionf().rotationAxis((float) Math.toRadians(90), 0, 1, 0);
		root.rotation(rootRotation);

		root.child(intermediate);
		intermediate.translation(new Vector3f(1, 1, 1));
		intermediate.scale(0.5f);

		intermediate.child(leaf);
		leaf.translation(new Vector3f(1, 0, 0));

		assertEquals(new Vector3f(6, 6, 2), intermediate.worldTransform().position());
		assertEquals(new Vector3f(6, 6, 1), leaf.worldTransform().position());
		assertEquals(rootRotation, intermediate.worldTransform().rotation());
	}

	@Test
	void testMultipleChildren() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();

		parent.translation(new Vector3f(2, 2, 2));
		parent.child(child1);
		parent.child(child2);

		child1.translation(new Vector3f(1, 0, 0));
		child2.translation(new Vector3f(0, 1, 0));

		assertEquals(new Vector3f(3, 2, 2), child1.worldTransform().position());
		assertEquals(new Vector3f(2, 3, 2), child2.worldTransform().position());
	}

	@Test
	void testNodeHierarchy() {
		Node root = new Node();
		Node parent = new Node();
		Node child = new Node();
		root.translation(new Vector3f(5, 5, 5));
		parent.translation(new Vector3f(2, 2, 2));
		root.child(parent);
		parent.child(child);
		assertEquals(new Vector3f(7, 7, 7), parent.worldTransform().position());
		assertEquals(new Vector3f(7, 7, 7), child.worldTransform().position());
	}

	@Test
	void testNodeRotation() {
		Node node = new Node();
		Node child = new Node();
		Quaternionf quaternionf = new Quaternionf().rotationAxis(90, 1, 0, 0);
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
		assertEquals(new Vector3f(2, 2, 2), child.worldTransform().scale());
	}

}

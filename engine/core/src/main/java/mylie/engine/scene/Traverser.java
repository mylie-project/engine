package mylie.engine.scene;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Traverser is a utility class for traversing hierarchical data structures
 * such as scene graphs. It includes static methods to perform traversal
 * in both sequential and parallel modes, using customizable traversal
 * directions and visitor callbacks.
 */
public class Traverser {
	/**
	 * Private default constructor for the Traverser utility class.
	 *
	 * The Traverser class is a static utility designed for traversing hierarchical
	 * data structures, such as scene graphs, using customizable traversal strategies
	 * and visitor callbacks. This private constructor prevents instantiation of the
	 * utility class, as all functionality is exposed via its static methods.
	 */
	private Traverser() {
	}

	/**
	 * A predefined {@link Direction} implementation that traverses from the current
	 * spatial node to its child nodes. This is typically used for depth-first or
	 * breadth-first traversal to iterate over the hierarchy towards the leaves
	 * of the scene graph.
	 */
	public static final Direction ToLeaf = (queue, spatial) -> {
		if (spatial instanceof Node node) {
			queue.addAll(node.children());
		}
	};
	/**
	 * A predefined {@link Direction} implementation that traverses from the current
	 * spatial node to its parent node. This is useful for propagating updates
	 * or performing operations that require moving towards the root of the scene graph.
	 */
	public static final Direction ToRoot = ((queue, spatial) -> {
		if (spatial.parent() != null) {
			queue.add(spatial.parent());
		}
	});

	/**
	 * Traverses a scene graph starting from the specified node using a given direction
	 * and applying a visitor to each node. The traversal proceeds based on the logic
	 * defined by the {@link Direction} and stops processing further nodes if the visitor
	 * returns {@code false}.
	 *
	 * @param direction The traversal direction, determining how nodes are visited.
	 * @param start     The starting node of the traversal.
	 * @param visitor   A callback-like visitor that performs actions on each visited node.
	 */
	public static void traverse(Direction direction, Spatial<?, ?> start, Visitor visitor) {
		Queue<Spatial<?, ?>> queue = new LinkedList<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Spatial<?, ?> spatial = queue.poll();
			if (visitor.visit(spatial)) {
				direction.apply(queue, spatial);
			}
		}
	}

	/**
	 * Traverses a scene graph in parallel, allowing multi-threaded processing of nodes.
	 * Each level in the scene graph is processed concurrently, applying the provided
	 * {@link Direction} and visitor to the nodes within it. Nodes at the same level are
	 * processed in parallel for performance optimization.
	 *
	 * @param direction The traversal direction, determining how nodes are visited.
	 * @param start     The starting node of the traversal.
	 * @param visitor   A callback-like visitor that performs actions on each visited node.
	 */
	public static void parallelTraverse(Direction direction, Spatial<?, ?> start, Visitor visitor) {
		Queue<Spatial<?, ?>> queue = new LinkedList<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			Queue<Spatial<?, ?>> nextLevel = new ConcurrentLinkedQueue<>();
			queue.parallelStream().forEach(spatial -> {
				visitor.visit(spatial);
				direction.apply(nextLevel, spatial);
			});
			queue = nextLevel;
		}
	}

	/**
	 * Represents a strategy or rule for determining the traversal behavior
	 * between nodes in a scene graph. Implementations of this interface define
	 * how the traversal moves from one spatial node to the next.
	 */
	public interface Direction {
		/**
		 * Applies the traversal logic to determine which nodes are visited next.
		 *
		 * @param queue   A queue of nodes to process during the traversal.
		 * @param spatial The current spatial node being processed.
		 */
		void apply(Queue<Spatial<?, ?>> queue, Spatial<?, ?> spatial);
	}

	/**
	 * Defines a callback interface for visiting nodes during a traversal operation.
	 * Implementing classes can define the behavior performed on each visited node
	 * and optionally control whether further traversal continues.
	 */
	public interface Visitor {
		/**
		 * Performs an operation on the specified spatial node during traversal.
		 *
		 * @param spatial The spatial node to be visited.
		 * @return {@code true} to continue traversal; {@code false} to stop.
		 */
		boolean visit(Spatial<?, ?> spatial);
	}
}

package mylie.engine.scene;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import mylie.engine.assets.AssetKey;

/**
 * Represents a hierarchical node in a scene graph that can contain child spatial elements.
 * Inherits and extends the functionality provided by the Spatial class, enabling transformations
 * like translation, rotation, and uniform scaling. A Node can hold other spatial entities as
 * children, maintaining a parent-child relationship.
 *
 * This class provides methods for managing child spatial entities by adding them to the node.
 * It automatically handles establishing parent relationships and updating transformations when
 * children are added.
 *
 * Nodes are identified uniquely by a {@link Node.Key}, which extends the AssetKey for this purpose.
 * The hierarchical relationship allows for propagation of transformation updates across the
 * scene graph.
 */
@Getter
public class Node extends Spatial<Node, Node.Key>
		implements
			Spatial.Translatable,
			Spatial.Rotatable,
			Spatial.ScalableUniform {
	final List<Spatial<?, ?>> children;

	/**
	 * Constructs a new Node instance with an empty list of children.
	 *
	 * Initializes the Node's internal structure to hold child spatial elements.
	 * This is the default constructor used to create an empty Node in the scene graph.
	 */
	public Node() {
		children = new ArrayList<>();
	}

	/**
	 * Adds one or more child spatial entities to this node. This method establishes a parent-child
	 * relationship by setting the parent of each child to this node, adding the child entities to
	 * the list of children, and triggering the local transformation update on each child.
	 *
	 * @param children One or more Spatial objects to be added as children of this node.
	 */
	public void child(Spatial<?, ?>... children) {
		for (Spatial<?, ?> child : children) {
			child.parent(this);
			this.children.add(child);
			child.onLocalTransformChanged();
		}
	}

	/**
	 * Represents a unique key specifically used to identify a {@link Node} within the asset management system.
	 * This class extends {@link AssetKey} and is parameterized to work with {@link Node} as the associated asset type.
	 * The key is uniquely defined by a path, which serves as its identifier.
	 */
	public static class Key extends AssetKey<Node, Key> {
		/**
		 * Constructs a {@code Key} instance with the specified unique path.
		 * This path serves as an identifier for a {@link Node} within the asset management system.
		 *
		 * @param path the unique path used to define and identify the key
		 */
		protected Key(String path) {
			super(path);
		}
	}
}

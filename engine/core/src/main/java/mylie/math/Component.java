package mylie.math;

/**
 * Represents a fundamental part or building block with a name identifier.
 */
public class Component {
	/**
	 * The name identifying the component.
	 */
	final String name;

	/**
	 * Constructs a new instance of {@code Component}.
	 *
	 * @param name the name identifier for the component
	 */
	protected Component(String name) {
		this.name = name;
	}
}

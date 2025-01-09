package mylie.core.action;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ActionGroupTest {

	/**
	 * Test class for {@link ActionGroup}. This class verifies the functionality of
	 * the {@link ActionGroup#enabled()} method. The method checks if the current
	 * group is enabled and if it has a parent, ensures the parent is also enabled.
	 */

	@Test
	void testEnabledWhenSelfEnabledAndNoParent() {
		// Arrange
		ActionGroup actionGroup = new ActionGroup("Group1", null);
		actionGroup.enabled(true);

		// Act
		boolean result = actionGroup.enabled();

		// Assert
		assertTrue(result, "ActionGroup should be enabled when it is enabled and has no parent.");
	}

	@Test
	void testEnabledWhenSelfDisabledAndNoParent() {
		// Arrange
		ActionGroup actionGroup = new ActionGroup("Group1", null);
		actionGroup.enabled(false);

		// Act
		boolean result = actionGroup.enabled();

		// Assert
		assertFalse(result, "ActionGroup should not be enabled when it is disabled and has no parent.");
	}

	@Test
	void testEnabledWhenSelfEnabledAndParentEnabled() {
		// Arrange
		ActionGroup parentGroup = new ActionGroup("Parent", null);
		parentGroup.enabled(true);

		ActionGroup childGroup = new ActionGroup("Child", parentGroup);
		childGroup.enabled(true);

		// Act
		boolean result = childGroup.enabled();

		// Assert
		assertTrue(result, "ActionGroup should be enabled when it and its parent are both enabled.");
	}

	@Test
	void testEnabledWhenSelfEnabledAndParentDisabled() {
		// Arrange
		ActionGroup parentGroup = new ActionGroup("Parent", null);
		parentGroup.enabled(false);

		ActionGroup childGroup = new ActionGroup("Child", parentGroup);
		childGroup.enabled(true);

		// Act
		boolean result = childGroup.enabled();

		// Assert
		assertFalse(result, "ActionGroup should not be enabled when its parent is disabled.");
	}

	@Test
	void testEnabledWhenSelfDisabledAndParentEnabled() {
		// Arrange
		ActionGroup parentGroup = new ActionGroup("Parent", null);
		parentGroup.enabled(true);

		ActionGroup childGroup = new ActionGroup("Child", parentGroup);
		childGroup.enabled(false);

		// Act
		boolean result = childGroup.enabled();

		// Assert
		assertFalse(result, "ActionGroup should not be enabled when it is disabled even if its parent is enabled.");
	}

	@Test
	void testEnabledWhenSelfDisabledAndParentDisabled() {
		// Arrange
		ActionGroup parentGroup = new ActionGroup("Parent", null);
		parentGroup.enabled(false);

		ActionGroup childGroup = new ActionGroup("Child", parentGroup);
		childGroup.enabled(false);

		// Act
		boolean result = childGroup.enabled();

		// Assert
		assertFalse(result, "ActionGroup should not be enabled if both it and its parent are disabled.");
	}
}

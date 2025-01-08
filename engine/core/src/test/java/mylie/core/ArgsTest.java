package mylie.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ArgsTest {

	/**
	 * Verifies functionality of the Args class and its defined method. The defined
	 * method checks if a specified key exists in the arguments and is set to
	 * "defined".
	 */

	@Test
	public void testDefined_KeyExistsAndDefined_ReturnsTrue() {
		// Arrange
		String[] arguments = {"-key", "-anotherKey"};
		Engine.Args args = new Engine.Args(arguments);

		// Act & Assert
		assertTrue(args.defined("key"));
		assertTrue(args.defined("anotherKey"));
	}

	@Test
	public void testArgs_InvalidArgumentFormat_ThrowsException() {
		// Arrange
		String[] arguments = {"invalidArgument"};

		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> new Engine.Args(arguments));
	}

	@Test
	public void testDefined_MultipleKeysIncludingOverride_ReturnsTrueForLastEntry() {
		// Arrange
		String[] arguments = {"-key", "-key", "value"};
		Engine.Args args = new Engine.Args(arguments);

		// Act & Assert
		assertTrue(args.defined("key")); // The last key entry should override previous definitions.
		assertEquals("value", args.value("key"));
	}

	@Test
	public void testDefined_NoArguments_ReturnsFalse() {
		// Arrange
		String[] arguments = {};
		Engine.Args args = new Engine.Args(arguments);

		// Act & Assert
		assertFalse(args.defined("key"));
	}

	@Test
	public void testDefined_KeyDoesNotExist_ReturnsFalse() {
		// Arrange
		String[] arguments = {"-key"};
		Engine.Args args = new Engine.Args(arguments);

		// Act & Assert
		assertFalse(args.defined("nonExistentKey"));
	}

	@Test
	public void testMultipleKeyValuePairs_CorrectHandlingAndRetrieval() {
		// Arrange
		String[] arguments = {"-key1", "value1", "-key2", "value2", "-key3", "value3"};
		Engine.Args args = new Engine.Args(arguments);

		// Act & Assert
		assertTrue(args.defined("key1"));
		assertTrue(args.defined("key2"));
		assertTrue(args.defined("key3"));

		assertEquals("value1", args.value("key1"));
		assertEquals("value2", args.value("key2"));
		assertEquals("value3", args.value("key3"));
	}

	@Test
	public void testDuplicateKey_OverridesPreviousValue() {
		// Arrange
		String[] arguments = {"-key", "initialValue", "-key", "overriddenValue"};
		Engine.Args args = new Engine.Args(arguments);

		// Act & Assert
		assertTrue(args.defined("key"));
		assertEquals("overriddenValue", args.value("key"));
	}
}

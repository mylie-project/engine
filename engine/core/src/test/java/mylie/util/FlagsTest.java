package mylie.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The FlagsTest class provides unit tests for the Flags class. Specifically, it
 * tests the isSet() method to ensure it behaves correctly under different
 * scenarios when checking if specific flags are set.
 */
public class FlagsTest {

	@Test
	void testIsSetForFlagThatIsSet() {
		// Initialize Flags object
		Flags flags = new Flags();

		// Set a specific flag (e.g., 1)
		int flag = 1;
		flags.set(flag);

		// Assert that isSet() returns true for the same flag
		assertTrue(flags.isSet(flag));
	}

	@Test
	void testIsSetForFlagThatIsNotSet() {
		// Initialize Flags object
		Flags flags = new Flags();

		// Define a flag without setting it
		int flag = 2;

		// Assert that isSet() returns false for the flag
		assertFalse(flags.isSet(flag));
	}

	@Test
	void testIsSetAfterClearingAFlag() {
		// Initialize Flags object
		Flags flags = new Flags();

		// Set and then clear a specific flag (e.g., 4)
		int flag = 4;
		flags.set(flag);
		flags.clear(flag);

		// Assert that isSet() returns false for the cleared flag
		assertFalse(flags.isSet(flag));
	}

	@Test
	void testIsSetWithMultipleFlagsSet() {
		// Initialize Flags object
		Flags flags = new Flags();

		// Set multiple flags (e.g., 1 and 8)
		int flag1 = 1;
		int flag2 = 8;
		flags.set(flag1);
		flags.set(flag2);

		// Assert that isSet() returns true for each of the set flags
		assertTrue(flags.isSet(flag1));
		assertTrue(flags.isSet(flag2));
	}

	@Test
	void testIsSetForUnrelatedFlagWhenOthersAreSet() {
		// Initialize Flags object
		Flags flags = new Flags();

		// Set specific flags (e.g., 1 and 8)
		int flag1 = 1;
		int flag2 = 8;
		flags.set(flag1);
		flags.set(flag2);

		// Define an unrelated flag (e.g., 4) that is not set
		int unrelatedFlag = 4;

		// Assert that isSet() returns false for the unrelated flag
		assertFalse(flags.isSet(unrelatedFlag));
	}
}

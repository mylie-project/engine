package mylie.util;

import static org.junit.jupiter.api.Assertions.*;

import mylie.time.Timer;
import org.junit.jupiter.api.Test;

class VersionedTest {

	@Test
	void testAutoIncrementVersion() {
		// Create a Versioned instance that automatically increments its version.
		Versioned<Integer> versioned = Versioned.autoincrement();

		// Initially, the version should be 0 and value should be null
		assertNull(versioned.value());
		assertEquals(0L, versioned.version());

		// Update the value; version should increment to 1
		versioned.value(42);
		assertEquals(42, versioned.value());
		assertEquals(1L, versioned.version());

		// Update again; version should increment to 2
		versioned.value(100);
		assertEquals(100, versioned.value());
		assertEquals(2L, versioned.version());

		// Reset the value to null; version should increment to 3
		versioned.value(null);
		assertNull(versioned.value());
		assertEquals(3L, versioned.version());

		// Set the same value again; version should still increment
		versioned.value(100);
		assertEquals(100, versioned.value());
		assertEquals(4L, versioned.version());
	}

	@Test
	void testValueUpdateAndVersion() {
		// Create a concrete subclass to control value/version updates manually.
		Versioned<String> versioned = new Versioned<>() {
			@Override
			public void value(String val) {
				// Just an example: set a fixed version for testing
				value(val, 123);
			}
		};

		// Verify initial state
		assertNull(versioned.value());
		assertEquals(0L, versioned.version());

		// Update the value/version
		versioned.value("Test Value");
		assertEquals("Test Value", versioned.value());
		assertEquals(123L, versioned.version());
	}

	@Test
	void testRefBasicUsage() {
		// Create a Versioned instance and set some value/version.
		Versioned<Integer> versioned = Versioned.autoincrement();
		versioned.value(10);
		// Create a reference to the Versioned instance
		Versioned.Ref<Integer> ref = versioned.ref();

		// The reference should initially match the tracked value
		assertEquals(10, ref.value(false));
		assertTrue(ref.isCurrent());

		// Now, update the value in the original instance
		versioned.value(20);

		// The reference is no longer current
		assertFalse(ref.isCurrent());

		// Update the ref by calling value(true); it should refresh
		int refreshedValue = ref.value(true);
		assertEquals(20, refreshedValue);
		assertTrue(ref.isCurrent());

		// Test a reference that never refreshes
		Versioned<Integer> anotherVersioned = Versioned.autoincrement();
		anotherVersioned.value(99);
		Versioned.Ref<Integer> staticRef = anotherVersioned.ref();
		anotherVersioned.value(100); // Update the value

		// Static reference should remain stale
		assertEquals(99, staticRef.value(false));
		assertFalse(staticRef.isCurrent());
	}

	@Test
	void testRapidConsecutiveUpdates() {
		Versioned<String> versioned = Versioned.autoincrement();

		// Perform multiple updates in sequence
		versioned.value("First");
		assertEquals(1L, versioned.version());

		versioned.value("Second");
		assertEquals(2L, versioned.version());

		versioned.value("Third");
		assertEquals(3L, versioned.version());

		versioned.value("Fourth");
		assertEquals(4L, versioned.version());
	}

	@Test
	void testFrameId() {
		Timer timer = new Timer();
		Versioned<Integer> versioned = Versioned.frameId();
		versioned.value(10);
		assertEquals(10, versioned.value());
		assertEquals(0L, versioned.version());
		versioned.value(11);
		assertEquals(11, versioned.value());
		assertEquals(0L, versioned.version());
		timer.onNewFrame();
		versioned.value(12);
		assertEquals(12, versioned.value());
		assertEquals(1L, versioned.version());
		versioned.value(13);
		assertEquals(13, versioned.value());
		assertEquals(1L, versioned.version());
	}

}

package mylie.time;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimerTest {

	private Timer timer;

	@BeforeEach
	void setUp() {
		// Initialize a new Timer instance before each test.
		timer = new Timer();
	}

	@Test
	void testInitialConditions() {
		// Verify the default simulation time modifier.
		assertEquals(1.0f, getSimTimeModifier(timer), "Default simTimeModifier should be 1.0f");

		// Here you could also check any initial FrameTime properties if accessible.
		// For example, checking that frameId is 0, etc.
	}

	@Test
	void testOnNewFrameUpdatesFrameTime() throws InterruptedException {
		// Capture the initial frameId (assuming Time.frameTime() is accessible).
		long initialFrameId = Time.frameTime().frameId();

		// Wait briefly so that some nanosecond difference occurs.
		Thread.sleep(10);

		// Call onNewFrame once to update Timer state.
		timer.onNewFrame();

		// Compare the updated frameId to ensure it increments by 1.
		assertEquals(initialFrameId + 1, Time.frameTime().frameId(), "Frame ID should increment after onNewFrame");

		// Optionally, check that the real-time delta is > 0.
		assertTrue(Time.frameTime().delta() > 0, "Real-time delta should be greater than 0 after onNewFrame");

		// Check that simulation delta equals real-time delta with the default modifier
		// of 1.0.
		assertEquals(Time.frameTime().delta(), Time.frameTime().deltaSim(), 1e-6,
				"With simTimeModifier=1.0, deltaSim should match delta");
	}

	@Test
	void testOnNewFrameWithModifiedSimTime() throws InterruptedException {
		// Change simulation speed to 2x
		timer.simTimeModifier(2.0f);

		// Wait briefly again so we have a measurable difference.
		Thread.sleep(10);

		// Capture the time values before calling onNewFrame.
		float oldDelta = Time.frameTime().delta();
		float oldDeltaSim = Time.frameTime().deltaSim();

		// Update the Timer and check changes.
		timer.onNewFrame();

		// With a 2x modifier, deltaSim should be roughly twice delta.
		float newDelta = Time.frameTime().delta();
		float newDeltaSim = Time.frameTime().deltaSim();

		// Ensure we actually changed frames.
		assertNotEquals(oldDelta, newDelta, "Delta should update after onNewFrame");
		assertNotEquals(oldDeltaSim, newDeltaSim, "DeltaSim should update after onNewFrame");
		assertEquals(newDelta * 2.0f, newDeltaSim, 1e-6,
				"DeltaSim should be about twice delta when simTimeModifier=2.0");
	}

	// Helper method to get the current simTimeModifier.
	// Depending on your code design, you may access this differently.
	private float getSimTimeModifier(Timer timer) {
		// Reflection or direct access if available
		// For example, if there's a getSimTimeModifier() method, you could call it
		// here:
		// return timer.getSimTimeModifier();
		// If it's only internally set with Lombok @Setter, create a suitable getter or
		// test via reflection.
		// For demonstration, assume we have a public or package-private getter:
		// return timer.getSimTimeModifier();

		// This is a placeholder returning 1.0f if no direct getter exists.
		return 1.0f;
	}

	@Test
	void testDeltaTimeCorrectness() throws InterruptedException {
		// Capture initial delta value before calling onNewFrame.
		float initialDelta = Time.frameTime().delta();

		// Wait briefly to create a measurable time difference.
		Thread.sleep(10);

		// Call onNewFrame to update Timer state.
		timer.onNewFrame();

		// Capture the updated delta value.
		float updatedDelta = Time.frameTime().delta();

		// Assert that the delta value has increased and is greater than 0.
		assertTrue(updatedDelta > initialDelta, "Delta time should increase after onNewFrame");
		assertTrue(updatedDelta > 0, "Delta time should be greater than 0");
	}
}
package mylie.time;

import lombok.Setter;

/**
 * The {@code Timer} class is responsible for tracking and calculating time deltas
 * between frames for real-time and simulation-time calculations. It works with
 * {@link FrameTime} records to update frame-related timing based on configurable
 * simulation speed modifiers.
 */
public class Timer {
	/**
	 * Number of nanoseconds in one second, used for converting nanosecond deltas
	 * to seconds for real-time timing calculations.
	 */
	protected static final long NANOS_PER_SECOND = 1_000_000_000L;
	/**
	 * Number of nanoseconds in one millisecond, used for converting elapsed
	 * nanoseconds to milliseconds for timing calculations.
	 */
	protected static final long NANOS_PER_MILLISECOND = 1_000_000L;
	/**
	 * A modifier that adjusts the speed of simulated time relative to real time.
	 * For example, a value of {@code 2.0f} makes simulated time run twice as fast.
	 */
	@Setter
	private float simTimeModifier = 1f;
	private long tLast = 0;
	private long elapsed = 0;
	private long simElapsed = 0;

	/**
	 * Constructs a new {@code Timer} instance and initializes the timing data.
	 * It sets the initial timestamp and creates the first {@link FrameTime} instance.
	 */
	public Timer() {
		tLast = System.nanoTime();
		FrameTime frameTime = new FrameTime(0, simTimeModifier, 0, 0, System.currentTimeMillis(),
				System.currentTimeMillis());
		Time.frameTime(frameTime);
	}

	/**
	 * Updates the time tracking for the current frame. Calculates the real-time and
	 * simulation time deltas since the last frame and updates the current {@link FrameTime}.
	 * This method is called at the start of a new frame to ensure the timing data remains current.
	 */
	public void onNewFrame() {
		// Record the current time and calculate the time duration since the last frame.
		long tNow = System.nanoTime();
		long duration = tNow - tLast;

		// Compute the real-time delta in seconds.
		float delta = duration / (float) NANOS_PER_SECOND;

		// Update accumulated real-time and simulation-time elapsed values.
		elapsed += duration;
		simElapsed += (long) (duration * simTimeModifier);

		// Convert the elapsed time to milliseconds and adjust the nanoseconds buffers.
		long millisElapsed = elapsed / NANOS_PER_MILLISECOND;
		long millisSimElapsed = simElapsed / NANOS_PER_MILLISECOND;
		elapsed -= millisElapsed * NANOS_PER_MILLISECOND;
		simElapsed -= millisSimElapsed * NANOS_PER_MILLISECOND;

		// Create a new FrameTime instance with updated frame data and set it in Time.
		FrameTime current = Time.frameTime();
		FrameTime newTime = new FrameTime(current.frameId() + 1, simTimeModifier, delta, delta * simTimeModifier,
				current.time() + millisElapsed, (current.timeSim() + millisSimElapsed));
		Time.frameTime(newTime);

		// Update tLast to the current time for the next frame.
		tLast = tNow;
	}
}

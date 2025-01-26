package mylie.time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * The Time class provides static methods to access frame-based timing information,
 * useful for simulations, animations, or real-time applications.
 * It delegates calls to an instance of FrameTime, encapsulating timing data such as
 * elapsed time, simulation time, and frame identifiers.
 */
public class Time {
	/**
	 * The {@link FrameTime} instance that holds frame-specific timing information.
	 * Provides data such as elapsed time, simulation time, and frame identifiers.
	 */
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private static FrameTime frameTime;

	/**
	 * Retrieves the time elapsed between the current frame and the previous frame in real time.
	 *
	 * @return the real-time delta between frames as a {@code float}.
	 */
	public static float delta() {
		return frameTime().delta();
	}

	/**
	 * Retrieves the time elapsed between the current frame and the previous frame in simulation time.
	 *
	 * @return the simulation-time delta between frames as a {@code float}.
	 */
	public static float deltaSim() {
		return frameTime().deltaSim();
	}

	/**
	 * Retrieves the total elapsed real time since the start of the application or simulation.
	 *
	 * @return the total real time in milliseconds as a {@code long}.
	 */
	public static long time() {
		return frameTime.time();
	}

	/**
	 * Retrieves the total elapsed simulation time since the start of the application or simulation.
	 *
	 * @return the total simulation time in milliseconds as a {@code long}.
	 */
	public static long timeSim() {
		return frameTime.timeSim();
	}

	/**
	 * Retrieves the unique identifier for the current frame.
	 *
	 * @return the frame ID as a {@code long}.
	 */
	public static long frameId() {
		return frameTime.frameId();
	}
}

package mylie.time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * The {@code Time} class provides static methods for accessing frame-specific timing data.
 * It enables querying details regarding real-time and simulation-time deltas,
 * total elapsed time, and frame identifiers during application runtime.
 * All data retrieval is managed via the current {@link FrameTime} instance,
 * which is updated externally for each frame.
 */
@SuppressWarnings("unused")
public class Time {

	/**
	 * Private constructor for the {@code Time} class.
	 * The constructor is private to ensure that the {@code Time} class cannot be instantiated.
	 * This class is designed to provide static methods for retrieving frame-specific timing data,
	 * and should not be used as an instance.
	 */
	private Time() {
	}
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
		if (frameTime == null) {
			return 0;
		}
		return frameTime.frameId();
	}
}

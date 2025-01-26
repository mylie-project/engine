package mylie.util;

import lombok.Getter;
import mylie.time.Time;

/**
 * An abstract generic class used to represent a versioned object that retains a value and its version number.
 * Designed to be extended by other classes to provide specific functionality while maintaining version tracking.
 *
 * @param <T> the type of the value stored in the versioned object
 */
@Getter
public abstract class Versioned<T> {
	/**
	 * Default constructor for the Versioned class.
	 * This constructor initializes an instance of the Versioned class, intended to be extended by subclasses
	 * for version tracking of a specific value type.
	 */
	Versioned() {

	}
	/**
	 * The value stored in the versioned object.
	 */
	private T value;

	/**
	 * The version number of the stored value, used to track changes in the versioned object.
	 */
	private long version;

	/**
	 * Provides a synchronized reference to the {@link Versioned} object,
	 * allowing for safe access and updates to its value and version.
	 *
	 * @return a {@link Ref} object for the current {@link Versioned} instance
	 */
	public Ref<T> ref() {
		return new Ref<>(this);
	}

	/**
	 * Updates the stored value and its corresponding version number for the {@link Versioned} object.
	 *
	 * @param value   the new value to be stored
	 * @param version the new version number associated with the value
	 */
	public void value(T value, long version) {
		this.value = value;
		this.version = version;
	}

	/**
	 * Abstract method for updating the value stored in the {@link Versioned} object.
	 * Subclasses must define the specific behavior for setting the value.
	 *
	 * @param value the new value to be stored
	 */
	public abstract void value(T value);

	/**
	 * Creates a {@link Versioned} instance with an auto-incrementing version number.
	 * Each time the value is updated, the version is incremented by 1.
	 *
	 * @param <T> the type of the value stored in the {@link Versioned} object
	 * @return a {@link Versioned} object with auto-incrementing version numbers
	 */
	public static <T> Versioned<T> autoincrement() {
		return new Versioned<T>() {
			@Override
			public void value(T value) {
				value(value, version() + 1);
			}
		};
	}

	/**
	 * Creates a {@link Versioned} instance that uses the current frame ID
	 * as the version for the stored value. The frame ID is retrieved from the
	 * {@link Time} class.
	 *
	 * @param <T> the type of the value stored in the {@link Versioned} object
	 * @return a {@link Versioned} object using the frame ID as the version
	 */
	public static <T> Versioned<T> frameId() {
		return new Versioned<>() {
			@Override
			public void value(T value) {
				value(value, Time.frameId());
			}
		};
	}

	/**
	 * A helper class for maintaining synchronized access to the {@link Versioned} object.
	 * It ensures values and their versions remain consistent, allowing optional updates.
	 *
	 * @param <T> the type of the value tracked by this reference
	 */
	public static class Ref<T> {
		/**
		 * Reference to the {@link Versioned} object for tracking its value and version.
		 */
		private final Versioned<T> versioned;

		/**
		 * The locally cached value corresponding to the tracked {@link Versioned} object.
		 */
		private T value;

		/**
		 * The locally cached version number matching the value of the {@link Versioned} object.
		 */
		private long version;

		/**
		 * Constructor for creating a reference to a {@link Versioned} object.
		 *
		 * @param versioned the {@link Versioned} object to be tracked
		 */
		Ref(Versioned<T> versioned) {
			this.versioned = versioned;
			this.value = versioned.value();
			this.version = versioned.version();
		}

		/**
		 * Retrieves the value of the tracked {@link Versioned} object, optionally updating it if changes exist.
		 *
		 * @param update whether to update the locally cached value and version if they are outdated
		 * @return the locally cached or updated value
		 */
		T value(boolean update) {
			if (update) {
				if (!isCurrent()) {
					this.version = versioned.version();
					this.value = versioned.value();
				}
			}
			return value;
		}

		/**
		 * Checks if the locally cached version matches the version of the tracked {@link Versioned} object.
		 *
		 * @return {@code true} if the cached version is not up-to-date with the tracked object; {@code false} otherwise
		 */
		boolean isCurrent() {
			return version == versioned.version();
		}
	}
}

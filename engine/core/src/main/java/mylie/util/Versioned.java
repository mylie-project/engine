package mylie.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A generic class that represents a value with an associated version. The
 * version tracks changes to the value, enabling updates and comparisons of
 * versions.
 *
 * @param <T>
 *            The type of the value being stored and tracked.
 */
@Setter(AccessLevel.PROTECTED)
@Getter
public class Versioned<T> {
	private T value;
	private long version;

	/// Updates the value and associated version of the instance.
	///
	/// @param newValue the new value to be set.
	/// @param version the new version number to be set.
	public void value(T newValue, long version) {
		this.value = newValue;
		this.version = version;
	}

	/// Updates the value of this Versioned object and increments its version.
	///
	/// @param newValue the new value to be set.
	public void value(T newValue) {
		this.value = newValue;
		this.version++;
	}

	/// Creates and returns a snapshot reference to the current state of this
	/// Versioned object.
	/// The reference includes the current value and version of the object.
	///
	/// @return a new Reference object encapsulating the current value and version
	/// of this Versioned object.
	public final Reference<T> reference() {
		return new Reference<>(this, value, version);
	}

	/// Represents a reference to a value and its version, originating from a
	/// [Versioned] object.
	/// This class provides mechanisms to check if the reference value has changed
	/// and to update
	/// the reference to match the current value and version of the source
	/// [Versioned] object.
	///
	/// @param <T> The type of the value being referenced.
	@Getter
	@AllArgsConstructor
	public static final class Reference<T> {
		@Getter(AccessLevel.NONE)
		final Versioned<T> versioned;
		T value;
		long version;

		/// Determines whether the version of the referenced value has changed compared
		/// to the
		/// current version of the source `Versioned` object.
		///
		/// @return `true` if the version of the referenced value is different from the
		/// current version of the `Versioned` object; `false` otherwise.
		public boolean changed() {
			return versioned.version() != version;
		}

		/// Updates the reference value and version to match the current state of the
		/// associated
		/// `Versioned` object if the version has changed.
		///
		/// @return true if the reference was updated (i.e., the version had changed);
		/// false otherwise.
		public boolean update() {
			boolean changed = changed();
			if (changed) {
				this.value = versioned.value();
				this.version = versioned.version();
			}
			return changed;
		}

		/// Retrieves the current value referenced by this object. Optionally updates
		/// the reference to ensure it reflects the most recent state of the associated
		/// `Versioned` object if the version has changed.
		///
		/// @param update a boolean flag indicating whether to update the reference
		/// to match the latest value and version of the associated `Versioned` object.
		/// @return the value currently referenced by this object, or the updated value
		/// if
		/// the reference was updated.
		public T value(boolean update) {
			if (update) {
				update();
			}
			return value;
		}
	}
}

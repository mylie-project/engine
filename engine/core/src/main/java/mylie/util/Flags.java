package mylie.util;

/// The Flags class provides a simple implementation for managing multiple flags
/// using bitwise operations. Flags are stored as integers and can be set, checked,
/// or cleared individually.
public class Flags {
	int flags;

	/// Sets the specified flag in the current flags using a bitwise OR operation.
	///
	/// @param flag the flag to be set. Each flag should be represented as a unique
	/// bit
	/// to avoid conflicts with other flags.
	public void set(int flag) {
		flags |= flag;
	}

	/// Checks if the specified flag is set in the current flags.
	///
	/// @param flag the flag to check. Each flag should be represented as a unique
	/// bit
	/// to avoid conflicts with other flags.
	/// @return true if the specified flag is set, false otherwise.
	public boolean isSet(int flag) {
		return (flags & flag) != 0;
	}

	/// Clears the specified flag from the current flags using a bitwise AND
	/// operation
	/// with the complement of the flag. This allows the flag to be unset while
	/// leaving other flags unchanged.
	///
	/// @param flag the flag to be cleared. Each flag should be represented as
	/// a unique bit to avoid conflicts with other flags.
	public void clear(int flag) {
		flags &= ~flag;
	}
}

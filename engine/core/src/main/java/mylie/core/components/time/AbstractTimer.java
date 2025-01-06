package mylie.core.components.time;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.TimerSettings;
import mylie.core.component.Components;
import mylie.util.Versioned;

/// AbstractTimer is an abstract class that serves as a base implementation for timers
/// within the system. It manages a versioned state of time and provides mechanisms
/// for time updates and transformation. It extends the `Components.Core` class and
/// implements the `Timer` interface.
/// This class encapsulates the following functionality:
/// 1. Maintains a versioned time state using the `Versioned<Time>` object.
/// 2. Provides a method to update the time state and retrieve the current time as a `Time` object.
/// 3. Offers utility methods for obtaining both the time and a referential handle to its versioned state.
/// 4. Enforces the implementation of a specialized method, `getTime`, which subclasses must define
///    to generate the appropriate `Time` object corresponding to the timer's logic.
/// Subclasses of AbstractTimer are expected to:
/// - Define their custom time behavior by implementing the `getTime` method.
/// - Utilize the `settings` provided to control timer-specific parameters.
/// Core Attributes:
/// - `time`: Tracks the current versioned time state.
/// - `settings`: Configures timer-specific settings encapsulated in `TimerSettings`.
/// - `count`: Internal counter for extended implementations.
/// Methods:
/// - `update(long version)`: Updates the timer state based on a given version and returns the updated `Time` object.
/// - `getTime(long version)`: Abstract method to be implemented by subclasses to return the current time state.
/// - `time()`: Retrieves the current `Time` object.
/// - `timeRef()`: Retrieves a versioned reference to the current `Time` object.
@Slf4j
public abstract class AbstractTimer extends Components.Core implements Timer{
    final Versioned<Time> time = new Versioned<>();

    @Getter(AccessLevel.PROTECTED)
    private final TimerSettings settings;
    private long count;

    protected AbstractTimer(TimerSettings settings) {
        this.settings = settings;
    }


    /// Updates the state of the timer to reflect the given version and returns the updated time object.
    /// This method calls a subclass-implemented `getTime` function to compute the updated `Time` state
    /// based on the provided version, sets the new `Time` value, and increments its version.
    ///
    /// @param version The version number representing the current state of the timer.
    /// @return The updated `Time` object containing the updated time state.
    public Time update(long version) {
        time.value(getTime(version));
        return time.value();
    }

    /// Responsible for generating a `Time` object representing the state of time
    /// based on the provided version. The implementation of this method defines
    /// how time is computed or updated by subclasses.
    ///
    /// @param version The version number representing the current state of the timer.
    /// @return A `Time` object that encapsulates the computed time state, including
    ///         its version, delta, and modified delta.
    protected abstract Time getTime(long version);

    /// Retrieves the current time state managed by the Timer.
    ///
    /// @return the current Time instance representing the Timer's state,
    ///         containing information such as elapsed time and its version.
    @Override
    public Time time() {
        return time.value();
    }

    /// Provides a reference to the current time state managed by the Timer.
    /// The reference includes both the current time instance and its version,
    /// enabling version-tracked access and consistency checks.
    ///
    /// @return a Versioned.Reference object containing the current Time instance
    ///         along with its associated version.
    @Override
    public Versioned.Reference<Time> timeRef() {
        return time.reference();
    }
}

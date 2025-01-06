package mylie.core.components.time;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import mylie.core.component.Components;
import mylie.util.Versioned;

/// The Timer interface extends the CoreComponent interface and provides methods for obtaining
/// time-related data and versioned references to this data. Implementations of Timer are
/// designed to handle time updates and keep track of a time state while maintaining version
/// consistency.
public interface Timer extends Components.CoreComponent {
    /// Retrieves the current time state managed by the Timer. The time state contains
    /// information such as version, delta, and modified delta values, which represent
    /// the elapsed time and its transformations.
    ///
    /// @return the current instance of the Time object representing the Timer's state.
    Time time();

    /// Retrieves a reference to the current time state managed by the Timer.
    /// The reference includes the current Time object and its associated version,
    /// allowing for version-tracking and consistency checks.
    ///
    /// @return a Versioned.Reference object encapsulating the current Time instance
    ///         and its version.
    Versioned.Reference<Time> timeRef();


    /// Represents a state of time with associated versioning and transformation data.
    /// This class is primarily used within the context of time management systems
    /// to hold details about time deltas and their modifications.
    /// The fields in this class include:
    /// - A `version` for maintaining version-tracked consistency of the time state.
    /// - A `delta` value that represents the elapsed time since the last update.
    /// - A `deltaMod` value which stores a transformed version of the `delta`,
    ///   such as a scaled or weighted time measure.
    /// This class is typically utilized or extended by components such as timers
    /// or time-managers that operate on or process time-related information.
    /// Access to its fields is protected to allow controlled modifications
    /// within subclasses or frameworks managing time states.
    @Setter(AccessLevel.PROTECTED)
    @Getter
    class Time {
        long version;
        double delta;
        double deltaMod;
    }
}

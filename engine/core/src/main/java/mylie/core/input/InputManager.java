package mylie.core.input;

import mylie.core.component.Components;

import java.util.List;

public interface InputManager extends Components.AppComponent {

    /// Prevents input events from the specified device under the given provider
    /// from being processed further by adding the device to an ignore list.
    ///
    /// @param provider The class type of the input provider whose associated
    ///                 input device should be ignored. Must implement the
    ///                 [Input.Provider] interface.
    /// @param device   The input device instance to be ignored for the specified
    ///                 provider.
    void ignoreInputFrom(Class<? extends Input.Provider> provider, InputDevice<?> device);

    /// Removes the specified input device from the ignore list for the provided input provider class.
    /// If the input device is in the ignore list for the given provider, it will no longer be ignored.
    ///
    /// @param provider the class of the input provider whose ignore list should be modified
    /// @param device   the input device to remove from the ignore list
    void unignoreInputFrom(Class<? extends Input.Provider> provider, InputDevice<?> device);

    /// Registers an input provider with the input system. The provider will be
    /// queried for input events during the update cycles of the InputSystem.
    ///
    /// @param provider the input provider instance to be registered.
    /// @return the current instance of the InputSystem, to allow method chaining.
    InputSystem registerInputProvider(Input.Provider provider);

    /// Unregisters an input provider from the input system.
    /// Removing the provider ensures that it will no longer be queried for input
    /// events during the update cycles of the input system.
    ///
    /// @param provider the input provider to be unregistered from the input system.
    /// @return the current instance of the InputSystem, to allow method chaining.
    InputSystem unregisterInputProvider(Input.Provider provider);

    /// Retrieves the first input device instance of the specified type from the
    /// list of input devices.
    ///
    /// @param <T> the type of the input device. Must extend [InputDevice].
    /// @param type the [Class] instance representing the type of input device to
    /// retrieve.
    /// @return the first input device of the specified type if found, or `null` if
    /// no device of the specified type exists.
    <T extends InputDevice<T>> T device(Class<T> type);

    /// Retrieves a list of input devices of a specified type from the collection of
    /// input devices.
    ///
    /// @param <T> the type of input devices to retrieve. Must extend `InputDevice`.
    /// @param type the `Class` instance representing the type of input devices to
    /// filter and retrieve.
    /// @return a list of input devices of the specified type. If no devices match,
    /// an empty list is returned.
    <T extends InputDevice<T>> List<InputDevice<?>> devices(Class<T> type);
}

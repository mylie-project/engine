package mylie.core.input;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Async;
import mylie.core.async.Result;
import mylie.core.component.Components;
import mylie.core.component.Stages;
import mylie.core.input.devices.Gamepad;
import mylie.core.input.devices.Keyboard;
import mylie.core.input.devices.Mouse;

@Slf4j
public class InputSystem extends Components.Core implements InputManager, Components.Updateable, Components.AddRemove {
	private final List<InputDevice<?>> inputDevices = new CopyOnWriteArrayList<>();
	private final List<Input.Provider> inputProviders = new CopyOnWriteArrayList<>();
	private final Map<Class<? extends Input.Provider>, Set<InputDevice<?>>> ignoreList = new HashMap<>();

	public InputSystem() {
		inputDevices.add(new Keyboard("Primary Keyboard"));
		inputDevices.add(new Mouse("Primary Mouse"));
		for (int i = 0; i < 4; i++) {
			inputDevices.add(new Gamepad(String.format("Gamepad %d", i), i));
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void onUpdate() {
		Collection<Result<Collection<Input.Event<?, ?, ?>>>> results = Async.async(inputProviders,
				Input.Provider::events);
		for (Result<Collection<Input.Event<?, ?, ?>>> result : results) {
			Collection<Input.Event<?, ?, ?>> events = result.result();
			for (Input.Event event : events) {
				if (ignoreEvent(event)) {
					event.device().value(event.type(), event.value());
				}
			}
		}
		log.trace("Updating input system");
	}

	/// Prevents input events from the specified device under the given provider
	/// from being processed further by adding the device to an ignore list.
	///
	/// @param provider The class type of the input provider whose associated
	/// input device should be ignored. Must implement the
	/// [Input.Provider] interface.
	/// @param device The input device instance to be ignored for the specified
	/// provider.
	@Override
	public void ignoreInputFrom(Class<? extends Input.Provider> provider, InputDevice<?> device) {
		Set<InputDevice<?>> devices = ignoreList.computeIfAbsent(provider, _ -> new HashSet<>());
		devices.add(device);
	}

	/// Removes the specified input device from the ignore list for the provided
	/// input provider class.
	/// If the input device is in the ignore list for the given provider, it will no
	/// longer be ignored.
	///
	/// @param provider the class of the input provider whose ignore list should be
	/// modified
	/// @param device the input device to remove from the ignore list
	@Override
	public void unignoreInputFrom(Class<? extends Input.Provider> provider, InputDevice<?> device) {
		Set<InputDevice<?>> devices = ignoreList.computeIfAbsent(provider, _ -> new HashSet<>());
		devices.remove(device);
	}

	/// Determines whether a given input event should be ignored based on its
	/// provider and device.
	///
	/// @param event The input event to evaluate. Contains information about the
	/// provider, device, type, and value of the input.
	/// @return true if the event should be ignored; false otherwise.
	private boolean ignoreEvent(Input.Event<?, ?, ?> event) {
		Set<InputDevice<?>> devices = ignoreList.get(event.provider());
		return devices != null && devices.contains(event.device());
	}

	/// Registers an input provider with the input system. The provider will be
	/// queried for input events during the update cycles of the InputSystem.
	///
	/// @param provider the input provider instance to be registered.
	/// @return the current instance of the InputSystem, to allow method chaining.
	@Override
	public InputSystem registerInputProvider(Input.Provider provider) {
		inputProviders.add(provider);
		return this;
	}

	/// Unregisters an input provider from the input system.
	/// Removing the provider ensures that it will no longer be queried for input
	/// events during the update cycles of the input system.
	///
	/// @param provider the input provider to be unregistered from the input system.
	/// @return the current instance of the InputSystem, to allow method chaining.
	@Override
	public InputSystem unregisterInputProvider(Input.Provider provider) {
		inputProviders.remove(provider);
		return this;
	}

	/// Retrieves the first input device instance of the specified type from the
	/// list of input devices.
	///
	/// @param <T> the type of the input device. Must extend [InputDevice].
	/// @param type the [Class] instance representing the type of input device to
	/// retrieve.
	/// @return the first input device of the specified type if found, or `null` if
	/// no device of the specified type exists.
	@Override
	public <T extends InputDevice<T>> T device(Class<T> type) {
		return inputDevices.stream().filter(type::isInstance).findFirst().map(type::cast).orElse(null);
	}

	/// Retrieves a list of input devices of a specified type from the collection of
	/// input devices.
	///
	/// @param <T> the type of input devices to retrieve. Must extend `InputDevice`.
	/// @param type the `Class` instance representing the type of input devices to
	/// filter and retrieve.
	/// @return a list of input devices of the specified type. If no devices match,
	/// an empty list is returned.
	@Override
	public <T extends InputDevice<T>> List<InputDevice<?>> devices(Class<T> type) {
		return inputDevices.stream().filter(type::isInstance).toList();
	}

	@Override
	public void onAdded() {
		Stages.PreUpdateLogic.updateDependency(this::update);
	}

	@Override
	public void onRemoved() {
		Stages.PreUpdateLogic.removeDependency(this::update);
	}
}

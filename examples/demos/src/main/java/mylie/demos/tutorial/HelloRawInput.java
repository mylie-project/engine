package mylie.demos.tutorial;

import lombok.extern.slf4j.Slf4j;
import mylie.core.component.Components;
import mylie.core.input.Input;
import mylie.core.input.InputListeners;
import mylie.core.input.InputManager;
import mylie.demos.Demo;

@Slf4j
public class HelloRawInput extends Demo implements InputListeners.Raw, Components.AddRemove {

	@Override
	public void onInput(Input.Event<?, ?, ?> event) {
		log.info("Received input event: {}", event);
	}

	@Override
	public void onAdded() {
		component(InputManager.class).registerInputListener(this);
	}

	@Override
	public void onRemoved() {
		component(InputManager.class).unregisterInputListener(this);
	}
}

package mylie.demos.tutorial;

import lombok.extern.slf4j.Slf4j;
import mylie.core.action.Action;
import mylie.core.action.Actions;
import mylie.core.component.Components;
import mylie.core.input.Input;
import mylie.core.input.InputListeners;
import mylie.core.input.InputManager;
import mylie.demos.Demo;

@Slf4j
public class HelloRawInput extends Demo implements InputListeners.Raw, Components.AddRemove {
	private Action<Boolean> exitApplication = Actions.call(null, b -> b,
			() -> System.out.println("Exiting application..."));
	@Override
	public void onInput(Input.Event<?, ?, ?> event) {
		log.info("Received input event: {}", event);
	}

	@Override
	public void onAdded() {
		component(InputManager.class).registerInputListener(this);
		InputManager component = component(InputManager.class);
	}

	@Override
	public void onRemoved() {
		component(InputManager.class).unregisterInputListener(this);
	}
}

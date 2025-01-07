package mylie.core.input;

import lombok.extern.slf4j.Slf4j;
import mylie.core.component.Components;
import mylie.core.component.Stages;

@Slf4j
public class InputSystem extends Components.Core
		implements
			InputManager,
			Components.Initializable,
			Components.Updateable,
			Components.AddRemove {

	@Override
	public void onInitialize() {

	}

	@Override
	public void onShutdown() {

	}

	@Override
	public void onUpdate() {
		log.trace("Updating input system");
	}

	@Override
	public void onAdded() {
		Stages.PreUpdateLogic.updateDependency(this::update);
	}

	@Override
	public void onRemoved() {

	}
}

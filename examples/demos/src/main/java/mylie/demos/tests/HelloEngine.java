package mylie.demos.tests;

import lombok.extern.slf4j.Slf4j;
import mylie.core.component.Components;
import mylie.demos.Demo;

@Slf4j
public class HelloEngine extends Demo implements Components.Updateable, Components.Initializable {
	@Override
	public void onUpdate() {

	}

	@Override
	public void onInitialize() {
		log.info("Hello Engine!");
	}

	@Override
	public void onShutdown() {

	}
}

package mylie.demos;

import mylie.core.application.Application;
import mylie.core.components.time.Timer;

public class DemoApplication extends Application {
	@Override
	protected void onInitialize() {
		component(new DemoSelector());
	}

	@Override
	protected void onUpdate(Timer.Time time) {

	}

	@Override
	protected void onShutdown() {

	}
}

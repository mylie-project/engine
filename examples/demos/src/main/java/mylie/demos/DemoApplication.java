package mylie.demos;

import mylie.core.application.BaseApplication;
import mylie.core.components.time.Timer;

public class DemoApplication extends BaseApplication {
	@Override
	protected void onInitialize() {
		super.onInitialize();
		component(new DemoSelector());
	}

	@Override
	protected void onUpdate(Timer.Time time) {

	}

	@Override
	protected void onShutdown() {

	}
}

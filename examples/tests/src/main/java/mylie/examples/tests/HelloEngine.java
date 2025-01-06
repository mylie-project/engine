package mylie.examples.tests;

import lombok.extern.slf4j.Slf4j;
import mylie.core.Engine;
import mylie.core.EngineConfiguration;
import mylie.core.EngineManager;
import mylie.core.application.Application;
import mylie.core.components.time.Timer;
import mylie.platform.Desktop;
@Slf4j
public class HelloEngine extends Application {
	public static void main(String[] args) {
		Desktop desktop = new Desktop();
		EngineConfiguration engineConfiguration = desktop.initialize();
		engineConfiguration.option(EngineConfiguration.Application, new HelloEngine());
		engineConfiguration.startEngine();
	}

	@Override
	protected void onInitialize() {
		log.info("onInitialize");
	}

	@Override
	protected void onUpdate(Timer.Time time) {
		log.info("onUpdate");
		if (time.version() == 10) {
			component(EngineManager.class).shutdown(Engine.ShutdownReason.ok("User requested shutdown"));
		}
	}

	@Override
	protected void onShutdown() {
		log.info("onShutdown");
	}
}

package mylie.examples.tests;

import lombok.extern.slf4j.Slf4j;
import mylie.core.Engine;
import mylie.core.EngineConfiguration;
import mylie.core.EngineManager;
import mylie.core.application.Application;
import mylie.core.async.SchedulerSettings;
import mylie.core.component.Components;
import mylie.core.components.time.Timer;
import mylie.core.components.time.TimerSettings;
import mylie.platform.Desktop;
@Slf4j
public class HelloEngine extends Application {
	public static void main(String[] args) {
		Desktop desktop = new Desktop();
		EngineConfiguration engineConfiguration = desktop.initialize();
		engineConfiguration.option(EngineConfiguration.Application, new HelloEngine());
		engineConfiguration.option(EngineConfiguration.Timer, TimerSettings.dynamicInterval());
		engineConfiguration.option(EngineConfiguration.Scheduler, SchedulerSettings.virtualThreads());
		engineConfiguration.startEngine();
	}

	@Override
	protected void onInitialize() {
		log.debug("onInitialize");
	}
	@Override
	protected void onUpdate(Timer.Time time) {
		log.debug("onUpdate");
		//EngineManager.shutdown(Engine.ShutdownReason.error(new RuntimeException("AAA####AAA")));
		throw new RuntimeException("AAA####AAA");
		// if (time.version() == 10) {
		// component(EngineManager.class).shutdown(Engine.ShutdownReason.ok("User
		// requested shutdown")); }

	}

	@Override
	protected void onShutdown() {
		log.debug("onShutdown");
	}

	@Slf4j
	private static class Com1 extends Components.AppSequential implements Components.Updateable {

		@Override
		public void onUpdate() {
			log.debug("Com1 update");
		}
	}

	@Slf4j
	private static class Com2 extends Components.AppSequential implements Components.Updateable {
		public Com2() {

		}

		@Override
		public void onUpdate() {
			log.debug("Com2 update");
		}
	}

	@Slf4j
	private static class Com3 extends Components.AppParallel implements Components.Updateable {

		@Override
		public void onUpdate() {
			log.debug("Com3 update");
		}
	}

	@Slf4j
	private static class Com4 extends Components.AppParallel implements Components.Updateable {

		@Override
		public void onUpdate() {
			log.debug("Com4 update");
		}
	}

	@Slf4j
	private static class Com5 extends Components.AppParallel implements Components.Updateable, Components.AddRemove {

		@Override
		public void onUpdate() {
			log.debug("Com5 update");
		}

		@Override
		public void onAdded() {
			componentDependecies(component(Com2.class));
		}

		@Override
		public void onRemoved() {

		}
	}
}

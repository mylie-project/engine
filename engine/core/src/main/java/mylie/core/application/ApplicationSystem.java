package mylie.core.application;

import lombok.extern.slf4j.Slf4j;
import mylie.core.async.*;
import mylie.core.component.Components;
import mylie.core.component.Stages;
import mylie.core.components.threads.EngineThread;
import mylie.core.components.threads.ThreadManager;
import mylie.core.components.time.Timer;

@Slf4j
public class ApplicationSystem extends Components.Core
		implements
			Components.AddRemove,
			Components.Updateable,
			Components.Initializable {
	private boolean initialized = false;
	private final Application application;
	private EngineThread engineThread;
	public ApplicationSystem(Application application) {
		this.application = application;
		target(Application.TARGET);
		executionMode(Async.ExecutionMode.Async);
		cache(Caches.OneFrame);
	}

	@Override
	public void onAdded() {
		engineThread = component(ThreadManager.class).createEngineThread(Application.TARGET,
				component(Scheduler.class));
		engineThread.start();
		Stages.UpdateLogic.addDependency(this::update);
		application.onInit(componentManager());
	}

	@Override
	public void onUpdate() {
		if (!initialized) {
			initialized = true;
			application.onInitialize();
		}
		application.onUpdate(component(Timer.class).time());
	}

	@Override
	public void onRemoved() {

	}

	private static Function.F1<Application, Boolean> InitApplication = new Function.F1<>("InitApplication") {
		@Override
		protected Boolean apply(Application application) {
			application.onInitialize();
			return true;
		}
	};

	private static Function.F2<Application, Timer.Time, Boolean> UpdateApplication = new Function.F2<>(
			"UpdateApplication") {
		@Override
		protected Boolean apply(Application application, Timer.Time time) {
			application.onUpdate(time);
			return true;
		}
	};

	private static Function.F1<Application, Boolean> ShutdownApplication = new Function.F1<>("ShutdownApplication") {
		@Override
		protected Boolean apply(Application application) {
			application.onShutdown();
			return true;
		}
	};

	@Override
	public void onInitialize() {

	}

	@Override
	public void onShutdown() {
		application.onShutdown();
	}
}

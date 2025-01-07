package mylie.core.application;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.*;
import mylie.core.component.Components;
import mylie.core.component.Stages;
import mylie.core.components.threads.EngineThread;
import mylie.core.components.threads.ThreadManager;
import mylie.core.components.time.Timer;
import mylie.core.input.InputSystem;

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
		Stages.UpdateLogic.updateDependency(this::update);
		componentDependecies(component(InputSystem.class));
		application.onInit(componentManager());
	}

	@Override
	public void onUpdate() {
		if (!initialized) {
			initialized = true;
			application.onInitialize();
		}
		List<Components.AppSequential> sequentialComponents = componentManager()
				.components(Components.AppSequential.class);

		for (Components.AppSequential sequentialComponent : sequentialComponents) {
			sequentialComponent.update().result();
		}
		application.onUpdate(component(Timer.class).time());
	}

	@Override
	public void onRemoved() {

	}

	@Override
	public void onInitialize() {

	}

	@Override
	public void onShutdown() {
		application.onShutdown();
	}
}

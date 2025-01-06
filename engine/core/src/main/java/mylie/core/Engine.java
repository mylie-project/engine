package mylie.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.application.Application;
import mylie.core.application.ApplicationSystem;
import mylie.core.async.Async;
import mylie.core.async.Scheduler;
import mylie.core.component.Component;
import mylie.core.component.ComponentManager;
import mylie.core.components.threads.ThreadManager;
import mylie.core.components.time.AbstractTimer;
import mylie.core.components.time.Timer;
import mylie.util.configuration.Observable;

@Slf4j
public class Engine {
	public static final Async.Target TARGET = new Async.Target("EnginePrimary");
	private final Platform platform;
	private final EngineConfiguration configuration;
	private final ComponentManager componentManager = new ComponentManager();
	private final BlockingQueue<Runnable> engineTasks = new LinkedBlockingQueue<>();
	@Setter(AccessLevel.PACKAGE)
	private ShutdownReason shutdownReason;
	public Engine(Platform platform, EngineConfiguration configuration) {
		this.platform = platform;
		this.configuration = configuration;
		initModules();
	}

	private void initModules() {
		componentManager.component(new EngineManager(this));
		initModule(EngineConfiguration.Scheduler);
		componentManager.component(ThreadManager.create(configuration.option(EngineConfiguration.MultiThreaded)));
		Async.SCHEDULER(componentManager.component(Scheduler.class));
		initModule(EngineConfiguration.Timer);
		Application application = configuration.option(EngineConfiguration.Application);
		if (application != null) {
			componentManager.component(new ApplicationSystem(application));
		}
	}

	ShutdownReason start() {
		Thread thread = new Thread(this::updateLoop);
		thread.setName("EngineUpdate");
		if (configuration.option(EngineConfiguration.MultiThreaded)) {
			thread.start();
			componentManager.component(Scheduler.class).target(TARGET, engineTasks::add);
			Async.CURRENT_THREAD_TARGET.set(TARGET);
			Thread.currentThread().setName("EnginePrimary");
			while (thread.isAlive()) {
				while (!engineTasks.isEmpty()) {
					engineTasks.poll().run();
				}
				try {
					Runnable runnable = engineTasks.poll(10, TimeUnit.MILLISECONDS);
					if (runnable != null)
						runnable.run();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			Thread.currentThread().setName("EnginePrimary");
			updateLoop();
		}
		return shutdownReason;
	}

	private void updateLoop() {
		long frameId = 0;
		double logTime = 0;
		long counter = 0;
		while (shutdownReason == null) {
			frameId++;
			log.debug("### Frame {} ###", frameId);
			Timer.Time time = componentManager.component(AbstractTimer.class).update(frameId);
			componentManager.component(Scheduler.class).update(frameId);
			componentManager.onUpdate(time);
			counter++;
			logTime += time.delta();
			if (logTime >= 1) {
				log.info("### Frames per second: {} ###", counter);
				logTime -= 1;
				counter = 0;
			}
		}
		componentManager.onShutdown();
		componentManager.component(Scheduler.class).onShutdown();
		componentManager.component(ThreadManager.class).onShutdown();
	}

	private <T extends Component, V extends EngineModuleSettings<T>> void initModule(Observable<Engine, V> settings) {
		EngineModuleSettings<T> moduleSettings = configuration.option(settings);
		T component = moduleSettings.build();
		componentManager.component(component);
	}

	public static class ShutdownReason {
		String reason;
		private ShutdownReason(String reason) {
			this.reason = reason;
		}

		public static ShutdownReason ok(String reason) {
			return new ShutdownReason(reason);
		}
	}
}

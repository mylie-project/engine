package mylie.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.application.Application;
import mylie.core.application.ApplicationSystem;
import mylie.core.async.Async;
import mylie.core.async.Scheduler;
import mylie.core.component.Component;
import mylie.core.component.ComponentManager;
import mylie.core.components.Vault;
import mylie.core.components.threads.ThreadManager;
import mylie.core.components.time.AbstractTimer;
import mylie.core.components.time.Timer;
import mylie.core.input.InputSystem;
import mylie.info.BuildInfo;
import mylie.info.MylieLogo;
import mylie.util.configuration.Observable;

@Slf4j
public class Engine {
	public static final Vault.Item<Args> Arguments = new Vault.Item<>();
	private static final String ENGINE_PRIMARY_THREAD_NAME = "EnginePrimary";
	private static final String ENGINE_UPDATE_THREAD_NAME = "EngineUpdate";
	public static final Async.Target TARGET = new Async.Target(ENGINE_PRIMARY_THREAD_NAME);
	static Engine instance;
	private final Platform platform;
	private final EngineConfiguration configuration;
	private final ComponentManager componentManager = new ComponentManager();
	private final BlockingQueue<Runnable> engineTasks = new LinkedBlockingQueue<>();
	@Setter(AccessLevel.PACKAGE)
	private ShutdownReason shutdownReason;
	public Engine(Args args, Platform platform, EngineConfiguration configuration) {
		MylieLogo.printLogo(log);
		new BuildInfo().logBuildInfo(log);
		instance = this;

		this.platform = platform;
		this.configuration = configuration;
		componentManager.component(new Vault());
		componentManager.component(Vault.class).value(Arguments, args);
		initModules();
	}

	private void initModules() {

		componentManager.component(new EngineManager(this));
		initScheduler();
		initModule(EngineConfiguration.Timer);
		componentManager.component(new InputSystem());

		Application application = configuration.option(EngineConfiguration.Application);
		if (application != null) {
			componentManager.component(new ApplicationSystem(application));
		}
	}

	private void initScheduler() {
		initModule(EngineConfiguration.Scheduler);
		boolean multiThreaded = componentManager.component(Scheduler.class).multiThreaded();
		configuration.option(EngineConfiguration.MultiThreaded, multiThreaded);
		componentManager.component(ThreadManager.create(multiThreaded));
		Async.asyncScheduler(componentManager.component(Scheduler.class));
	}

	ShutdownReason start() {
		Thread thread = new Thread(this::updateLoop);
		thread.setName(ENGINE_UPDATE_THREAD_NAME);
		if (Boolean.TRUE.equals(configuration.option(EngineConfiguration.MultiThreaded))) {
			thread.start();
			componentManager.component(Scheduler.class).target(TARGET, engineTasks::add);
			Async.CURRENT_THREAD_TARGET.set(TARGET);
			Thread.currentThread().setName(ENGINE_PRIMARY_THREAD_NAME);
			while (thread.isAlive()) {
				while (!engineTasks.isEmpty()) {
					engineTasks.poll().run();
				}
				try {
					Runnable runnable = engineTasks.poll(10, TimeUnit.MILLISECONDS);
					if (runnable != null)
						runnable.run();
				} catch (InterruptedException e) {
					log.error("Engine thread interrupted", e);
					shutdownReason = ShutdownReason.error(e);
				}
			}
		} else {
			Thread.currentThread().setName(ENGINE_PRIMARY_THREAD_NAME);
			updateLoop();
		}
		if (shutdownReason instanceof ShutdownReason.Error error) {
			log.error("Exception: {}", error.cause().getMessage(), error.cause());
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
			try {
				componentManager.onUpdate();
			} catch (Exception e) {
				shutdownReason = ShutdownReason.error(e);
			}
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

	public interface ShutdownReason {

		static ShutdownReason ok(String reason) {
			return new UserRequest(reason);
		}

		static ShutdownReason error(Throwable cause) {
			return new Error(cause);
		}

		record UserRequest(String reason) implements ShutdownReason {
		}

		@Getter
		class Error implements ShutdownReason {
			Throwable cause;

			public Error(Throwable cause) {
				this.cause = cause;
				while (this.cause.getCause() != null) {
					this.cause = this.cause.getCause();
				}
			}
		}
	}
	@Slf4j
	public static class Args {
		private static final String ARGUMENT_DEFINED = "defined";
		final Map<String, String> arguments = new HashMap<>();
		public Args(String[] arguments) {
			for (int i = 0; i < arguments.length;) {
				String command = null;
				String value = null;
				if (arguments[i].startsWith("-")) {
					command = arguments[i].substring(1);
				}
				if (i + 1 < arguments.length && !arguments[i + 1].startsWith("-")) {
					value = arguments[i + 1];
				}
				if (command != null) {
					if (value == null) {
						this.arguments.put(command, ARGUMENT_DEFINED);
					} else {
						this.arguments.put(command, value);
						i++;
					}
					i++;
					log.trace("Argument {} : {}", command, value);
				} else {
					throw new IllegalArgumentException("Invalid argument: " + arguments[i]);
				}
			}
		}

		public boolean defined(String key) {
			if (key == null)
				return false;
			if (!arguments.containsKey(key))
				return false;
			return arguments.containsKey(key) || arguments.get(key).equals(ARGUMENT_DEFINED);
		}

		public String value(String key) {
			return arguments.get(key);
		}
	}
}

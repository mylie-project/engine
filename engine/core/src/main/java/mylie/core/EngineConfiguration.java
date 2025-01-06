package mylie.core;

import mylie.core.application.Application;
import mylie.util.configuration.Changeable;
import mylie.util.configuration.Configuration;
import mylie.util.configuration.Observable;

public class EngineConfiguration extends Configuration<Engine> {
	public static final Changeable<Engine, SchedulerSettings> Scheduler = new Changeable<>(
			SchedulerSettings.virtualThreads());
	public static final Changeable<Engine, TimerSettings> Timer = new Changeable<>(TimerSettings.dynamicInterval());
	public static final Observable<Engine, Boolean> MultiThreaded = new Observable<>(true);
	public static final Changeable<Engine, Application> Application = new Changeable<>(null);
	private final Platform platform;
	EngineConfiguration(Platform platform) {
		super(Engine.class);
		this.platform = platform;
	}

	public Engine.ShutdownReason startEngine() {
		return new Engine(platform, this).start();
	}
}

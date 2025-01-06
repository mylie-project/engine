package mylie.core;

import mylie.util.configuration.Changeable;
import mylie.util.configuration.Configuration;
import mylie.util.configuration.Observable;

public class EngineConfiguration extends Configuration<Engine> {
	public static final Changeable<Engine, SchedulerSettings> SCHEDULER = new Changeable<>(
			SchedulerSettings.virtualThreads());
	public static final Changeable<Engine, TimerSettings> TIMER = new Changeable<>(TimerSettings.dynamicInterval());
	public static final Observable<Engine,Boolean> MultiThreaded = new Observable<>(false);
	private final Platform platform;
	EngineConfiguration(Platform platform) {
		super(Engine.class);
		this.platform = platform;
	}

	public Engine.ShutdownReason startEngine() {
		return new Engine(platform, this).start();
	}
}

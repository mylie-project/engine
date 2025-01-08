package mylie.core;

import mylie.core.application.Application;
import mylie.core.async.SchedulerSettings;
import mylie.core.components.time.TimerSettings;
import mylie.util.configuration.Changeable;
import mylie.util.configuration.Configuration;
import mylie.util.configuration.Observable;

public class EngineConfiguration extends Configuration<Engine> {
	private final Engine.Args args;
	public static final Changeable<Engine, SchedulerSettings> Scheduler = new Changeable<>(
			SchedulerSettings.virtualThreads());
	public static final Changeable<Engine, TimerSettings> Timer = new Changeable<>(TimerSettings.dynamicInterval(-1));
	public static final Observable<Engine, Boolean> MultiThreaded = new Observable<>(true);
	public static final Changeable<Engine, Application> Application = new Changeable<>(null);
	private final Platform platform;

	@Override
	protected final <V> void option(Observable<Engine, V> option, V value) {
		super.option(option, value);
	}

	EngineConfiguration(Engine.Args args, Platform platform) {
		super(Engine.class);
		this.args = args;
		this.platform = platform;
	}

	public Engine.ShutdownReason startEngine() {
		return new Engine(args, platform, this).start();
	}
}

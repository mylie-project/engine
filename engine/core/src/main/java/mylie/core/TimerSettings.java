package mylie.core;

import lombok.Getter;
import mylie.core.components.time.Timer;
import mylie.core.components.time.Timers;

@Getter
public class TimerSettings implements EngineModuleSettings<Timer> {
	private float timeModifier = 1.0f;

	public static TimerSettings dynamicInterval() {
		return new TimerSettings();
	}

	@Override
	public Timer build() {
		return Timers.nanoTimer(this);
	}
}

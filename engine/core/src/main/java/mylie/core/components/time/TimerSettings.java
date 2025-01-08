package mylie.core.components.time;

import lombok.Getter;
import mylie.core.EngineModuleSettings;

@Getter
public class TimerSettings implements EngineModuleSettings<Timer> {
	private int fps;
	private float timeModifier = 1.0f;

	public TimerSettings(int fps) {
		this.fps = fps;
	}

	public static TimerSettings dynamicInterval(int fpsLimit) {
		return new TimerSettings(fpsLimit);
	}

	@Override
	public Timer build() {
		if (fps != -1) {
			return new Timers.LimitedNanoTimer(this, fps);
		} else {
			return Timers.nanoTimer(this);
		}
	}
}

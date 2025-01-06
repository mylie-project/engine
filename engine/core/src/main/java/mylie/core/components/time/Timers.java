package mylie.core.components.time;

import java.util.concurrent.TimeUnit;
import mylie.core.TimerSettings;

public class Timers {
	static final double NANOSECONDS_IN_SECOND = TimeUnit.SECONDS.toNanos(1);

	public static Timer nanoTimer(TimerSettings timerSettings) {
		return new NanoTimer(timerSettings);
	}

	static final class NanoTimer extends AbstractTimer {
		long startTime, endTime;

		public NanoTimer(TimerSettings settings) {
			super(settings);
			endTime = System.nanoTime();
		}

		@Override
		protected Time getTime(long version) {
			startTime = endTime;
			endTime = System.nanoTime();
			NanoTime time = new NanoTime();
			time.version(version);
			time.delta((endTime - startTime) / NANOSECONDS_IN_SECOND);
			time.deltaMod(time.delta() * settings().timeModifier());
			return time;
		}

		static class NanoTime extends Time {
			@Override
			protected Time version(long version) {
				return super.version(version);
			}

			@Override
			protected Time delta(double delta) {
				return super.delta(delta);
			}

			@Override
			protected Time deltaMod(double deltaMod) {
				return super.deltaMod(deltaMod);
			}
		}
	}
}

package mylie.core.components.time;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

public class Timers {
	private Timers() {
	}
	static final double NANOSECONDS_IN_SECOND = TimeUnit.SECONDS.toNanos(1);

	public static Timer nanoTimer(TimerSettings timerSettings) {
		return new NanoTimer(timerSettings);
	}

	static class NanoTimer extends AbstractTimer {
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

	@Slf4j
	static final class LimitedNanoTimer extends NanoTimer {
		final long fps;
		final long tpf;
		public LimitedNanoTimer(TimerSettings settings, long fps) {
			super(settings);
			this.fps = fps;
			tpf = (long) (NANOSECONDS_IN_SECOND / fps);
		}

		private void waitFor(long nanos) {
			if (nanos <= 0) {
				return; // No need to wait for non-positive durations
			}

			long millis = nanos / 1_000_000;
			nanos -= millis * 1_000_000;

			try {
				// Use Thread.sleep for durations longer than 1 millisecond
				if (millis > 0 || nanos > 0) {
					Thread.sleep(millis, (int) nanos);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupt status
				log.error("Interrupted while waiting for timer", e);
			}
		}
		long limitTime = 0;

		@Override
		protected Time getTime(long version) {
			long waitTime = tpf - (System.nanoTime() - limitTime);
			// Wait only if there's remaining time
			if (waitTime > 0) {
				waitFor(waitTime);
			}
			limitTime = System.nanoTime();
			return super.getTime(version);
		}
	}
}

package mylie.core.input;

import java.util.Arrays;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import mylie.core.input.devices.Gamepad;
import mylie.util.filter.Filter;

public class InputFilters {
	public static final Filter<InputType.Type<Boolean, Gamepad>> KonamiCode = sequence(Gamepad.Dpad.UP, Gamepad.Dpad.UP,
			Gamepad.Dpad.DOWN, Gamepad.Dpad.DOWN, Gamepad.Dpad.LEFT, Gamepad.Dpad.RIGHT, Gamepad.Dpad.LEFT,
			Gamepad.Dpad.RIGHT, Gamepad.Button.B, Gamepad.Button.A);

	/// Constructs a filter that detects a specific sequence of input types. The
	/// filter matches when the provided
	/// sequence of input events is detected in the specified order. Once the
	/// sequence is detected, the internal
	/// state resets and the filter can match the sequence again.
	///
	/// @param <I> The specific type of the input that extends [InputType.Type].
	/// @param <D> The input device type that extends [InputDevice].
	/// @param sequence The sequence of input types to detect.
	/// @return A [Filter] that detects the specified input sequence.
	@SafeVarargs
	public static <I extends InputType.Type<Boolean, D>, D extends InputDevice<D>> Filter<I> sequence(I... sequence) {
		return new SequenceFilter<>(sequence);
	}

	@Slf4j
	@EqualsAndHashCode(callSuper = false)
	private static final class SequenceFilter<I extends InputType.Type<Boolean, D>, D extends InputDevice<D>>
			extends
				Filter<I> {
		final I[] sequence;
		@EqualsAndHashCode.Exclude
		final boolean[] sequenceState;
		@SafeVarargs
		private SequenceFilter(I... sequence) {
			this.sequence = sequence;
			this.sequenceState = new boolean[sequence.length];
			resetSequenceState();
		}
		@SafeVarargs
		@Override
		public final boolean apply(I... value) {
			for (I key : value) {
				for (int currentIndex = 0; currentIndex < sequence.length; currentIndex++) {
					if (sequenceState[currentIndex])
						continue;
					if (sequence[currentIndex] == key) {
						sequenceState[currentIndex] = true;
						if (isLastIndex(currentIndex)) {
							resetSequenceState();
							return true;
						} else {
							break;
						}
					} else {
						if (currentIndex == 0) {
							break;
						}
						resetSequenceState();
						currentIndex = -1;
					}
				}
			}
			return false;
		}

		private boolean isLastIndex(int index) {
			return index == sequence.length - 1;
		}

		private void resetSequenceState() {
			Arrays.fill(sequenceState, false);
		}
	}
}

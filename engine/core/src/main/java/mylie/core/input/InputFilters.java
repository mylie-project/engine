package mylie.core.input;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import mylie.util.filter.Filter;

public class InputFilters {
	/// Constructs a filter that detects a specific sequence of input types. The filter matches when the provided
	/// sequence of input events is detected in the specified order. Once the sequence is detected, the internal
	/// state resets and the filter can match the sequence again.
	///
	/// @param <I> The specific type of the input that extends [InputType.Type].
	/// @param <D> The input device type that extends [InputDevice].
	/// @param sequence The sequence of input types to detect.
	/// @return A [Filter] that detects the specified input sequence.
	@SafeVarargs
    public static <I extends InputType.Type<Boolean, D>, D extends InputDevice<D>> Filter<I> sequenze(I... sequence) {
		return new SequenzeFilter<>(sequence);
	}

	@Slf4j
	private static final class SequenzeFilter<I extends InputType.Type<Boolean, D>, D extends InputDevice<D>>
			extends
				Filter<I> {
		I[] sequence;
		boolean[] sequenceState;
		@SafeVarargs
        private SequenzeFilter(I... sequence) {
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

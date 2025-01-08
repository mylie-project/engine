package mylie.core.input;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import mylie.core.input.devices.Keyboard;
import mylie.util.Versioned;

public class InputDevice<D extends InputDevice<D>> {
	@Getter
	private final String name;
	private final Map<InputType.Type<?, D>, Versioned<?>> inputStates = new HashMap<>();

	@SafeVarargs
	protected InputDevice(String name, InputType.Type<?, D>[]... types) {
		this.name = name;
		for (InputType.Type<?, D>[] type : types) {
			for (InputType.Type<?, D> dType : type) {
				inputStates.put(dType, new Versioned<>(null));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> Versioned<T> versioned(InputType.Type<T, D> value) {
		return (Versioned<T>) inputStates.get(value);
	}

	<R> void value(InputType.Type<R, D> type, R value) {
		versioned(type).value(value);
	}

	public <R> R value(InputType.Type<R, D> type) {
		return versioned(type).value();
	}

	public <R> Versioned.Reference<R> valueReference(InputType.Type<R, D> type) {
		return versioned(type).reference();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof InputDevice) {
			return name.equals(((InputDevice<?>) obj).name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

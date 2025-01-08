package mylie.core.input;

public class InputType {
	public interface Type<T, D extends InputDevice<D>> {

	}

	public interface Digital<D extends InputDevice<D>> extends Type<Boolean, D> {
	}

	public interface Analog<D extends InputDevice<D>> extends Type<Float, D> {

	}

	public interface Absolute<T, D extends InputDevice<D>> extends Type<T, D> {

	}
}

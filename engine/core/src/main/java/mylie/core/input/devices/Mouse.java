package mylie.core.input.devices;

import mylie.core.input.InputDevice;
import mylie.core.input.InputType;
import mylie.math.Vector2f;

public class Mouse extends InputDevice<Mouse> {

	public Mouse(String name) {
		super(name, State.values(), Axis.values(), Button.values(), Cursor.values());
	}

	public enum State implements InputType.Digital<Mouse> {
		CURSOR_HIDDEN, CURSOR_VISIBLE
	}

	public enum Axis implements InputType.Analog<Mouse> {
		X, Y, WHEEL
	}

	public enum Button implements InputType.Digital<Mouse> {
		LEFT, RIGHT, MIDDLE, BUTTON_1, BUTTON_2, BUTTON_3, BUTTON_4, BUTTON_5, BUTTON_6, BUTTON_7, BUTTON_8
	}

	public enum Cursor implements InputType.Absolute<Vector2f, Mouse> {
		ABSOLUTE, RELATIVE
	}
}

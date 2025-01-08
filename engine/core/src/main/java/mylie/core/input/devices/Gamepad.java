package mylie.core.input.devices;

import mylie.core.input.InputDevice;
import mylie.core.input.InputType;

import java.util.Objects;

public class Gamepad extends InputDevice<Gamepad> {
	final int gamepadIndex;

	public Gamepad(String name, int gamepadIndex) {
		super(name, State.values(), Axis.values(), Button.values(), Dpad.values());
		this.gamepadIndex = gamepadIndex;
	}

	public enum State implements InputType.Digital<Gamepad> {
		CONNECTED, DISCONNECTED, WIRED, VIBRATION;
	}
	public enum Axis implements InputType.Digital<Gamepad> {
		LEFT_X, LEFT_Y, RIGHT_X, RIGHT_Y, LEFT_TRIGGER, RIGHT_TRIGGER;
	}
	public enum Button implements InputType.Digital<Gamepad> {
		A, B, X, Y, LEFT_BUMPER, RIGHT_BUMPER, BACK, START, GUIDE, LEFT_STICK, RIGHT_STICK;
	}

	public enum Dpad implements InputType.Digital<Gamepad> {
		UP, DOWN, LEFT, RIGHT;
	}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Gamepad gamepad = (Gamepad) o;
        return gamepadIndex == gamepad.gamepadIndex && name().equals(gamepad.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(gamepadIndex,name());
    }
}

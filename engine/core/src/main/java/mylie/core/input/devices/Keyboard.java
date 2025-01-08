package mylie.core.input.devices;

import mylie.core.input.InputDevice;
import mylie.core.input.InputType;

public class Keyboard extends InputDevice<Keyboard> {
	public Keyboard(String name) {
		super(name, Key.values());
	}

	public enum Key implements InputType.Digital<Keyboard> {
		// Function keys
		ESC, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,

		// Number row and symbols
		GRAVE_ACCENT, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9, NUM_0, MINUS, EQUAL, BACKSPACE,

		// Tab, letters, brackets, and backslash
		TAB, Q, W, E, R, T, Y, U, I, O, P, LEFT_BRACKET, RIGHT_BRACKET, BACKSLASH,

		// Home row with Caps Lock and Enter
		CAPS_LOCK, A, S, D, F, G, H, J, K, L, SEMICOLON, APOSTROPHE, ENTER,

		// Shift row and Space
		LEFT_SHIFT, Z, X, C, V, B, N, M, COMMA, PERIOD, SLASH, RIGHT_SHIFT, LEFT_CTRL, LEFT_ALT, SPACE, RIGHT_ALT, RIGHT_CTRL,

		// Navigation keys
		PRINT_SCREEN, SCROLL_LOCK, PAUSE, INSERT, HOME, PAGE_UP, DELETE, END, PAGE_DOWN, ARROW_UP, ARROW_LEFT, ARROW_DOWN, ARROW_RIGHT,

		// Numpad keys
		NUM_LOCK, NUMPAD_DIVIDE, NUMPAD_MULTIPLY, NUMPAD_SUBTRACT, NUMPAD_ADD, NUMPAD_ENTER, NUMPAD_0, NUMPAD_1, NUMPAD_2, NUMPAD_3, NUMPAD_4, NUMPAD_5, NUMPAD_6, NUMPAD_7, NUMPAD_8, NUMPAD_9, NUMPAD_DECIMAL
	}
}

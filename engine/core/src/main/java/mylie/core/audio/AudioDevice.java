package mylie.core.audio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class AudioDevice {
	final String name;
	final boolean isDefault;

	@ToString(callSuper = true)
	public static class Output extends AudioDevice {

		public Output(String name, boolean isDefault) {
			super(name, isDefault);
		}
	}

	@ToString(callSuper = true)
	public static class Input extends AudioDevice {
		public Input(String name, boolean isDefault) {
			super(name, isDefault);
		}
	}
}

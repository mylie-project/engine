package mylie.core.audio;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.core.async.Result;
import mylie.util.Void;

@Getter(AccessLevel.PROTECTED)
public abstract class AudioOutputContext {
	final AudioDevice.Output device;

	protected AudioOutputContext(AudioDevice.Output device) {
		this.device = device;
	}

	public abstract Result<Void> create();

	public abstract Result<Void> destroy();
}

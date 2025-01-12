package mylie.core.audio.apis.openal;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.core.audio.AudioDevice;
import mylie.core.audio.AudioOutputContext;

@Getter(AccessLevel.PROTECTED)
public abstract class OpenAlOutputContext extends AudioOutputContext {
	private final List<MonoSource> freeSources = new ArrayList<>();
	protected OpenAlOutputContext(AudioDevice.Output device) {
		super(device);
	}
}

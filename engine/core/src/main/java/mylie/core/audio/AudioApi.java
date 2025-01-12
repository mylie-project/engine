package mylie.core.audio;

import java.util.Collection;

public abstract class AudioApi {
	public abstract void initialize(AudioSystem audioSystem);

	public abstract Collection<AudioDevice> devices();

	public abstract AudioOutputContext createContext(AudioDevice.Output device);
}

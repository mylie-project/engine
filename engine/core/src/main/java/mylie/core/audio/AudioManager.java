package mylie.core.audio;

import java.util.List;
import mylie.core.async.Async;
import mylie.core.component.Components;

public interface AudioManager extends Components.AppComponent {
	Async.Target TARGET = new Async.Target("Audio");

	List<AudioDevice.Output> playbackDevices();

	List<AudioDevice.Input> recordDevices();

	void listener(AudioListener audioListener);

	void createOutputContext(AudioDevice.Output device);
}

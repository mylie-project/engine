package mylie.lwjgl3.openal;

import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import mylie.core.audio.AudioDevice;
import mylie.core.audio.AudioOutputContext;
import mylie.core.audio.AudioSystem;
import org.lwjgl.openal.*;
@Slf4j
public class Lwjgl3OpenAlApi extends mylie.core.audio.apis.OpenAl {
	long device;
	@Override
	public void initialize(AudioSystem audioSystem) {
		log.trace("Default playback device: {}", alcGetString(0, EnumerateAllExt.ALC_DEFAULT_ALL_DEVICES_SPECIFIER));
		device = alcOpenDevice((ByteBuffer) null);
		if (device == NULL) {
			log.error("Failed to open OpenAL device.");
		}
	}

	@Override
	public Collection<AudioDevice> devices() {
		String outputDevice = alcGetString(0, EnumerateAllExt.ALC_DEFAULT_ALL_DEVICES_SPECIFIER);
		List<String> outputDevices = Objects
				.requireNonNull(ALUtil.getStringList(NULL, EnumerateAllExt.ALC_ALL_DEVICES_SPECIFIER));
		List<String> inputDevices = Objects.requireNonNull(ALUtil.getStringList(NULL, ALC_CAPTURE_DEVICE_SPECIFIER));
		String inputDevice = alcGetString(0, ALC_CAPTURE_DEVICE_SPECIFIER);

		List<AudioDevice> audioDevices = new ArrayList<>();
		for (String deviceId : outputDevices) {
			audioDevices.add(new AudioDevice.Output(deviceId, deviceId.equals(outputDevice)));
		}
		for (String deviceId : inputDevices) {
			audioDevices.add(new AudioDevice.Input(deviceId, deviceId.equals(inputDevice)));
		}
		return audioDevices;
	}

	@Override
	public AudioOutputContext createContext(AudioDevice.Output device) {
		return new Lwjgl3OutputContext(this, device);
	}
}

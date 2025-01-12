package mylie.core.audio;

import static mylie.util.Void.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Async;
import mylie.core.async.Caches;
import mylie.core.async.Function;
import mylie.core.async.Scheduler;
import mylie.core.component.Components;
import mylie.core.component.Stages;
import mylie.core.components.threads.EngineThread;
import mylie.core.components.threads.ThreadManager;

@Slf4j
public class AudioSystem extends Components.Core implements AudioManager, Components.AddRemove, Components.Updateable {
	private final AudioApi audioApi;
	private EngineThread engineThread;
	@Getter
	private final List<AudioDevice.Output> playbackDevices = new ArrayList<>();
	@Getter
	private final List<AudioDevice.Input> recordDevices = new ArrayList<>();
	public AudioSystem(AudioApi audioApi) {
		this.audioApi = audioApi;
	}

	@Override
	public void onAdded() {
		engineThread = component(ThreadManager.class).createEngineThread(AudioManager.TARGET,
				component(Scheduler.class));
		engineThread.start();
		updateDependency(Stages.UpdateLogic::execute);
		audioApi.initialize(this);
		for (AudioDevice device : audioApi.devices()) {
			log.trace("Audio device: {}", device);
			switch (device) {
				case AudioDevice.Output output -> playbackDevices.add(output);
				case AudioDevice.Input input -> recordDevices.add(input);
				default -> {
				}
			}
		}
		log.info("Audio system initialized");
		AudioOutputContext context = audioApi.createContext(playbackDevices.getFirst());
		context.create().result();
		context.destroy().result();
	}

	@Override
	public void onRemoved() {

	}

	@Override
	public void onUpdate() {
		Async.async(Async.ExecutionMode.ASYNC, AudioManager.TARGET, Caches.OneFrame, -1, Update).result();
	}

	private static final Function.F0<mylie.util.Void> Update = new Function.F0<>("UpdateAudio") {
		@Override
		protected mylie.util.Void apply() {
			return VOID;
		}
	};
}

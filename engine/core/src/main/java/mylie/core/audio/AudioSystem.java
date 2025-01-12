package mylie.core.audio;

import static mylie.util.Void.*;

import java.util.*;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Async;
import mylie.core.async.Caches;
import mylie.core.async.Function;
import mylie.core.async.Scheduler;
import mylie.core.component.Components;
import mylie.core.component.Stages;
import mylie.core.components.threads.EngineThread;
import mylie.core.components.threads.ThreadManager;
import mylie.core.scene.Node;
import mylie.core.scene.Spatial;
import mylie.core.scene.Traverser;

@Slf4j
public class AudioSystem extends Components.Core implements AudioManager, Components.AddRemove, Components.Updateable {
	private final AudioApi audioApi;
	private EngineThread engineThread;
	@Setter
	private AudioListener listener;
	@Getter
	private final List<AudioDevice.Output> playbackDevices = new ArrayList<>();
	@Getter
	private final List<AudioDevice.Input> recordDevices = new ArrayList<>();
	private AudioOutputContext outputContext;
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
	}

	@Override
	public void onRemoved() {

	}

	@Override
	public void onUpdate() {
		if (listener != null) {
			Spatial sceneRoot = listener.getRoot();
			AudioCullResult audioCullResult = new AudioCullResult(listener, null);
			Traverser.traverse(Traverser.ToLeaf,sceneRoot,audioCullResult);
			Set<AudioSource> sources = audioCullResult.sources;

		}
		Async.async(Async.ExecutionMode.ASYNC, AudioManager.TARGET, Caches.OneFrame, -1, Update).result();
	}

	@Override
	public void createOutputContext(AudioDevice.Output device) {
		if(device == null){
			device = playbackDevices.getFirst();
		}
		outputContext = audioApi.createContext(device);
		outputContext.create();
	}


	private static final Function.F0<mylie.util.Void> Update = new Function.F0<>("UpdateAudio") {
		@Override
		protected mylie.util.Void apply() {
			return VOID;
		}
	};



	private static class AudioCullResult implements Traverser.Visitor {
		final AudioListener listener;
		final AudioCullResult parent;
		final Set<AudioSource> sources = new HashSet<>();
		public AudioCullResult(AudioListener listener, AudioCullResult parent) {
			this.listener = listener;
            this.parent = parent;
        }

		@Override
		public boolean visit(Spatial spatial) {
			//if(!spatial.worldBounds().collidesWith(listener.worldBounds())){
			//	return false;
			//}else{
				if(spatial instanceof AudioSource audioSource){
					if(audioSource.state() == AudioSource.State.PLAYING) {
						sources.add(audioSource);
					}
				}
			//}
			return true;
		}
	}
}

package mylie.demos.tutorial.audio;

import mylie.core.assets.AssetManager;
import mylie.core.audio.AudioData;
import mylie.core.component.Components;
import mylie.demos.Demo;

public class HelloAudio extends Demo implements Components.Initializable {

	private AudioData load;

	@Override
	public void onInitialize() {
		load = component(AssetManager.class).loadAsset(new AudioData.Id("sound/test.wav"));
	}

	@Override
	public void onShutdown() {

	}
}

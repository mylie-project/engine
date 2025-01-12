package mylie.demos;

import mylie.core.application.Application;
import mylie.core.assets.AssetLocation;
import mylie.core.assets.AssetManager;
import mylie.core.audio.loaders.JavaAudioSystemLoader;
import mylie.core.components.time.Timer;
import mylie.demos.tutorial.audio.HelloAudio;

public class DemoApplication extends Application {
	@Override
	protected void onInitialize() {
		component(AssetManager.class).addAssetLocation(AssetLocation.classpath(""));
		component(AssetManager.class).addAssetImporter(new JavaAudioSystemLoader());
		component(new DemoSelector());
		component(new HelloAudio());
	}

	@Override
	protected void onUpdate(Timer.Time time) {

	}

	@Override
	protected void onShutdown() {

	}
}

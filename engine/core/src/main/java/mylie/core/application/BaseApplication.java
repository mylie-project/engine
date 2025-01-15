package mylie.core.application;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.assets.AssetLocation;
import mylie.core.assets.AssetManager;
import mylie.core.audio.AudioListener;
import mylie.core.audio.AudioManager;
import mylie.core.audio.loaders.JavaAudioSystemLoader;
import mylie.core.scene.Node;
@Slf4j
@Getter(AccessLevel.PROTECTED)
public abstract class BaseApplication extends Application {
	private AssetManager assetManager;
	private AudioManager audioManager;
	private AudioListener audioListener;
	private final Node rootNode = new Node();
	private final Node cameraNode = new Node();
	public BaseApplication() {
		super();
	}

	@Override
	protected void onInitialize() {
		assetManager = component(AssetManager.class);
		initDefaultAssetResources();
		audioManager = component(AudioManager.class);
		rootNode.child(cameraNode);
		log.info("Initialized application {}", audioManager != null ? "with audio" : "without audio");
		if (audioManager != null) {
			audioListener = new AudioListener();
			cameraNode.child(audioListener);
			audioManager.listener(audioListener);
			audioManager.createOutputContext(null);
		}
	}

	private void initDefaultAssetResources() {
		assetManager.addAssetLocation(AssetLocation.classpath(""));
		assetManager.addAssetImporter(new JavaAudioSystemLoader());
	}
}

package mylie.core.audio.loaders;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.sound.sampled.*;
import lombok.extern.slf4j.Slf4j;
import mylie.core.assets.AssetId;
import mylie.core.assets.AssetImporter;
import mylie.core.assets.AssetInfo;
import mylie.core.audio.AudioData;

@Slf4j
public class JavaAudioSystemLoader extends AssetImporter<AudioData.Id, AudioData> {
	private final Set<String> supportedTypes = new HashSet<>();

	public JavaAudioSystemLoader() {
		this(Arrays.stream(AudioSystem.getAudioFileTypes()).map(AudioFileFormat.Type::getExtension).toList()
				.toArray(new String[0]));
	}

	public JavaAudioSystemLoader(String... supportedTypes) {
		this.supportedTypes.addAll(Arrays.asList(supportedTypes));
	}

	@Override
	protected boolean canRead(AssetId<?> assetInfo) {
		if (assetInfo instanceof AudioData.Id audioId) {
			return audioId.filetype().equals("wav");
		}
		return false;
	}

	@Override
	protected AudioData read(AssetInfo<AudioData.Id, AudioData> assetInfo) throws IOException {
		try (InputStream inputStream = assetInfo.assetLocation().read(assetInfo)) {
			try {
				AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(new BufferedInputStream(inputStream));
				AudioFormat format = audioInputStream.getFormat();
				int channels = format.getChannels();
				float sampleRate = format.getSampleRate();
				int bufferSize = (int) (format.getFrameRate() * format.getFrameSize());
				byte[] data = new byte[bufferSize];
				// noinspection StatementWithEmptyBody
				while (audioInputStream.read(data) != -1) {
					// intentional
				}
				AudioData audioData = new AudioData(channels, sampleRate, ByteBuffer.wrap(data));
				audioData.assetId(assetInfo.assetId());
				audioData.assetId().loaded(true);
				log.trace("Loaded audio data: {}", audioData);
				return audioData;

			} catch (UnsupportedAudioFileException e) {
				log.error("Unsupported audio file", e);
			}
		}
		return null;
	}
}

package mylie.core.audio;

import java.nio.ByteBuffer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import mylie.core.assets.Asset;
import mylie.core.assets.AssetId;

@ToString
public class AudioData implements Asset<AudioData.Id, AudioData> {
	private Id assetId;
	@Getter
	final int channels;
	@Getter
	final float sampleRate;
	@Getter(AccessLevel.PACKAGE)
	final ByteBuffer data;

	public AudioData(int channels, float sampleRate, ByteBuffer data) {
		this.channels = channels;
		this.sampleRate = sampleRate;
		this.data = data;
	}

	@Override
	public Id assetId() {
		return assetId;
	}

	@Override
	public void assetId(Id assetId) {
		this.assetId = assetId;
	}

	@ToString(callSuper = true)
	public static class Id extends AssetId<AudioData> {
		public Id(String name) {
			super(name);
		}
	}
}

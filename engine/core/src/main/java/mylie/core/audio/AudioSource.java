package mylie.core.audio;

import lombok.AccessLevel;
import lombok.Getter;
import mylie.core.scene.Spatial;

@Getter(AccessLevel.PACKAGE)
public class AudioSource extends Spatial implements Spatial.Translatable {
    private State state = State.STOPPED;
    public enum State {
        STOPPED, PLAYING
    }
}

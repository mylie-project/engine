package mylie.core.audio;

import mylie.core.async.Async;
import mylie.core.component.Components;

public interface AudioManager extends Components.AppComponent {
    Async.Target TARGET = new Async.Target("Audio");
}

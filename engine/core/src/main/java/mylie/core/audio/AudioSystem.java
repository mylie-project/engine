package mylie.core.audio;

import mylie.core.async.Async;
import mylie.core.async.Caches;
import mylie.core.async.Function;
import mylie.core.async.Scheduler;
import mylie.core.component.Components;
import mylie.core.component.Stages;
import mylie.core.components.threads.EngineThread;
import mylie.core.components.threads.ThreadManager;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static mylie.util.Void.*;

public class AudioSystem extends Components.Core implements AudioManager,Components.AddRemove, Components.Updateable{
    private final AudioApi audioApi;
    private EngineThread engineThread;
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
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onUpdate() {
        Async.async(Async.ExecutionMode.ASYNC,AudioManager.TARGET, Caches.OneFrame, -1, Update).result();
    }

    private static final Function.F0<mylie.util.Void> Update= new Function.F0<>("UpdateAudio") {
        @Override
        protected mylie.util.Void apply() {
            return VOID;
        }
    };
}

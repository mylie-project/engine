package mylie.lwjgl3.openal;

import lombok.extern.slf4j.Slf4j;
import mylie.core.audio.AudioSystem;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EnumerateAllExt;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static mylie.lwjgl3.openal.Lwjgl3OpenAlUtil.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
@Slf4j
public class OpenAlApi extends mylie.core.audio.apis.OpenAl {
    long device;
    @Override
    public void initialize(AudioSystem audioSystem) {
        log.trace("Default playback device: {}", alcGetString(0, EnumerateAllExt.ALC_DEFAULT_ALL_DEVICES_SPECIFIER));
        device= alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            log.error("Failed to open OpenAL device.");
            return;
        }
        ALCCapabilities deviceCapabilities= ALC.createCapabilities(device);
        long context = alcCreateContext(device, (IntBuffer)null);
        alcMakeContextCurrent(context);
        checkALCError(device);
        AL.createCapabilities(deviceCapabilities);
        printALCInfo(device, deviceCapabilities);
        printALInfo();
    }
}

package mylie.lwjgl3.openal;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC11.*;

import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Async;
import mylie.core.async.Caches;
import mylie.core.async.Function;
import mylie.core.async.Result;
import mylie.core.audio.AudioDevice;
import mylie.core.audio.AudioManager;
import mylie.core.audio.apis.openal.MonoSource;
import mylie.core.audio.apis.openal.OpenAlOutputContext;
import mylie.util.Void;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryUtil;

@Slf4j
public class Lwjgl3OutputContext extends OpenAlOutputContext {
	private final Lwjgl3OpenAlApi alApi;
	private long deviceHandle;
	private long contextHandle;
	private ALCCapabilities alcCapabilities;
	private ALCapabilities alCapabilities;
	private boolean threadLocal;
	public Lwjgl3OutputContext(Lwjgl3OpenAlApi alApi, AudioDevice.Output output) {
		super(output);
		this.alApi = alApi;
	}

	private void initSources() {
		int buffer;
		while ((buffer = alGenSources()) != 0) {
			freeSources().add(new MonoSource(buffer, this));
		}
	}

	@Override
	public Result<Void> create() {
		return Async.async(Async.ExecutionMode.ASYNC, AudioManager.TARGET, Caches.No, -1, CREATE_CONTEXT, this);
	}

	@Override
	public Result<Void> destroy() {
		return Async.async(Async.ExecutionMode.ASYNC, AudioManager.TARGET, Caches.No, -1, DESTROY_CONTEXT, this);
	}

	private final static Function.F1<Lwjgl3OutputContext, Void> CREATE_CONTEXT = new Function.F1<>(
			"Create OpenAl context") {
		@Override
		protected Void apply(Lwjgl3OutputContext context) {
			context.deviceHandle = ALC11.alcOpenDevice(context.device().name());
			if (context.deviceHandle == MemoryUtil.NULL) {
				log.error("Failed to open OpenAL device.");
				return Void.VOID;
			}
			context.alcCapabilities = ALC.createCapabilities(context.deviceHandle);
			if (!context.alcCapabilities.OpenALC10) {
				log.error("Failed to create OpenAL context.");
				return Void.VOID;
			}
			log.trace("Created OpenAl context.");
			context.contextHandle = ALC11.alcCreateContext(context.deviceHandle, (int[]) null);
			context.threadLocal = context.alcCapabilities.ALC_EXT_thread_local_context;
			if (context.threadLocal) {
				log.trace("Setting OpenAl context to thread local.");
				if (!EXTThreadLocalContext.alcSetThreadContext(context.contextHandle)) {
					context.threadLocal = false;
				}
			}
			if (!context.threadLocal) {
				log.trace("Setting OpenAl context to global.");
				ALC11.alcMakeContextCurrent(context.contextHandle);
			}
			context.alCapabilities = AL.createCapabilities(context.alcCapabilities, MemoryUtil::memCallocPointer);

			log.trace("ALC_FREQUENCY     : {}", alcGetInteger(context.deviceHandle, ALC_FREQUENCY) + "Hz");
			log.trace("ALC_REFRESH       : {}", alcGetInteger(context.deviceHandle, ALC_REFRESH) + "Hz");
			log.trace("ALC_SYNC          : {}", (alcGetInteger(context.deviceHandle, ALC_SYNC) == ALC_TRUE));
			log.trace("ALC_MONO_SOURCES  : {}", alcGetInteger(context.deviceHandle, ALC_MONO_SOURCES));
			log.trace("ALC_STEREO_SOURCES: {}", alcGetInteger(context.deviceHandle, ALC_STEREO_SOURCES));

			context.initSources();
			return Void.VOID;
		}
	};

	private final static Function.F1<Lwjgl3OutputContext, Void> DESTROY_CONTEXT = new Function.F1<>(
			"Destroy OpenAl context") {
		@Override
		protected Void apply(Lwjgl3OutputContext context) {
			alcMakeContextCurrent(MemoryUtil.NULL);
			if (context.threadLocal) {
				AL.setCurrentThread(null);
			} else {
				AL.setCurrentProcess(null);
			}
			MemoryUtil.memFree(context.alCapabilities.getAddressBuffer());
			alcDestroyContext(context.contextHandle);
			alcCloseDevice(context.deviceHandle);
			log.trace("Destroyed OpenAl context.");
			return Void.VOID;
		}
	};
}

package mylie.core.components.threads;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import mylie.core.async.Async;
import mylie.core.async.Scheduler;
import mylie.core.component.Components;

public class ThreadManager implements Components.CoreComponent {
	private final boolean multithreaded;
	private final Set<EngineThread> activeThreads = new CopyOnWriteArraySet<>();
	public ThreadManager(boolean multithreaded) {
		this.multithreaded = multithreaded;
	}

	public EngineThread createEngineThread(Async.Target target, Scheduler scheduler) {
		if (multithreaded) {
			ManagedThread managedThread = new ManagedThread(target, scheduler);
			activeThreads.add(managedThread);
			return managedThread;
		} else {
			scheduler.target(target, null);
			return new NoOpThread();
		}
	}

	public void onShutdown() {
		activeThreads.forEach(EngineThread::stop);
	}

	public static ThreadManager create(boolean multithreaded) {
		return new ThreadManager(multithreaded);
	}

	private static class NoOpThread implements EngineThread {
		@Override
		public void start() {
			// NoOp intentional
		}

		@Override
		public void stop() {
			// NoOp intentional
		}
	}

}

package mylie.core.components.threads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Async;
import mylie.core.async.Scheduler;

@Slf4j
class ManagedThread implements EngineThread {
	private final Async.Target target;
	private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
	private final Thread thread;
	private boolean running = false;
	ManagedThread(Async.Target target, Scheduler scheduler) {
		this.target = target;
		scheduler.target(target, queue::add);
		this.thread = new Thread(this::run);
		this.thread.setName(target.id());
	}

	@Override
	public void start() {
		running = true;
		thread.start();
	}

	@Override
	public void stop() {

		CompletableFuture<Boolean> future = new CompletableFuture<>();
		queue.add(() -> {
			log.trace("Stopping engine thread");
			running = false;
			future.complete(true);
		});
		try {
			Boolean b = future.get(1, TimeUnit.SECONDS);
			if (!b) {
				log.error("Failed to stop engine thread");
			}
		} catch (Exception e) {
			log.error("Failed to stop engine thread", e);
		}
	}

	private void run() {
		Async.CURRENT_THREAD_TARGET.set(target);
		while (running) {
			try {
				queue.take().run();
			} catch (InterruptedException e) {
				log.error("Engine thread interrupted", e);
			}
		}
	}
}

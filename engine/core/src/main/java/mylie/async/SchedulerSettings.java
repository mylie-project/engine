package mylie.async;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
@SuppressWarnings("unused")
@Getter(AccessLevel.PACKAGE)
public class SchedulerSettings {
	public enum ThreadingModel {
		SINGLE_THREADED, MULTI_THREADED,
	}

	public enum ExecutorType {
		FORK_JOIN, THREAD_POOL, VIRTUAL_THREAD, WORK_STEALING_POOL, CUSTOM
	}
	private final ThreadingModel threadingModel;
	private final ExecutorType executorType;
	private final int threadCount;
	private final Supplier<Scheduler> customSchedulerSupplier;

	private SchedulerSettings(ThreadingModel threadingModel, ExecutorType executorType, int threadCount,
			Supplier<Scheduler> customSchedulerSupplier) {
		this.threadingModel = threadingModel;
		this.executorType = executorType;
		this.threadCount = threadCount;
		this.customSchedulerSupplier = customSchedulerSupplier;
	}

	public static SchedulerSettings singleThreaded() {
		return new SchedulerSettings(ThreadingModel.SINGLE_THREADED, null, 0, null);
	}

	public static SchedulerSettings multiThreaded(ExecutorType executorType, int threadCount) {
		return new SchedulerSettings(ThreadingModel.MULTI_THREADED, executorType, threadCount, null);
	}

	public static SchedulerSettings customScheduler(Supplier<Scheduler> customSchedulerSupplier) {
		return new SchedulerSettings(ThreadingModel.MULTI_THREADED, ExecutorType.CUSTOM, 0, customSchedulerSupplier);
	}

	public static SchedulerSettings virtualThreads() {
		return new SchedulerSettings(ThreadingModel.MULTI_THREADED, ExecutorType.VIRTUAL_THREAD, 0, null);
	}

	public Scheduler build() {
		if (threadingModel == ThreadingModel.SINGLE_THREADED) {
			return Schedulers.singleThreaded();
		} else {
			return switch (executorType) {
				case FORK_JOIN -> Schedulers.forkJoin();
				case THREAD_POOL -> Schedulers.threadPool(threadCount);
				case VIRTUAL_THREAD -> Schedulers.virtualThreads();
				case WORK_STEALING_POOL -> Schedulers.workStealing(threadCount);
				case CUSTOM -> customSchedulerSupplier.get();
			};
		}
	}
}

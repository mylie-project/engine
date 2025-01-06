package mylie.core;

import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import mylie.core.async.Scheduler;
import mylie.core.async.Schedulers;

@Getter(AccessLevel.PACKAGE)
public class SchedulerSettings implements EngineModuleSettings<Scheduler> {
	public enum ThreadingModel {
		SingleThreaded, MultiThreaded,
	}

	public enum ExecutorType {
		ForkJoin, ThreadPool, VirtualThread, WorkStealingPool, Custom
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
		return new SchedulerSettings(ThreadingModel.SingleThreaded, null, 0, null);
	}

	public static SchedulerSettings multiThreaded(ExecutorType executorType, int threadCount) {
		return new SchedulerSettings(ThreadingModel.MultiThreaded, executorType, threadCount, null);
	}

	public static SchedulerSettings customScheduler(Supplier<Scheduler> customSchedulerSupplier) {
		return new SchedulerSettings(ThreadingModel.MultiThreaded, ExecutorType.Custom, 0, customSchedulerSupplier);
	}

	public static SchedulerSettings virtualThreads() {
		return new SchedulerSettings(ThreadingModel.MultiThreaded, ExecutorType.VirtualThread, 0, null);
	}

	@Override
	public Scheduler build() {
		if (threadingModel == ThreadingModel.SingleThreaded) {
			return Schedulers.singleThreaded();
		} else {
			return switch (executorType) {
				case ForkJoin -> Schedulers.forkJoin();
				case ThreadPool -> Schedulers.threadPool(threadCount);
				case VirtualThread -> Schedulers.virtualThreads();
				case WorkStealingPool -> Schedulers.workStealing(threadCount);
				case Custom -> customSchedulerSupplier.get();
				default -> throw new IllegalStateException("Unexpected value: " + executorType);
			};
		}
	}
}

package mylie.core.component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import mylie.core.application.Application;
import mylie.core.async.*;
import mylie.util.Void;

public class Components {
	public interface AppComponent extends Component {
	}

	public interface CoreComponent extends AppComponent {
	}

	public interface AddRemove {
		void onAdded();
		void onRemoved();
	}

	public interface Initializable {
		void onInitialize();
		void onShutdown();
	}

	public interface Updateable {
		void onUpdate();
	}

	public interface Enableable {
		void onEnable();
		void onDisable();
		default void setEnabled(boolean enabled) {
			if (this instanceof Base base) {
				base.enabledRequest = enabled;
			}
		}
	}
	@Slf4j
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	@Accessors(chain = false)
	static sealed class Base implements Component permits Core, App {
		private ComponentManager componentManager;
		private final List<Supplier<Result<Void>>> updateDependecies = new CopyOnWriteArrayList<>();
		private final List<Supplier<Result<Void>>> shutdownDependecies = new CopyOnWriteArrayList<>();
		private Async.ExecutionMode executionMode = Async.ExecutionMode.ASYNC;
		private Cache cache = Caches.OneFrame;
		private Async.Target target = Async.Target.Any;
		private boolean enabled = false, enabledRequest = false;
		private boolean initialized = false;

		public Base() {
			if (!(this instanceof Updateable)) {
				this.cache = Caches.Forever;
				this.executionMode = Async.ExecutionMode.DIRECT;
			}
		}

		void onAdd(ComponentManager componentManager) {
			this.componentManager = componentManager;
			if (this instanceof AddRemove) {
				((AddRemove) this).onAdded();
			}
		}

		void onRemove() {
			if (this instanceof AddRemove) {
				((AddRemove) this).onRemoved();
			}
			this.componentManager = null;
		}

		public Result<Void> update() {
			return Async.async(executionMode, target, cache, -1, Update, this);
		}

		Result<Void> shutdown() {
			return Async.async(executionMode, target, cache, -1, Shutdown, this);
		}

		public void componentDependecies(Component... dependecies) {
			for (Component dependecy : dependecies) {
				if (dependecy instanceof Base dependecyBase) {
					updateDependency(dependecyBase::update);
					shutdownDependency(dependecyBase::shutdown);
				}
			}
		}

		public void updateDependency(Supplier<Result<Void>> dependency) {
			updateDependecies.add(dependency);
		}

		public void shutdownDependency(Supplier<Result<Void>> dependency) {
			shutdownDependecies.add(dependency);
		}

		private static final Function.F1<Base, Void> Update = new Function.F1<>("Update") {
			@Override
			protected Void apply(Base base) {
				Wait.wait(Async.async(base.updateDependecies()));
				if (!base.initialized) {
					base.initialized = true;
					if (base instanceof Initializable initializable) {
						initializable.onInitialize();
					}
				}
				if (base instanceof Enableable enableable && base.enabled() != base.enabledRequest()) {
					base.enabled = base.enabledRequest;
					if (base.enabled()) {
						enableable.onEnable();
					} else {
						enableable.onDisable();
					}
				}
				if (base instanceof Updateable updateable) {
					updateable.onUpdate();
				}
				return Void.VOID;
			}
		};

		private static final Function.F1<Base, Void> Shutdown = new Function.F1<>("Shutdown") {
			@Override
			protected Void apply(Base base) {
				Wait.wait(Async.async(base.shutdownDependecies));
				base.initialized = false;
				if (base instanceof Initializable initializable) {
					initializable.onShutdown();
				}
				return Void.VOID;
			}
		};
	}

	@Slf4j
	public non-sealed abstract static class Core extends Base implements CoreComponent {

		protected <T extends Component> T component(Class<T> type) {
			return componentManager().component(type);
		}

		protected <T extends Component> void component(T component) {
			componentManager().component(component);
		}

		protected <T extends AppComponent> void removeComponent(T component) {
			componentManager().removeComponent(component);
		}

		protected ComponentManager componentManager() {
			return super.componentManager;
		}

		protected void cache(Cache cache) {
			super.cache = cache;
		}

		protected void target(Async.Target target) {
			super.target(target);
		}

		protected void executionMode(Async.ExecutionMode executionMode) {
			super.executionMode(executionMode);
		}
	}

	@Slf4j
	sealed abstract static class App extends Base implements AppComponent permits AppSequential, AppParallel {
		protected <T extends AppComponent> T component(Class<T> type) {
			return componentManager().component(type);
		}

		protected <T extends AppComponent> void component(T component) {
			componentManager().component(component);
		}

		protected <T extends AppComponent> void removeComponent(T component) {
			if (!(component instanceof CoreComponent)) {
				componentManager().removeComponent(component);
				return;
			}
			log.warn("Removing Core component {} from App component", component.getClass().getSimpleName());
		}
	}

	public non-sealed abstract static class AppSequential extends App {
		protected AppSequential() {
			target(Application.TARGET);
		}

		@Override
		void onAdd(ComponentManager componentManager) {
			super.onAdd(componentManager);
			updateDependency(Stages.PreUpdateLogic::execute);
		}
	}

	public non-sealed abstract static class AppParallel extends App {
		@Override
		void onAdd(ComponentManager componentManager) {
			super.onAdd(componentManager);
			Stages.PostRender.updateDependency(this::update);
		}
	}
}

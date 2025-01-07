package mylie.core.component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.*;

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
	@Setter(AccessLevel.PACKAGE)
	@Getter(AccessLevel.PACKAGE)
	static sealed class Base implements Component permits Core, App {
		private ComponentManager componentManager;
		private final List<Base> dependencies = new CopyOnWriteArrayList<>();
		private Async.ExecutionMode executionMode = Async.ExecutionMode.Async;
		private Cache cache = Caches.OneFrame;
		private Async.Target target = Async.Target.Any;
		private boolean enabled = false, enabledRequest = false;
		private boolean initialized = false;

		public Base() {
			if (!(this instanceof Updateable)) {
				this.cache = Caches.Forever;
				this.executionMode = Async.ExecutionMode.Direct;
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

		public Result<?> update() {
			return Async.async(executionMode, target, cache, -1, Update, this);
		}

		Result<?> shutdown() {
			return Async.async(executionMode, target, cache, -1, Shutdown, this);
		}

		private static final Function.F1<Base, Boolean> Update = new Function.F1<>("Update") {
			@Override
			protected Boolean apply(Base base) {
				for (Base dependency : base.dependencies()) {
					dependency.update().result();
				}
				if (!base.initialized) {
					base.initialized = true;
					if (base instanceof Initializable initializable) {
						initializable.onInitialize();
					}
				}
				if (base instanceof Enableable enableable) {
					if (base.enabled() != base.enabledRequest()) {
						base.enabled = base.enabledRequest;
						if (base.enabled()) {
							enableable.onEnable();
						} else {
							enableable.onDisable();
						}
					}
				}
				if (base instanceof Updateable updateable) {
					updateable.onUpdate();
				}
				return true;
			}
		};

		private static final Function.F1<Base, Boolean> Shutdown = new Function.F1<>("Shutdown") {
			@Override
			protected Boolean apply(Base base) {
				for (Base dependency : base.dependencies()) {
					dependency.shutdown().result();
				}
				base.initialized = false;
				if (base instanceof Initializable initializable) {
					initializable.onShutdown();
				}
				return true;
			}
		};
	}

	@Slf4j
	public non-sealed static abstract class Core extends Base implements CoreComponent {

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

		protected Core cache(Cache cache) {
			super.cache(cache);
			return this;
		}

		protected Core target(Async.Target target) {
			super.target(target);
			return this;
		}

		protected Core executionMode(Async.ExecutionMode executionMode) {
			super.executionMode(executionMode);
			return this;
		}
	}

	@Slf4j
	sealed static abstract class App extends Base implements AppComponent permits AppSequential, AppParallel {
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

	public static non-sealed abstract class AppSequential extends App {

	}

	public static non-sealed abstract class AppParallel extends App {
		@Override
		void onAdd(ComponentManager componentManager) {
			super.onAdd(componentManager);
			Stages.PostRender.addDependency(this::update);
		}
	}
}

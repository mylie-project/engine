package mylie.core.component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import mylie.core.async.Result;

@Slf4j
public class ComponentManager {
	private final List<Component> components = new CopyOnWriteArrayList<>();

	public <T extends Component> T component(Class<T> componentClass) {
		for (Component component : components) {
			if (componentClass.isInstance(component)) {
				return componentClass.cast(component);
			}
		}
		return null;
	}

	public <T extends Component> ComponentManager component(T component) {
		log.debug("Adding component {}", component.getClass().getSimpleName());
		components.add(component);
		if (component instanceof Components.Base base) {
			base.onAdd(this);
		}
		return this;
	}

	public <T extends Component> List<T> components(Class<T> type) {
		List<T> result = new LinkedList<>();
		for (Component component : this.components) {
			if (type.isInstance(component)) {
				result.add(type.cast(component));
			}
		}
		return result;
	}

	public boolean removeComponent(Component component) {
		boolean remove = components.remove(component);
		if (remove && component instanceof Components.Base base) {
			base.onRemove();
		}
		return remove;
	}

	public void onUpdate() {
		List<Result<?>> results = new LinkedList<>();
		for (Components.AppParallel component : components(Components.AppParallel.class)) {
			results.add(component.update());
		}
		results.add(Stages.PostRender.execute());
		results.forEach(Result::result);
	}

	public void onShutdown() {
		List<Result<?>> results = new LinkedList<>();
		for (Component component : components) {
			if (component instanceof Components.Base base) {
				results.add(base.shutdown());
			}
		}
		results.forEach(Result::result);
	}

}

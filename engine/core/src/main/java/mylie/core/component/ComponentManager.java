package mylie.core.component;

import lombok.extern.slf4j.Slf4j;
import mylie.core.components.time.Timer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class ComponentManager {
    private final List<Component> components=new CopyOnWriteArrayList<>();

    public <T extends Component> T component(Class<T> componentClass){
        for (Component component : components) {
            if(componentClass.isInstance(component)){
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public <T extends Component> ComponentManager component(T component){
        log.debug("Adding component {}", component.getClass().getSimpleName());
        components.add(component);
        if(component instanceof Components.Base base){
            base.onAdd(this);
        }
        return this;
    }

    public boolean removeComponent(Component component){
        boolean remove = components.remove(component);
        if(remove && component instanceof Components.Base base){
            base.onRemove();
        }
        return remove;
    }

    public void onUpdate(Timer.Time time) {

    }

    public void onShutdown() {

    }
}

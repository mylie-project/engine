package mylie.core.component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

public class Components {
    public interface AppComponent extends Component {

    }

    public interface CoreComponent extends AppComponent {}

    @Getter(AccessLevel.PACKAGE)
    static sealed class  Base implements Component permits Core,App {
        private ComponentManager componentManager;

        void onAdd(ComponentManager componentManager){
            this.componentManager=componentManager;
        }

        public void onRemove() {
            this.componentManager=null;
        }
    }

    @Slf4j
    public non-sealed static abstract class Core extends Base implements CoreComponent{
        protected <T extends Component> T component(Class<T> type){
            return componentManager().component(type);
        }

        protected <T extends Component> void component(T component){
            componentManager().component(component);
        }

        protected <T extends AppComponent> void removeComponent(T component){
            componentManager().removeComponent(component);
        }
    }

    @Slf4j
    sealed static abstract class App extends Base implements AppComponent permits AppSequential,AppParallel{
        protected <T extends AppComponent> T component(Class<T> type){
            return componentManager().component(type);
        }

        protected <T extends AppComponent> void component(T component){
            componentManager().component(component);
        }

        protected <T extends AppComponent> void removeComponent(T component){
            if(!(component instanceof CoreComponent)){
                componentManager().removeComponent(component);
                return;
            }
            log.warn("Removing Core component {} from App component", component.getClass().getSimpleName());
        }
    }

    public static non-sealed abstract class AppSequential extends App {

    }

    public static non-sealed abstract class AppParallel extends App {

    }
}

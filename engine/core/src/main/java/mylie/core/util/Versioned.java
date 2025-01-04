package mylie.core.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter(AccessLevel.PROTECTED)
@Getter
public class Versioned<T> {
    private T value;
    private long version;

    public void value(T newValue,long version){
        this.value=newValue;
        this.version=version;
    }

    public final Reference<T> reference(){
        return new Reference<>(this,value,version);
    }

    @Getter
    @AllArgsConstructor
    public static final class Reference<T>{
        @Getter(AccessLevel.NONE)
        final Versioned<T> versioned;
        T value;
        long version;

        public boolean changed(){
            return versioned.version() != version;
        }

        public boolean update(){
            boolean changed = changed();
            if(changed){
                this.value= versioned.value();
                this.version=versioned.version();
            }
            return changed;
        }

        public T value(boolean update){
            if(update){
                update();
            }
            return value;
        }
    }
}

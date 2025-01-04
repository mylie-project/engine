package mylie.core.util;

public class Flags {
    int flags;
    public void set(int flag){
        flags |= flag;
    }

    public boolean isSet(int flag){
        return (flags & flag) != 0;
    }

    public void clear(int flag){
        flags &= ~flag;
    }
}

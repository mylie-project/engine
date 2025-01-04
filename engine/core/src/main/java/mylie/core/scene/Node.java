package mylie.core.scene;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter(AccessLevel.PACKAGE)
public class Node extends Spatial implements Spatial.Translatable, Spatial.Rotatable, Spatial.ScalableUniform{
    private Set<Spatial> children=new HashSet<>();

    public void child(Spatial ... children){
        for (Spatial child : children) {
            child.parent(this);
            this.children.add(child);
            child.onLocalTransformChanged();
        }
    }

    public void removeChild(Spatial child){
        children.remove(child);
        onWorldBoundsChanged();
    }
}

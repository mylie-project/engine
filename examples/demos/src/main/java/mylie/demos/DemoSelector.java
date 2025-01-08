package mylie.demos;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import mylie.core.component.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DemoSelector extends Components.AppParallel implements Components.Initializable{
    private Map<String, List<Demo>> loadedDemos=new HashMap<>();
    @Override
    public void onInitialize() {
        log.info("Scanning for demos...");
        ClassGraph classGraph = new ClassGraph()
                .enableAllInfo()
                .acceptPackages("mylie.demos")
                .enableClassInfo();
        try (ScanResult scanResult = classGraph.scan()) {
            for (ClassInfo subclass : scanResult.getSubclasses(Demo.class)) {
                String category=subclass.getName().substring(0, subclass.getName().lastIndexOf("."));
                category=category.replace("mylie.demos.","");
                log.info("Found demo: {} in category: {}", subclass.getSimpleName(),category);
            }
        }
    }

    @Override
    public void onShutdown() {

    }
}

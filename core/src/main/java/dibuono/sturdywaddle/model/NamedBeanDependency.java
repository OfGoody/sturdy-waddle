package dibuono.sturdywaddle.model;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class NamedBeanDependency {

    private final String name;
    private final Class<?> clazz;
    private final List<Annotation> annotations;
    private final boolean isInterface;

    public NamedBeanDependency(String name,
                               Class<?> clazz,
                               List<Annotation> annotations,
                               boolean isInterface){
        this.name = name;
        this.clazz = clazz;
        this.annotations = annotations;
        this.isInterface = isInterface;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<Class<?>> getAnnotationsAsClasses() {
        return annotations.stream().map(Annotation::annotationType).collect(Collectors.toList());
    }
    public boolean isInterface() {
        return isInterface;
    }
}

package dibuono.sturdywaddle.model;

import dibuono.sturdywaddle.annotation.SturdyWaddleBean;
import dibuono.sturdywaddle.annotation.SturdyWaddleInject;
import dibuono.sturdywaddle.annotation.SturdyWaddleQualifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NamedBean {
    private final Class<?> clazz;
    private final String name;
    private final Constructor<?> constructor;
    private final List<NamedBeanDependency> dependencies;
    private final ScopeType scopeType;
    private final List<Class<?>> implementedInterfaces;
    private NamedBeanCreationQueue creationQueue;
    private Object singletonInstance = null;

    private NamedBean(Class<?> clazz,
                      String name,
                      Constructor<?> constructor,
                      List<NamedBeanDependency> namedBeanDependencies,
                      ScopeType scopeType,
                      List<Class<?>> implementedInterfaces) {
        this.clazz = clazz;
        this.name = name;
        this.constructor = constructor;
        this.dependencies = namedBeanDependencies;
        this.scopeType = scopeType;
        this.implementedInterfaces = implementedInterfaces;
        this.creationQueue = NamedBeanCreationQueue.EMPTY;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public List<NamedBeanDependency> getDependencies() {
        return dependencies;
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    public List<Class<?>> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public NamedBeanCreationQueue getCreationQueue() {
        return creationQueue;
    }

    public void setCreationQueue(NamedBeanCreationQueue creationQueue) {
        this.creationQueue = creationQueue;
    }

    public Object getSingletonInstance() {
        return singletonInstance;
    }

    public void setSingletonInstance(Object singletonInstance) {
        this.singletonInstance = singletonInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NamedBean bean = (NamedBean) o;
        return Objects.equals(clazz, bean.clazz) && Objects.equals(name, bean.name) && Objects.equals(constructor, bean.constructor) && Objects.equals(dependencies, bean.dependencies) && scopeType == bean.scopeType && Objects.equals(implementedInterfaces, bean.implementedInterfaces) && Objects.equals(creationQueue, bean.creationQueue) && Objects.equals(singletonInstance, bean.singletonInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, name, constructor, dependencies, scopeType, implementedInterfaces, creationQueue, singletonInstance);
    }

    public boolean implementInterface(Class<?> interfaceClass){
        return this.implementedInterfaces.contains(interfaceClass);
    }
    public static class NamedBeanBuilder {
        private Class<?> clazz;

        public NamedBeanBuilder withClass(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }

        public NamedBean build() {
            Constructor<?> constructor = getConstructor();
            return new NamedBean(clazz,
                    getName(),
                    constructor,
                    getDependencies(constructor),
                    getScopeType(),
                    getImplementedInterfaces());
        }

        private Constructor<?> getConstructor() {
            final Constructor<?> constructor;
            Constructor<?>[] classConstructors = clazz.getConstructors();

            if (classConstructors.length == 0) {
                throw new IllegalArgumentException("Missing constructor on bean " + clazz.getName());
            }

            if (classConstructors.length > 1) {
                List<Constructor<?>> annotatedConstructors = Arrays.stream(classConstructors)
                        .filter(it -> it.isAnnotationPresent(SturdyWaddleInject.class))
                        .collect(Collectors.toList());

                if (annotatedConstructors.size() == 0) {
                    throw new IllegalArgumentException("Too many constructors for bean " + clazz.getName());
                } else if (annotatedConstructors.size() > 1) {
                    throw new IllegalArgumentException("Too many annotated constructors for bean " + clazz.getName());
                } else {
                    constructor = annotatedConstructors.get(0);
                }
            } else {
                constructor = classConstructors[0];
            }
            return constructor;
        }

        private String getName() {
            String value = clazz.getAnnotation(SturdyWaddleBean.class).value();

            if (value == null || value.isBlank()) {
                return clazz.getName();
            } else {
                return value;
            }
        }

        private List<NamedBeanDependency> getDependencies(Constructor<?> constructor) {
            Parameter[] params = constructor.getParameters();

            List<NamedBeanDependency> dependencyList = new ArrayList<>();
            for (Parameter p : params) {
                String dependencyName;

                if (p.isAnnotationPresent(SturdyWaddleQualifier.class)) {
                    dependencyName = ((SturdyWaddleQualifier) p.getAnnotation(SturdyWaddleQualifier.class)).value();
                } else {
                    dependencyName = p.getType().getName();
                }

                dependencyList.add(new NamedBeanDependency(
                        dependencyName,
                        p.getType(),
                        Arrays.stream(p.getAnnotations()).collect(Collectors.toList()),
                        p.getType().isInterface()));
            }

            return dependencyList;
        }

        private ScopeType getScopeType() {
            return clazz.getAnnotation(SturdyWaddleBean.class).scope();
        }

        private List<Class<?>> getImplementedInterfaces() {
            return Arrays.stream(clazz.getInterfaces()).collect(Collectors.toList());
        }
    }
}

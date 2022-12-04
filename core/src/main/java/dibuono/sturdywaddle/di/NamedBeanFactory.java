package dibuono.sturdywaddle.di;

import dibuono.sturdywaddle.model.NamedBean;
import dibuono.sturdywaddle.model.NamedBeanCreationQueue;
import dibuono.sturdywaddle.model.NamedBeanDependency;
import dibuono.sturdywaddle.model.ScopeType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

class NamedBeanFactory {

    public static Object createInstanceOfNamedBean(NamedBean namedBean) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (namedBean.getCreationQueue().isToInit()) {
            initializeNamedBeanCreationQueue(namedBean);
        }

        if (namedBean.getScopeType() == ScopeType.SINGLETON) {
            if (namedBean.getSingletonInstance() == null) {
                Object instance = createInstanceOfNamedBeanInner(namedBean);
                namedBean.setSingletonInstance(instance);
            }

            return namedBean.getSingletonInstance();
        }

        return createInstanceOfNamedBeanInner(namedBean);
    }

    private static void initializeNamedBeanCreationQueue(NamedBean namedBean) {
        List<NamedBean> dependenciesList = new ArrayList<>();

        for (NamedBeanDependency namedBeanDependency : namedBean.getDependencies()) {
            NamedBean currentBean = SturdyWaddleContainer.getNamedBeanFromDependencyOrNull(namedBeanDependency);

            if (currentBean == null) {
                throw new IllegalStateException(String.format("Unable to find NamedBean for the dependency %s", namedBeanDependency.getName()));
            }

            initializeNamedBeanCreationQueueInner(new HashSet<>(Collections.singletonList(namedBean)), dependenciesList, currentBean);
            namedBean.setCreationQueue(new NamedBeanCreationQueue(dependenciesList));
        }

        dependenciesList.add(namedBean);
    }

    private static void initializeNamedBeanCreationQueueInner(HashSet<NamedBean> pathStack, List<NamedBean> dependenciesList, NamedBean currentBean) {
        if (pathStack.contains(currentBean)) {
            throw new IllegalStateException(String.format("Unable to find a path without cycle to instantiate bean %s", currentBean.getName()));
        }

        pathStack.add(currentBean);
        for (NamedBeanDependency dependency : currentBean.getDependencies()) {
            NamedBean dependencyBean = SturdyWaddleContainer.getNamedBeanFromDependencyOrNull(dependency);

            if (dependencyBean == null) {
                throw new IllegalStateException(String.format("Unable to find NamedBean for the bean %s", dependency.getName()));
            }

            initializeNamedBeanCreationQueueInner(pathStack, dependenciesList, dependencyBean);
        }

        dependenciesList.add(currentBean);
    }

    private static Object createInstanceOfNamedBeanInner(NamedBean namedBean) throws InvocationTargetException,
            InstantiationException,
            IllegalAccessException {
        Stack<Object> instanceStack = new Stack<>();

        for (NamedBean beanToCreate : namedBean.getCreationQueue()) {
            List<Class<?>> paramsByType = Arrays.stream(beanToCreate.getConstructor().getParameters())
                    .map(Parameter::getType)
                    .collect(Collectors.toList());

            Object[] params = new Object[paramsByType.size()];

            for (int i = 0; i < paramsByType.size(); i++) {
                params[i] = instanceStack.pop();
            }

            params = Arrays.stream(params).sorted((Object pa, Object pb) -> {
                int paPos = paramsByType.indexOf(pa.getClass());
                int pbPos = paramsByType.indexOf(pb.getClass());

                return paPos - pbPos;
            }).toArray();

            instanceStack.push(beanToCreate.getConstructor().newInstance(params));
        }

        return instanceStack.pop();
    }
}

package dibuono.sturdywaddle.di;

import dibuono.sturdywaddle.annotation.SturdyWaddleBean;
import dibuono.sturdywaddle.annotation.SturdyWaddleQualifier;
import dibuono.sturdywaddle.model.NamedBean;
import dibuono.sturdywaddle.model.NamedBeanDependency;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SturdyWaddleContainer {
    private static final List<NamedBean> namedBeansContainer = new ArrayList<>();

    public static void loadContainer(Class<?> mainClass) {
        String basePackage = Arrays.stream(mainClass.getPackage().getName().split("\\."))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to obtain the package from main class"));

        List<NamedBean> namedBeans = new Reflections(basePackage).getTypesAnnotatedWith(SturdyWaddleBean.class)
                .stream().map(it -> new NamedBean.NamedBeanBuilder()
                        .withClass(it)
                        .build())
                .collect(Collectors.toList());

        namedBeansContainer.addAll(namedBeans);
    }

    public static Object getInstance(Class<?> beanClass) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        List<NamedBean> matchingBeans = namedBeansContainer.stream()
                .filter(it -> it.getClazz().equals(beanClass))
                .collect(Collectors.toList());

        if (matchingBeans.size() == 0) {
            throw new IllegalArgumentException(String.format("Unable to find a bean with class %s", beanClass));

        }

        if (matchingBeans.size() > 1) {
            throw new IllegalArgumentException(String.format("Found more than one bean with class %s", beanClass));
        }

        return NamedBeanFactory.createInstanceOfNamedBean(matchingBeans.get(0));
    }

    public static Object getInstance(String beanName) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        List<NamedBean> matchingBeans = namedBeansContainer.stream()
                .filter(it -> it.getName().equals(beanName))
                .collect(Collectors.toList());

        if (matchingBeans.size() == 0) {
            throw new IllegalArgumentException(String.format("Unable to find a bean with name %s", beanName));

        }

        if (matchingBeans.size() > 1) {
            throw new IllegalArgumentException(String.format("Found more than one bean with name %s", beanName));
        }

        return NamedBeanFactory.createInstanceOfNamedBean(matchingBeans.get(0));
    }

    protected static NamedBean getNamedBeanFromDependencyOrNull(NamedBeanDependency namedBeanDependency) {
        List<NamedBean> matchingBeans = new ArrayList<>();

        if (namedBeanDependency.isInterface() &&
                !namedBeanDependency.getAnnotationsAsClasses().contains(SturdyWaddleQualifier.class)) {
                matchingBeans.addAll(searchDependencyByInterface(namedBeanDependency.getClazz()));

        } else if(namedBeanDependency.getAnnotationsAsClasses().contains(SturdyWaddleQualifier.class)){
            matchingBeans.addAll(searchDependencyByName(namedBeanDependency.getName()));
        }else{
            matchingBeans.addAll(searchDependencyByClass(namedBeanDependency.getClazz()));
        }

        if (matchingBeans.size() == 0) {
            throw new IllegalArgumentException(String.format("Unable to find a bean with name %s", namedBeanDependency.getName()));

        }

        if (matchingBeans.size() > 1) {
            throw new IllegalArgumentException(String.format("Found more than one bean with name %s", namedBeanDependency.getName()));
        }

        return matchingBeans.get(0);
    }

    private static List<NamedBean> searchDependencyByInterface(Class<?> clazz) {
        List<NamedBean> matchingBeans = new ArrayList<>();

        for(NamedBean bean : namedBeansContainer){
            if(bean.implementInterface(clazz)){
                matchingBeans.add(bean);
            }
        }

        return matchingBeans;
    }

    private static List<NamedBean> searchDependencyByName(String name) {
        List<NamedBean> matchingBeans = new ArrayList<>();

        for(NamedBean bean : namedBeansContainer){
            if(bean.getName().equals(name)){
                matchingBeans.add(bean);
            }
        }

        return matchingBeans;
    }

    private static List<NamedBean> searchDependencyByClass(Class<?> clazz) {
        List<NamedBean> matchingBeans = new ArrayList<>();

        for(NamedBean bean : namedBeansContainer){
            if(bean.getClazz().equals(clazz)){
                matchingBeans.add(bean);
            }
        }

        return matchingBeans;
    }
}

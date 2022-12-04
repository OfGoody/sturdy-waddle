package sturdywaddle.demo;

import dibuono.sturdywaddle.di.SturdyWaddleContainer;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        SturdyWaddleContainer.loadContainer(Main.class);
        BeanA a = (BeanA) SturdyWaddleContainer.getInstance(BeanA.class);
        System.out.println(a);
    }
}

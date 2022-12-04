package sturdywaddle.demo;

import dibuono.sturdywaddle.annotation.SturdyWaddleBean;

@SturdyWaddleBean
public class BeanA {

    public BeanA(BeanB b, BeanC c){}

    @Override
    public String toString() {
        return "AAAA";
    }
}

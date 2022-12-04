package sturdywaddle.demo;

import dibuono.sturdywaddle.annotation.SturdyWaddleBean;
import dibuono.sturdywaddle.annotation.SturdyWaddleQualifier;

@SturdyWaddleBean
public class BeanC {
    public BeanC(@SturdyWaddleQualifier("testBean") BeanF f){}
}

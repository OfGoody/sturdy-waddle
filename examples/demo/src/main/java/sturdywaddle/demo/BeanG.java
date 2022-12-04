package sturdywaddle.demo;

import dibuono.sturdywaddle.annotation.SturdyWaddleBean;
import dibuono.sturdywaddle.model.ScopeType;

@SturdyWaddleBean(scope = ScopeType.PROTOTYPE)
public class BeanG implements InterfaceA{
}

package dibuono.sturdywaddle.annotation;

import dibuono.sturdywaddle.model.ScopeType;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface SturdyWaddleBean {
    public String value() default "";
    public ScopeType scope() default ScopeType.SINGLETON;
}

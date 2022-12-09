# Sturdy-Waddle
This repository contains a simple attempt to implement a runtime-time Dependency Injector framework in Java.

## Expected Usage


```Java
@SturdyWaddleBean
class BeanName{
	...
}
```

It is possible also to assign a specific name to the bean using the value attribute.
```Java
@SturdyWaddleBean(value = "aVerySpecificBean", scope = PROTOTYPE)
class BeanName{
	...
}
```

```Java
@SturdyWaddleBean(value = "aVerySpecificBean", scope = SINGLETON)
class BeanName{
	...
}
```

Constructor injection: 

```Java
@SturdyWaddleInject 
public Client(bean: BeanName){
	...
}
```
Method injection:
```Java
@SturdyWaddleInject
public void method(bean: BeanName){
	...
}
```

It is also possible to specifically say the name of the bean to inject:

```Java

@SturdyWaddleInject
class Client(@SturdyWaddleQualifier("aVerySpecificBean") bean: BeanName){
	...
}
```

# Strudy-Waddle
This repository contains a simple attempt to implement a compile-time Dependency Injector framework in Kotlin.

## Expected Usage
The idea is to create a DI framework that follow (or at least try to follow) the JSR-330 specification. 

Definition of an injectable bean:

```kotlin
@Named
class BeanName(){
	...
}
```

It is possible also to assign a specific name to the bean using the value attribute.
```kotlin
@Named("aVeryInterestingBean")
class BeanName(){
	...
}
```

Constructor injection: 

```kotlin
@Inject 
class Client(bean: BeanName){
	...
}
```
Method injection:
```kotlin
@Inject
fun method(bean: BeanName){
	...
}
```

By default a new instance of the BeanName is created for each invocation on the @Inject annotation on it. 
The @Scope annotation allow the future re-use of an instance of the bean, while the @Singleton annotation allow the definition of a singleton.

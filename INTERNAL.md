# Internal Working
We can define an AnnotationProcessor that will be called during the maven build process.

The AnnotationProcesso will be responsible of:
* Scan the packages for all the **@Named** classes
* Scan the packages for all the **@Inject** annotations

We can create a graph of all **@Named** and **@Inject** and try to get a topological sort. As soon we have this kind of ordering we can start to create the single instances.

For the moment we are going to assume that all the instance are marked as **@Singleton**

## SturdyWaddleContainer
An instance of this class will contain all the singleton instances of the various beans create using the **@Named** annotation.
The interface will be something like this:
```kotlin
interface SturdyWaddleContainer{
    companion object {
        fun <T> getInstance(beanName: String): T
        fun <T> getInstance(beanClass: Class<T>): T
    }
}
```
## Constructor injection
```kotlin
	@Inject
	class Client(service: Service){
		...
	}
```





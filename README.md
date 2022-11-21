# Strudy-Waddle
This repository contains a simple attempt to implement a compile-time Dependency Injector framework in Kotlin.

## Expected Usage
Constructor injection: 

```kotlin
	@Inject 
	class Client(service: Service){
		...
	}
```
Setter injection:
```kotlin
	var service: Service? = null
	@Inject
	set(s){
		service = s
	}
```
Service definition:
```kotlin
@Injectable
class Service(...){
	...
}
```

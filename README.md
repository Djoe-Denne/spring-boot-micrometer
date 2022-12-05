# spring-boot-micrometer

## About

Autoconfiguration class to declare micrometer Aspect to handler `@Timed` and `@Counted` Annotation.
Aspect beans are directly declared with a `tagsBasedOnJoinPoint` which allow to add dynamic tags where tag value must be [SpEL](https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html) expression 

## Requirements

* [Java 17](https://formulae.brew.sh/formula/openjdk@17)
* [Spring boot (Tested 3.0.0)](https://spring.io/projects/spring-boot)
* [Micrometer-core](https://github.com/micrometer-metrics/micrometer)

## Setup

Just add `MicrometerAspectConfiguration` to your application configuration

## Example

> :warning: It doesn't provide bean for `MeterRegistry`
```java
@Timed(value = "my-timer", extraTags = {"static-class-tag", "static-value"})
@DynamicTag(key = "dynamic-class-tag", value = "#foo.getProperty()")
public class FakeBeanOneOnClass {

    @Counted(value = "my-counter", extraTags = {"static-method-tag", "static-value"})
    @DynamicTag(key = "dynamic-method-tag-1", value = "#foo.getSecondProperty()")
    @DynamicTag(key = "dynamic-method-tag-2", value = "#foo.getThirdProperty()")
    public void foo(Foo foo) {

    }
    
    @Counted(value = "my-second-counter", extraTags = {"static-second-method-tag", "static-value"})
    @DynamicTag(key = "dynamic-second-method-tag-1", value = "#foo.getSecondProperty()")
    @DynamicTag(key = "dynamic-second-method-tag-2", value = "#foo.getThirdProperty()")
    public void foo2(Foo foo) {

    }
}
```

Here all declared `@DynamicTag` at class level will be resolved and added to timer and counter
All declared `@DynamicTag` at method level will be resolved and added to timer and counter declared on the same method

If we call `foo` a new timing and counting will be registered with following tags :
* `static-class-tag`
* `dynamic-class-tag`
* `static-method-tag`
* `dynamic-method-tag-1`
* `dynamic-method-tag-2`

If we call `foo2` a new timing and counting will be registered with following tags :
* `static-class-tag`
* `dynamic-class-tag`
* `static-second-method-tag`
* `dynamic-second-method-tag-1`
* `dynamic-second-method-tag-2`

## Add JoinPoint tags resolver

By implementing `JoinPointTagResolver` and declaring it has a bean, it will automatically be used by Micrometric Aspect. 
You can declare how many you want.

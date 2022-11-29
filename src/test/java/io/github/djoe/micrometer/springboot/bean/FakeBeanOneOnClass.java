package io.github.djoe.micrometer.springboot.bean;

import io.github.djoe.micrometer.springboot.DynamicTag;
import io.github.djoe.micrometer.springboot.Foo;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

@Timed(value = "my-timer", extraTags = {"my-static-tag", "static-value"})
@DynamicTag(key = "dynamic-class-tag", value = "#foo.getProperty()")
public class FakeBeanOneOnClass {

    @Counted(value = "my-counter", extraTags = {"my-static-tag", "static-value"})
    @DynamicTag(key = "dynamic-method-tag-1", value = "#foo.getSecondProperty()")
    @DynamicTag(key = "dynamic-method-tag-2", value = "#foo.getThirdProperty()")
    public void foo(Foo foo) {

    }
}

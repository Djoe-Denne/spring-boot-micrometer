package io.github.djoe.micrometer.springboot.bean;

import io.github.djoe.micrometer.springboot.DynamicTag;
import io.github.djoe.micrometer.springboot.Foo;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;


@Timed(value = "fakeTimer")
@DynamicTag(key = "property1", value = "#foo.getProperty1()")
public class FakeBeanOneOnClass {

    @Counted(value = "fakeCounter")
    @DynamicTag(key = "property3", value = "#foo.getProperty3()")
    public void foo(Foo foo) {

    }

    @Counted(value = "fakeCounter")
    @DynamicTag(key = "property3", value = "#foo.getProperty3()")
    @DynamicTag(key = "property4", value = "#foo.getProperty4()")
    @DynamicTag(key = "wrongProperty", value = "#foo.getWrongProperty()")
    public void foo2(Foo foo) {

    }
}

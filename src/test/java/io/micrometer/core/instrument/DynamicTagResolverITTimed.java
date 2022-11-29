package io.micrometer.core.instrument;

import io.github.djoe.micrometer.springboot.Foo;
import io.github.djoe.micrometer.springboot.bean.FakeBeanOneOnClass;
import io.github.djoe.micrometer.springboot.bean.FakeBeanTwoOnClass;
import io.github.djoe.micrometer.springboot.configuration.MicrometerAspectConfiguration;
import io.micrometer.core.instrument.DynamicTagResolverITTimed.TestConfiguration;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {MicrometerAspectConfiguration.class,
                           TestConfiguration.class
})
@ExtendWith(MockitoExtension.class)
class DynamicTagResolverITTimed {

    @Autowired
    FakeBeanTwoOnClass fakeBeanTwoOnClass;
    @Autowired
    FakeBeanOneOnClass fakeBeanOneOnClass;

    @Autowired
    MeterRegistry meterRegistry;

    @Test
    void test_tow_tag_on_class_two_on_method_all_added() {
        when(meterRegistry.config()).thenReturn(new SimpleMeterRegistry().config());
        when(meterRegistry.counter(any(Meter.Id.class))).thenReturn(mock(Counter.class));
        fakeBeanTwoOnClass.foo2(new Foo("value1", "value2", "value3", "value4"));

        ArgumentCaptor<Meter.Id> captor = ArgumentCaptor.forClass(Meter.Id.class);
        verify(meterRegistry).timer(captor.capture(), any(), any());

        Meter.Id id = captor.getValue();

        then(id.getTag("property1")).isEqualTo("value1");
        then(id.getTag("property2")).isEqualTo("value2");
        then(id.getTag("property3")).isEqualTo("value3");
        then(id.getTag("property4")).isEqualTo("value4");
    }

    @Test
    void test_tow_tag_on_class_one_on_method_all_added() {
        when(meterRegistry.config()).thenReturn(new SimpleMeterRegistry().config());
        when(meterRegistry.counter(any(Meter.Id.class))).thenReturn(mock(Counter.class));
        fakeBeanTwoOnClass.foo2(new Foo("value1", "value2", "value3", "value4"));

        ArgumentCaptor<Meter.Id> captor = ArgumentCaptor.forClass(Meter.Id.class);
        verify(meterRegistry).timer(captor.capture(), any(), any());

        Meter.Id id = captor.getValue();

        then(id.getTag("property1")).isEqualTo("value1");
        then(id.getTag("property2")).isEqualTo("value2");
        then(id.getTag("property3")).isEqualTo("value3");
    }

    @Test
    void test_one_tag_on_class_two_on_method_all_added() {
        when(meterRegistry.config()).thenReturn(new SimpleMeterRegistry().config());
        when(meterRegistry.counter(any(Meter.Id.class))).thenReturn(mock(Counter.class));
        fakeBeanOneOnClass.foo2(new Foo("value1", "value2", "value3", "value4"));

        ArgumentCaptor<Meter.Id> captor = ArgumentCaptor.forClass(Meter.Id.class);
        verify(meterRegistry).timer(captor.capture(), any(), any());

        Meter.Id id = captor.getValue();

        then(id.getTag("property1")).isEqualTo("value1");
        then(id.getTag("property3")).isEqualTo("value3");
        then(id.getTag("property4")).isEqualTo("value4");
    }

    @Test
    void test_one_tag_on_class_one_on_method_all_added() {
        when(meterRegistry.config()).thenReturn(new SimpleMeterRegistry().config());
        when(meterRegistry.counter(any(Meter.Id.class))).thenReturn(mock(Counter.class));
        fakeBeanOneOnClass.foo2(new Foo("value1", "value2", "value3", "value4"));

        ArgumentCaptor<Meter.Id> captor = ArgumentCaptor.forClass(Meter.Id.class);
        verify(meterRegistry).timer(captor.capture(), any(), any());

        Meter.Id id = captor.getValue();

        then(id.getTag("property1")).isEqualTo("value1");
        then(id.getTag("property3")).isEqualTo("value3");
        then(id.getTag("property4")).isEqualTo("value4");
    }

    @Test
    void test_wrong_one_on_method_all_added_wrong_not_added() {
        when(meterRegistry.config()).thenReturn(new SimpleMeterRegistry().config());
        when(meterRegistry.counter(any(Meter.Id.class))).thenReturn(mock(Counter.class));
        fakeBeanOneOnClass.foo2(new Foo("value1", "value2", "value3", "value4"));

        ArgumentCaptor<Meter.Id> captor = ArgumentCaptor.forClass(Meter.Id.class);
        verify(meterRegistry).timer(captor.capture(), any(), any());

        Meter.Id id = captor.getValue();

        then(id.getTag("property1")).isEqualTo("value1");
        then(id.getTag("property3")).isEqualTo("value3");
        then(id.getTag("property4")).isEqualTo("value4");
        then(id.getTag("property4")).isEqualTo("value4");
        then(id.getTag("wrongProperty")).isNull();
    }

    @Configuration
    public static class TestConfiguration {
        @MockBean
        MeterRegistry meterRegistry;

        @Bean
        FakeBeanTwoOnClass fakeBeanTwoOnClass(){
            return new FakeBeanTwoOnClass();
        }

        @Bean
        FakeBeanOneOnClass fakeBeanOneOnClass(){
            return new FakeBeanOneOnClass();
        }
    }

}

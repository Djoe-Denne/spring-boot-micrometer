package io.github.djoe.micrometer.springboot.configuration;

import io.github.djoe.micrometer.springboot.DynamicTagResolver;
import io.github.djoe.micrometer.springboot.JoinPointExpressionEvaluator;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Add micrometer Aspect to handle @Timed and @Counted annotation
 */
@Configuration
@EnableAspectJAutoProxy
public class MicrometerAspectConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public TimedAspect timedAspect(MeterRegistry registry, DynamicTagResolver dynamicTagResolver) {
        return new TimedAspect(registry, dynamicTagResolver::resolve);
    }

    @Bean
    @ConditionalOnMissingBean
    public CountedAspect countedAspect(MeterRegistry registry, DynamicTagResolver dynamicTagResolver) {
        return new CountedAspect(registry, dynamicTagResolver::resolve);
    }

    @Bean
    @ConditionalOnMissingBean
    public JoinPointExpressionEvaluator pjpExpressionEvaluator(BeanFactory beanFactory) {
        return new JoinPointExpressionEvaluator(beanFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicTagResolver dynamicTagResolver(JoinPointExpressionEvaluator joinPointExpressionEvaluator) {
        return new DynamicTagResolver(joinPointExpressionEvaluator);
    }
}

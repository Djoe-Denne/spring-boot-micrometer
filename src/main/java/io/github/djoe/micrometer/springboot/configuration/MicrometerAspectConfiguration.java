package io.github.djoe.micrometer.springboot.configuration;

import io.github.djoe.micrometer.springboot.CompositeTagResolver;
import io.github.djoe.micrometer.springboot.DynamicTagResolver;
import io.github.djoe.micrometer.springboot.JoinPointExpressionEvaluator;
import io.github.djoe.micrometer.springboot.JoinPointTagResolver;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.List;

/**
 * Add micrometer Aspect to handle @Timed and @Counted annotation
 */
@Configuration
@EnableAspectJAutoProxy
public class MicrometerAspectConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public TimedAspect timedAspect(MeterRegistry registry, CompositeTagResolver compositeTagResolver) {
        return new TimedAspect(registry, compositeTagResolver::resolve);
    }

    @Bean
    @ConditionalOnMissingBean
    public CountedAspect countedAspect(MeterRegistry registry, CompositeTagResolver compositeTagResolver) {
        return new CountedAspect(registry, compositeTagResolver::resolve);
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

    @Bean
    CompositeTagResolver compositeTagResolver(List<JoinPointTagResolver> joinPointTagResolvers) {
        return new CompositeTagResolver(joinPointTagResolvers);
    }
}

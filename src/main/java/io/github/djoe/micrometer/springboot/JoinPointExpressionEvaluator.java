package io.github.djoe.micrometer.springboot;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpEL condition evaluator, use to evaluate a SpEL by using a {@code ProceedingJoinPoint} as a context
 *
 * highly inspired from Spring {@code EventExpressionEvaluator}
 *
 * @author djoedenne
 */
@RequiredArgsConstructor
public class JoinPointExpressionEvaluator extends CachedExpressionEvaluator {

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private final @Nullable BeanFactory beanFactory;

    /**
     * Evaluate a SpEL expression for a JoinPoint
     */
    public String evaluate(String conditionExpression, JoinPoint pjp) {

        Method targetMethod = ((MethodSignature) pjp.getSignature()).getMethod();
        MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(
                pjp.getThis(), targetMethod, pjp.getArgs(), getParameterNameDiscoverer());
        if (beanFactory != null) {
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }

        return getExpression(this.conditionCache, new AnnotatedElementKey(targetMethod, pjp.getThis().getClass()), conditionExpression).getValue(
                evaluationContext, String.class);
    }

}


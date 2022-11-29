package io.github.djoe.micrometer.springboot;

import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Dynamic tag resolver, use {@link JoinPointExpressionEvaluator} ton process {@code value} attribute of all {@link DynamicTag}
 * annotation over current proceeding method and its declaring class.
 *
 * is {@code value} attribute cannot be parsed using SpEL parser, current {@link DynamicTag} is skipped
 *
 * @author djoedenne
 */
@RequiredArgsConstructor
public class DynamicTagResolver {
    private final JoinPointExpressionEvaluator joinPointExpressionEvaluator;

    /**
     * Extract {@link DynamicTag} from {@code pjp} method and it's declaring class, try to avaluate {@link DynamicTag}
     * value using SpEL parser, map them to micrometer {@code Tag} if parsing succeeded
     *
     * @param pjp
     * @return
     */
    public Iterable<Tag> resolve(ProceedingJoinPoint pjp) {

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        List<DynamicTag> tagSet = getDynamicTags(method);

        return tagSet.stream().map(tag -> buildMicrometerTag(tag, pjp)).filter(Objects::nonNull).toList();
    }

    /**
     * Try to evaluate {@code tag} value using SpEL parser
     *
     * @param tag
     * @param pjp
     * @return Micrometer Tag or null if parsing fail
     */
    protected Tag buildMicrometerTag(DynamicTag tag, ProceedingJoinPoint pjp) {
        try {
            return Tag.of(tag.key(), joinPointExpressionEvaluator.evaluate(tag.value(), pjp));
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Get {@link DynamicTag} from {@code method} and its declaring class, concat them
     *
     * @param method Method currently processed
     * @return List of all {@link DynamicTag} over {@code method} and declaring class
     */
    protected List<DynamicTag> getDynamicTags(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        DynamicTagSet methodTagSet = method.getAnnotation(DynamicTagSet.class);
        DynamicTagSet classTagSet = declaringClass.getAnnotation(DynamicTagSet.class);
        List<DynamicTag> methodTags = Arrays.asList(method.getAnnotation(DynamicTag.class));
        List<DynamicTag> classTags = Arrays.asList(declaringClass.getAnnotation(DynamicTag.class));
        // if tagSet are empty, that's mean there is les than 2 DynamicTag
        methodTags = (methodTagSet != null) ? Arrays.asList(methodTagSet.value()) : methodTags;
        classTags = (classTagSet != null) ? Arrays.asList(classTagSet.value()) : classTags;

        return Stream.concat(methodTags.stream(), classTags.stream()).filter(Objects::nonNull).toList();
    }

}

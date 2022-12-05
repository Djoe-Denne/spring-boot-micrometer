package io.github.djoe.micrometer.springboot;

import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * will execute all {@code resolve} method of declared beans of type {@link JoinPointTagResolver}
 */
@RequiredArgsConstructor
public class CompositeTagResolver implements JoinPointTagResolver {
    private final List<JoinPointTagResolver> resolvers;

    /**
     * execute resolve method of all {@link JoinPointTagResolver} beans
     *
     * @param pjp
     * @return a Set of Tag
     */
    @Override
    public Iterable<Tag> resolve(ProceedingJoinPoint pjp) {
        return resolvers.stream().flatMap(resolver -> StreamSupport.stream(resolver.resolve(pjp).spliterator(), false)).collect(Collectors.toSet());
    }
}

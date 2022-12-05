package io.github.djoe.micrometer.springboot;

import io.micrometer.core.instrument.Tag;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Tag resolver, provide tags based on ongoing {@code ProceedingJoinPoint}
 *
 * @author djoedenne
 */
public interface JoinPointTagResolver {

    /**
     * Extract {@code Tag} from {@code pjp}
     *
     * @param pjp
     * @return
     */
    Iterable<Tag> resolve(ProceedingJoinPoint pjp);

}

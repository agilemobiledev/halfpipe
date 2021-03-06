package halfpipe.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must be a {@link halfpipe.util.Duration}
 * whose value must be higher or equal to the specified minimum.
 * <p/>
 * <code>null</code> elements are considered valid
 * see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MaxDurationValidator.class)
public @interface MaxDuration {
    String message() default "must be less than or equal to {value} {unit}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default {};

    /**
     * @return value the element must be higher or equal to
     */
    long value();

    /**
     * @return unit of the value the element must be higher or equal to
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}

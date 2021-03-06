package halfpipe.validation;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates a bean predicate method as returning true. Bean predicates must be of the form
 * {@code isSomething} or they'll be silently ignored.
 * see original https://github.com/codahale/dropwizard/tree/master/dropwizard-core/src/main/java/com/yammer/dropwizard/validation
 */
@Target({TYPE, ANNOTATION_TYPE, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = MethodValidator.class)
@Documented
public @interface ValidationMethod {
    String message() default "is not valid";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}

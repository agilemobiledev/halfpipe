package halfpipe.validation.configuration;

import halfpipe.configuration.DynaProp;
import halfpipe.validation.PortRange;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Allow 0 to indicate dynamic port range allocation. If not zero, it must be within the {min,max}
 * range, inclusive.
 */
@Component
public class PortRangeValidator implements ConstraintValidator<PortRange, DynaProp<Integer>> {
    private int min;
    private int max;

    @Override
    public void initialize(PortRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(DynaProp<Integer> prop, ConstraintValidatorContext context) {
        Integer value = prop.getValue();
        return value == 0 || (value >= min && value <= max);
    }
}

package gr.uoa.di.rent.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static gr.uoa.di.rent.config.Constraint.RATING_MAX;
import static gr.uoa.di.rent.config.Constraint.RATING_MIN;

public class RatingValidator implements ConstraintValidator<Owner, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value >= RATING_MIN && value <= RATING_MAX;
    }
}

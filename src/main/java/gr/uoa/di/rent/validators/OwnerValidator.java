package gr.uoa.di.rent.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class OwnerValidator implements ConstraintValidator<Owner, String> {

    /* TODO: Add extra parameters for every case.*/

    /* TODO: Replace this with real owners*/
    List<String> owners = Arrays.asList("Santideva", "Marie Kondo", "Martin Fowler", "mkyong");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return owners.contains(value);
    }
}

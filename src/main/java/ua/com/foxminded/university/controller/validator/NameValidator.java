package ua.com.foxminded.university.controller.validator;

import ua.com.foxminded.university.annotation.NameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {

    @Override
    public void initialize(NameConstraint name) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext cxt) {
            return name != null
                    && name != ""
                    && name.matches("[a-zA-Z]+")
                    && (name.length() > 2)
                    && (name.length() < 12); }
}

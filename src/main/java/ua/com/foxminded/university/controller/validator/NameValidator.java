package ua.com.foxminded.university.controller.validator;

import ua.com.foxminded.university.annotation.NameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {

    @Override
    public void initialize(NameConstraint contactNumber) {
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext cxt) {
            return contactField != null
                    && contactField != ""
                    && contactField.matches("[a-zA-Z]+")
                    && (contactField.length() > 2)
                    && (contactField.length() < 12); }
}

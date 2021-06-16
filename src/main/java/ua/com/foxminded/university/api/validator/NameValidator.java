package ua.com.foxminded.university.api.validator;

import ua.com.foxminded.university.annotation.NameConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<NameConstraint, String> {

    private static final String EMPTY_STRING = "";
    private static final String NAME_FORMAT = "[a-zA-Z]+";
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 12;


    @Override
    public void initialize(NameConstraint name) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext cxt) {
            return name != null
                    && name != EMPTY_STRING
                    && name.matches(NAME_FORMAT)
                    && (name.length() > MIN_LENGTH)
                    && (name.length() < MAX_LENGTH); }
}

package ua.com.foxminded.university.annotation;

import ua.com.foxminded.university.controller.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NameConstraint {
    String message() default "Name can not be empty and not contain special characters and digits. Name length must be grater then 2 and less then 12";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
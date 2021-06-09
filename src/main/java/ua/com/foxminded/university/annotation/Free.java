package ua.com.foxminded.university.annotation;

import ua.com.foxminded.university.controller.validator.FreeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FreeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Free {
    String message() default "it work";

    String id();

    String date();

    String number();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        Free[] value();
    }
}
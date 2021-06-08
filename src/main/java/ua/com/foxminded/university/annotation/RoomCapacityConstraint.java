package ua.com.foxminded.university.annotation;

import ua.com.foxminded.university.controller.validator.RoomCapacityValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoomCapacityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoomCapacityConstraint {
    String message() default "Group size grater than room capacity";

    String groupId();

    String roomId();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
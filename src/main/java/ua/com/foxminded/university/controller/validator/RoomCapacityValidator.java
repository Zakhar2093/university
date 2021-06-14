package ua.com.foxminded.university.controller.validator;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.annotation.RoomCapacityConstraint;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Room;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class RoomCapacityValidator implements ConstraintValidator<RoomCapacityConstraint, Object> {

    private String groupString;
    private String roomString;

    public RoomCapacityValidator() {
    }

    public void initialize(RoomCapacityConstraint constraintAnnotation) {
        this.groupString = constraintAnnotation.group();
        this.roomString = constraintAnnotation.room();
    }

    @Override
    public boolean isValid(Object lesson, ConstraintValidatorContext cxt) {

        Group group = (Group) new BeanWrapperImpl(lesson).getPropertyValue(groupString);
        Room room = (Room) new BeanWrapperImpl(lesson).getPropertyValue(roomString);

        if (room == null || group == null){
            return true;
        }

        int roomCapacity = room.getRoomCapacity();
        int groupSize = group.getStudents().size();

        return roomCapacity >= groupSize;
    }
}

package ua.com.foxminded.university.controller.validator;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.annotation.RoomCapacityConstraint;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.StudentService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class RoomCapacityValidator implements ConstraintValidator<RoomCapacityConstraint, Object> {

    private String group;
    private String room;
    @Autowired
    private RoomService roomService;
    @Autowired
    private StudentService studentService;

    public RoomCapacityValidator() {
    }

    public void initialize(RoomCapacityConstraint constraintAnnotation) {
        this.group = constraintAnnotation.groupId();
        this.room = constraintAnnotation.roomId();
    }

    @Override
    public boolean isValid(Object lesson, ConstraintValidatorContext cxt) {

        int groupId = (int) new BeanWrapperImpl(lesson).getPropertyValue(group);
        int roomId = (int) new BeanWrapperImpl(lesson).getPropertyValue(room);

        int roomCapacity = roomService.getById(roomId).getRoomCapacity();
        int groupSize = studentService.getStudentsByGroupId(groupId).size();

        return roomCapacity >= groupSize;
    }
}

package ua.com.foxminded.university.controller.validator;

import org.springframework.beans.BeanWrapperImpl;
import ua.com.foxminded.university.annotation.RoomCapacityConstraint;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.StudentService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoomCapacityValidator implements ConstraintValidator<RoomCapacityConstraint, Object> {

    private String group;
    private String room;
    private RoomService roomService;
    private StudentService studentService;

    public RoomCapacityValidator(RoomService roomService, StudentService studentService) {
        this.roomService = roomService;
        this.studentService = studentService;
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

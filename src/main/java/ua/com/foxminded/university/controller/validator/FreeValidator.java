package ua.com.foxminded.university.controller.validator;

import org.springframework.beans.BeanWrapperImpl;
import ua.com.foxminded.university.annotation.Free;
import ua.com.foxminded.university.repository.LessonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FreeValidator implements ConstraintValidator<Free, Object> {

    private String idString;
    private String dateString;
    private String numberString;
    private LessonRepository lessonRepository;

    public FreeValidator(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public void initialize(Free constraintAnnotation) {
        this.idString = constraintAnnotation.id();
        this.dateString = constraintAnnotation.date();
        this.numberString = constraintAnnotation.number();
    }

    @Override
    public boolean isValid(Object lesson, ConstraintValidatorContext cxt) {

        int id = (int) new BeanWrapperImpl(lesson).getPropertyValue(idString);
        String date = (String) new BeanWrapperImpl(lesson).getPropertyValue(dateString);
        int number = (int) new BeanWrapperImpl(lesson).getPropertyValue(numberString);
        LocalDate localDate = LocalDate.parse(date);

        if(idString.equals("groupId")){
            return lessonRepository.findByGroupGroupIdAndDateAndLessonNumberAndLessonInactiveFalse(id, localDate, number).isEmpty();
        } else if (idString.equals("roomId")){
            return lessonRepository.findByRoomRoomIdAndDateAndLessonNumberAndLessonInactiveFalse(id, localDate, number).isEmpty();
        } else if (idString.equals("teacherId")){
            return lessonRepository.findByTeacherTeacherIdAndDateAndLessonNumberAndLessonInactiveFalse(id, localDate, number).isEmpty();
        }
        return false;
    }
}

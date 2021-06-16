package ua.com.foxminded.university.api.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.*;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
public class RoomCapacityValidatorTest {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Test
    void roomCapacityValidatorShouldThrowValidationExceptionWhenGroupSizeIsGraterThenRoomCapacity(){
        roomService.save(new Room(1, 1, 2, false));
        Group group = new Group(1, "Java", false);
        groupService.save(group);
        teacherService.save(new Teacher(1, "Jack", "Smith", false));
        studentService.save(new Student(1, "ddcscd", "sdfsdfds", group, false));
        studentService.save(new Student(2, "ddcscd", "sdfsdfds", group, false));
        studentService.save(new Student(3, "ddcscd", "sdfsdfds", group, false));
        studentService.save(new Student(4, "ddcscd", "sdfsdfds", group, false));

        LessonDto lessonDto = new LessonDto(1, "Bio", 1, 1, 1, "2021-06-11", 1);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            lessonService.save(lessonDto);
        });
        assertEquals(thrown.getMessage(), "Group size grater than room capacity");
    }
}

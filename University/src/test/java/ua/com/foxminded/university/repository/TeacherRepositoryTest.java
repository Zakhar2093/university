package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Teacher;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TeacherRepositoryTest {
    private LessonRepository lessonRepository;
    private TeacherRepository teacherRepository;

    @Autowired
    public TeacherRepositoryTest(LessonRepository lessonRepository, TeacherRepository teacherRepository) {
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
    }

    @Test
    void removeTeacherFromAllLessonsShouldSetNullInLessonTeacher(){
        Teacher teacher1 = new Teacher(1, "one", "one", false);
        teacherRepository.save(teacher1);
        Teacher teacher2 = new Teacher(2, "one", "one", false);
        teacherRepository.save(teacher2);
        Lesson lesson1 = new Lesson(1, "Math", teacher1, null, null, LocalDateTime.now(), false);
        lessonRepository.save(lesson1);
        Lesson lesson2 = new Lesson(2, "Bio", teacher2, null, null, LocalDateTime.now(), false);
        lessonRepository.save(lesson2);
        Lesson lesson3 = new Lesson(3, "History", teacher1, null, null, LocalDateTime.now(), false);
        lessonRepository.save(lesson3);

        teacherRepository.removeTeacherFromAllLessons(teacher1.getTeacherId());

        assertNull(lessonRepository.findById(1).get().getTeacher());
        assertNotNull(lessonRepository.findById(2).get().getTeacher());
        assertNull(lessonRepository.findById(3).get().getTeacher());
    }
}

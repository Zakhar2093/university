package ua.com.foxminded.university.repository.hibernate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherRepositoryHibernateTest {

    private LessonRepository lessonRepository;
    private TeacherRepository teacherRepository;

    @Autowired
    public TeacherRepositoryHibernateTest(LessonRepository lessonRepository, TeacherRepository teacherRepository) {
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
    }

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        Teacher teacher = new Teacher(1, "one", "one", lessons,false);
        teacherRepository.create(teacher);
        Teacher expected = teacher;
        Teacher actual = teacherRepository.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", lessons,false));
        teachers.add(new Teacher(2, "two", "two", lessons,false));
        teachers.add(new Teacher(3, "three", "three", lessons,false));
        teacherRepository.create(teachers.get(0));
        teacherRepository.create(teachers.get(1));
        teacherRepository.create(teachers.get(2));
        List<Teacher> expected = teachers;
        List<Teacher> actual = teacherRepository.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        Teacher groupBeforeUpdating = new Teacher(1, "one", "one", lessons,false);
        Teacher groupAfterUpdating = new Teacher(1, "two", "one", lessons,false);
        teacherRepository.create(groupBeforeUpdating);
        teacherRepository.update(groupAfterUpdating);
        Teacher expected = groupAfterUpdating;
        Teacher actual = teacherRepository.getById(1);
        List<Teacher> groups = teacherRepository.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInTeacherInactive() {
        createLesson();
        teacherRepository.deactivate(1);
        assertTrue(teacherRepository.getById(1).isTeacherInactive());
        assertNull(lessonRepository.getById(1).getTeacher());
    }

    @Test
    void activateShouldSetFalseInTeacherInactive() {
        Teacher teacher = new Teacher(1, "one", "one", true);
        teacherRepository.create(teacher);
        assertTrue(teacherRepository.getById(1).isTeacherInactive());
        teacherRepository.activate(1);
        assertFalse(teacherRepository.getById(1).isTeacherInactive());
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsRepositoryException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            teacherRepository.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 does not exist"));
    }

    private Lesson createLesson() {
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherRepository.create(teacher);
        Lesson lesson = new Lesson(1, "Math", teacher, null, null, LocalDateTime.now(), false);
        lessonRepository.create(lesson);
        return lesson;
    }
}
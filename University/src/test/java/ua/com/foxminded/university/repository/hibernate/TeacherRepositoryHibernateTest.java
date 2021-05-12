package ua.com.foxminded.university.repository.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Teacher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherRepositoryHibernateTest {

    @Autowired
    private LessonRepository lessonDao;
    @Autowired
    private TeacherRepository teacherDao;

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        Teacher teacher = new Teacher(1, "one", "one", lessons,false);
        teacherDao.create(teacher);
        Teacher expected = teacher;
        Teacher actual = teacherDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", lessons,false));
        teachers.add(new Teacher(2, "two", "two", lessons,false));
        teachers.add(new Teacher(3, "three", "three", lessons,false));
        teacherDao.create(teachers.get(0));
        teacherDao.create(teachers.get(1));
        teacherDao.create(teachers.get(2));
        List<Teacher> expected = teachers;
        List<Teacher> actual = teacherDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        Teacher groupBeforeUpdating = new Teacher(1, "one", "one", lessons,false);
        Teacher groupAfterUpdating = new Teacher(1, "two", "one", lessons,false);
        teacherDao.create(groupBeforeUpdating);
        teacherDao.update(groupAfterUpdating);
        Teacher expected = groupAfterUpdating;
        Teacher actual = teacherDao.getById(1);
        List<Teacher> groups = teacherDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInTeacherInactive() {
        createLesson();
        teacherDao.deactivate(1);
        assertTrue(teacherDao.getById(1).isTeacherInactive());
        assertNull(lessonDao.getById(1).getTeacher());
    }

    @Test
    void activateShouldSetFalseInTeacherInactive() {
        Teacher teacher = new Teacher(1, "one", "one", true);
        teacherDao.create(teacher);
        assertTrue(teacherDao.getById(1).isTeacherInactive());
        teacherDao.activate(1);
        assertFalse(teacherDao.getById(1).isTeacherInactive());
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            teacherDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 does not exist"));
    }

    private Lesson createLesson() {
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Lesson lesson = new Lesson(1, "Math", teacher, null, null, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        return lesson;
    }
}
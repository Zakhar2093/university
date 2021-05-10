package ua.com.foxminded.university.dao.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GroupRepositoryTest {

    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentDao studentDao;

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        Group group = new Group(1, "one", students, lessons,false);
        groupDao.create(group);
        Group expected = group;
        Group actual = groupDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "one", students, lessons,false));
        groups.add(new Group(2, "two", students, lessons,false));
        groups.add(new Group(3, "three", students, lessons,false));
        groupDao.create(groups.get(0));
        groupDao.create(groups.get(1));
        groupDao.create(groups.get(2));
        List<Group> expected = groups;
        List<Group> actual = groupDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        List<Student> students = new ArrayList<>();
        List<Lesson> lessons = new ArrayList<>();
        Group groupBeforeUpdating = new Group(1, "one", students, lessons,false);
        Group groupAfterUpdating = new Group(1, "two", students, lessons,false);
        groupDao.create(groupBeforeUpdating);
        groupDao.update(groupAfterUpdating);
        Group expected = groupAfterUpdating;
        Group actual = groupDao.getById(1);
        List<Group> groups = groupDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInGroupInactive() {
        Group group = new Group(1, "one",  false);
        groupDao.create(group);
        Lesson lesson = new Lesson(1, "Math", null, group, null, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student);
        createLesson();
        groupDao.deactivate(group.getGroupId());
        assertTrue(groupDao.getById(group.getGroupId()).isGroupInactive());
        assertNull(lessonDao.getById(lesson.getLessonId()).getGroup());
        assertNull(studentDao.getById(student.getStudentId()).getGroup());
    }

    @Test
    void activateShouldSetFalseInGroupInactive() {
        Group group = new Group(1, "one", true);
        groupDao.create(group);
        assertTrue(groupDao.getById(1).isGroupInactive());
        groupDao.activate(1);
        assertFalse(groupDao.getById(1).isGroupInactive());
    }

    @Test
    void removeGroupFromLessonsShouldSetNullInLessonsGroupId() {
        Group group1 = new Group(1, "one",  false);
        Group group2 = new Group(2, "one",  false);
        groupDao.create(group1);
        groupDao.create(group2);
        Lesson lesson1 = new Lesson(1, "Math", null, group1, null, LocalDateTime.now(), false);
        Lesson lesson2 = new Lesson(2, "Math", null, group1, null, LocalDateTime.now(), false);
        Lesson lesson3 = new Lesson(3, "Math", null, group2, null, LocalDateTime.now(), false);
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);

        groupDao.removeGroupFromAllLessons(1);

        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson3);
        List<Lesson> actual = lessonDao.getAll()
                .stream()
                .filter((x) -> x.getGroup() != null)
                .collect(Collectors.toList()
                );
        assertEquals(expected, actual);
    }

    @Test
    void getGroupByLessonShouldReturnCorrectGroup() {
        List<Student> students = new ArrayList<>();
        Lesson lesson = createLesson();
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        Group expected = new Group(1, "one", students, lessons ,false);
        Group actual = groupDao.getGroupByLesson(1);

        assertEquals(expected, actual);
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }

    @Test
    void whenGetGroupByLessonGetNonexistentDataShouldThrowsDaoException() {
        createLesson();
        groupDao.removeGroupFromAllLessons(1);
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.getGroupByLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Such lesson (id = 1) does not have any group"));
    }

    private Lesson createLesson() {

        Group group = new Group(1, "one",  false);
        groupDao.create(group);
        Lesson lesson = new Lesson(1, "Math", null, group, null, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        return lesson;
    }
}
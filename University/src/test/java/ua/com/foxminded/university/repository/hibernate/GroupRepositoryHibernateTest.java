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
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GroupRepositoryHibernateTest {

    private LessonRepository lessonDao;
    private GroupRepository groupDao;
    private StudentRepository studentDao;

    @Autowired
    public GroupRepositoryHibernateTest(LessonRepository lessonDao, GroupRepository groupDao, StudentRepository studentDao) {
        this.lessonDao = lessonDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

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
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            groupDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }

    private Lesson createLesson() {

        Group group = new Group(1, "one",  false);
        groupDao.create(group);
        Lesson lesson = new Lesson(1, "Math", null, group, null, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        return lesson;
    }
}
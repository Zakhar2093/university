package ua.com.foxminded.university.repository.hibernate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GroupRepositoryHibernateTest {

    private LessonRepository lessonRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    @Autowired
    public GroupRepositoryHibernateTest(LessonRepository lessonRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        Group group = new Group(1, "one", students, lessons,false);
        groupRepository.create(group);
        Group expected = group;
        Group actual = groupRepository.getById(1);
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
        groupRepository.create(groups.get(0));
        groupRepository.create(groups.get(1));
        groupRepository.create(groups.get(2));
        List<Group> expected = groups;
        List<Group> actual = groupRepository.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        List<Student> students = new ArrayList<>();
        List<Lesson> lessons = new ArrayList<>();
        Group groupBeforeUpdating = new Group(1, "one", students, lessons,false);
        Group groupAfterUpdating = new Group(1, "two", students, lessons,false);
        groupRepository.create(groupBeforeUpdating);
        groupRepository.update(groupAfterUpdating);
        Group expected = groupAfterUpdating;
        Group actual = groupRepository.getById(1);
        List<Group> groups = groupRepository.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInGroupInactive() {
        Group group = new Group(1, "one",  false);
        groupRepository.create(group);
        Lesson lesson = new Lesson(1, "Math", null, group, null, LocalDateTime.now(), false);
        lessonRepository.create(lesson);
        Student student = new Student(1, "one", "one", group, false);
        studentRepository.create(student);
        createLesson();
        groupRepository.deactivate(group.getGroupId());
        assertTrue(groupRepository.getById(group.getGroupId()).isGroupInactive());
        assertNull(lessonRepository.getById(lesson.getLessonId()).getGroup());
        assertNull(studentRepository.getById(student.getStudentId()).getGroup());
    }

    @Test
    void activateShouldSetFalseInGroupInactive() {
        Group group = new Group(1, "one", true);
        groupRepository.create(group);
        assertTrue(groupRepository.getById(1).isGroupInactive());
        groupRepository.activate(1);
        assertFalse(groupRepository.getById(1).isGroupInactive());
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsRepositoryException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            groupRepository.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }

    private Lesson createLesson() {

        Group group = new Group(1, "one",  false);
        groupRepository.create(group);
        Lesson lesson = new Lesson(1, "Math", null, group, null, LocalDateTime.now(), false);
        lessonRepository.create(lesson);
        return lesson;
    }
}
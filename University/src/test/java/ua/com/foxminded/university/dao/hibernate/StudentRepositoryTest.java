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
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.exception.DaoException;
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
class StudentRepositoryTest {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private StudentDao studentDao;

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);

        Student student = new Student(1, "one", "one", group,false);
        studentDao.create(student);

        Student expected = student;
        Student actual = studentDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "one", "one", group,false));
        students.add(new Student(2, "two", "two", group,false));
        students.add(new Student(3, "three", "three", group,false));
        studentDao.create(students.get(0));
        studentDao.create(students.get(1));
        studentDao.create(students.get(2));

        List<Student> expected = students;
        List<Student> actual = studentDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);

        Student groupBeforeUpdating = new Student(1, "one", "one", group,false);
        Student groupAfterUpdating = new Student(1, "two", "one", group,false);
        studentDao.create(groupBeforeUpdating);
        studentDao.update(groupAfterUpdating);
        Student expected = groupAfterUpdating;
        Student actual = studentDao.getById(1);
        List<Student> groups = studentDao.getAll();

        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInStudentInactive() {
        createGroupWithStudent();
        studentDao.deactivate(1);
        assertTrue(studentDao.getById(1).isStudentInactive());
    }

    @Test
    void activateShouldSetFalseInStudentInactive() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, true);
        studentDao.create(student);

        assertTrue(studentDao.getById(1).isStudentInactive());
        studentDao.activate(1);
        assertFalse(studentDao.getById(1).isStudentInactive());
    }

    @Test
    void removeStudentFromGroupsShouldSetNullInGroupsStudentId() {
        Group group = new Group(1, "Math",  false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student);
        studentDao.removeStudentFromGroup(1);
        assertNull(studentDao.getById(student.getStudentId()).getGroup());
    }

    @Test
    void getStudentsByGroupIdShouldReturnCorrectStudents() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        Student student2 = new Student(2, "two", "two", group, false);
        studentDao.create(student);
        studentDao.create(student2);

        List<Student> expected = new ArrayList<>();
        expected.add(student);
        expected.add(student2);
        List<Student> actual = studentDao.getStudentsByGroupId(group.getGroupId());
        assertEquals(expected, actual);
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }

    private Group createGroupWithStudent() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student);
        return group;
    }
}
package ua.com.foxminded.university.repository.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepositoryHibernateTest {

    @Autowired
    private GroupRepository groupDao;
    @Autowired
    private StudentRepository studentDao;

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
        Student student = createStudentWithGroup();
        studentDao.deactivate(student.getStudentId());
        assertTrue(studentDao.getById(1).isStudentInactive());
        assertTrue(groupDao.getById(1).getStudents().isEmpty());
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
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            studentDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }

    @Test
    void getStudentsByGroupIdShouldReturnCorrectData(){
        Group group1 = new Group(1, "Java", false);
        Group group2 = new Group(2, "C++", false);
        groupDao.create(group1);
        groupDao.create(group2);
        Student student1 = new Student(1, "one", "one", group1, false);
        Student student2 = new Student(2, "two", "two", group1, false);
        Student student3 = new Student(3, "three", "three", group2, false);
        studentDao.create(student1);
        studentDao.create(student2);
        studentDao.create(student3);
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        expected.add(student2);
        List<Student> actual = studentDao.getStudentsByGroupId(1);
        assertEquals(expected, actual);
    }

    private Student createStudentWithGroup() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student);
        return student;
    }
}
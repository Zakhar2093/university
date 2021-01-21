package ua.com.foxminded.university.dao.imlementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementation.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementation.StudentDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

class StudentDaoImplTest {

    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private StudentDao studentDao;
    private GroupDao groupDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        studentDao = context.getBean("studentDaoImpl", StudentDaoImpl.class);
        groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        dbInit.initialization();

    }

    @Test
    void whenCreateAndGetByIdAreColledSouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group);
        studentDao.create(student);
        Student expected = student;
        Student actual = studentDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateAndGetAllAreColledSouldInsertAndGetCorrectData() {
        List<Student> students = new ArrayList<>();
        Group group = new Group(1, "any name");
        groupDao.create(group);
        students.add(new Student(1, "one", "one", group));
        students.add(new Student(2, "two", "two", group));
        students.add(new Student(3, "three", "three", group));
        studentDao.create(students.get(0));
        studentDao.create(students.get(1));
        studentDao.create(students.get(2));
        List<Student> expected = students;
        List<Student> actual = studentDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteIsColledSouldDeleteCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        studentDao.create(new Student(1, "one", "one", group));
        studentDao.delete(1);
        List<Student> actual = studentDao.getAll();
        assertTrue(actual.size() == 0);
    }

    @Test
    void whenUpdateIsColledSouldUpdateCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Student studentBeforeUpdating = new Student(1, "one", "one", group);
        Student studentAfterUpdating = new Student(1, "two", "one", group);
        studentDao.create(studentBeforeUpdating);
        studentDao.update(studentAfterUpdating);
        Student expected = studentAfterUpdating;
        Student actual = studentDao.getById(1);
        List<Student> students = studentDao.getAll();
        assertTrue(students.size() == 1);
        assertEquals(expected, actual);
    }

    @AfterEach
    void closeConext() {
        context.close();
    }
}
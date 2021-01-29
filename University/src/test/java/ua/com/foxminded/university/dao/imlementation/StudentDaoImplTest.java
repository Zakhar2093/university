package ua.com.foxminded.university.dao.imlementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementation.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementation.StudentDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Student;

class StudentDaoImplTest {
    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private JdbcTemplate jdbcTemplate;
    private PropertyReader propertyReader;
    private GroupDao groupDao;
    private StudentDao studentDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        propertyReader = context.getBean("propertyReader", PropertyReader.class);
        groupDao = new GroupDaoImpl(jdbcTemplate, propertyReader);
        studentDao = new StudentDaoImpl(jdbcTemplate, propertyReader, groupDao);
        dbInit.initialization();
    }

    @Test
    void getByIdAndCreateSouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student);
        Student expected = student;
        Student actual = studentDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateSouldInsertAndGetCorrectData() {
        List<Student> students = new ArrayList<>();
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        students.add(new Student(1, "one", "one", group, false));
        students.add(new Student(2, "two", "two", group, false));
        students.add(new Student(3, "three", "three", group, false));
        studentDao.create(students.get(0));
        studentDao.create(students.get(1));
        studentDao.create(students.get(2));
        List<Student> expected = students;
        List<Student> actual = studentDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateSouldUpdateCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student studentBeforeUpdating = new Student(1, "one", "one", group, false);
        Student studentAfterUpdating = new Student(1, "two", "one", group, false);
        studentDao.create(studentBeforeUpdating);
        studentDao.update(studentAfterUpdating);
        Student expected = studentAfterUpdating;
        Student actual = studentDao.getById(1);
        List<Student> students = studentDao.getAll();
        assertTrue(students.size() == 1);
        assertEquals(expected, actual);
    }
    
    @Test
    void deactivateSouldSetTrueInStudentInactive() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student); 
        studentDao.removeStudentFromGroup(1);
        studentDao.deactivate(1);
        assertTrue(studentDao.getById(1).isStudentInactive());
    }
    
    @Test
    void activateSouldSetFolseInStudentInactive() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student);
        studentDao.removeStudentFromGroup(1);
        studentDao.deactivate(1);
        assertTrue(studentDao.getById(1).isStudentInactive());
        studentDao.activate(1);
        assertFalse(studentDao.getById(1).isStudentInactive());
    }
    
    @Test
    void removeStudentFromGroupSouldSetNullInStudentGroupId() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student); 
        studentDao.removeStudentFromGroup(1);
        assertTrue(studentDao.getById(1).getGroup() == null);
    }
    
    @Test
    void addStudentToGroupSouldSetGroupInStudentGroupId() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentDao.create(student); 
        studentDao.removeStudentFromGroup(1);
        assertTrue(studentDao.getById(1).getGroup() == null);
        studentDao.addStudentToGroup(1, 1);
        assertTrue(studentDao.getById(1).getGroup().equals(group));
    }
    
    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id does not exist"));
    }
    
    @AfterEach
    void closeConext() {
        context.close();
    }
}
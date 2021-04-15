package ua.com.foxminded.university.dao.imlementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.interfaces.*;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
class StudentDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private DatabaseInitialization dbInit;

    @BeforeEach
    void createBean() {
        dbInit.initialization();;
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

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }
    
    @Test 
    void whenUpdateNonexistentStudentShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group group = new Group(1, "any name", false);
            groupDao.create(group);
            studentDao.update(new Student(1, "one", "one", group, false));
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 can not be updated"));
    }
    
    @Test 
    void whenDeactivateNonexistentStudentShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.deactivate(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 can not be deactivated"));
    }
    
    @Test 
    void whenActivateNonexistentStudentShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.activate(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 can not be activated"));
    }
    
    @Test
    void whenCreateStudentWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group group = new Group(1, "any name", false);
            groupDao.create(group);
            studentDao.create(new Student(1, null, null, group, false));
        });
        assertTrue(thrown.getMessage().contains("Student can not be created. Some field is null"));
    }
    
    @Test
    void whenUpdateStudentWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group group = new Group(1, "any name", false);
            groupDao.create(group);
            Student studentBeforeUpdating = new Student(1, "one", "one", group, false);
            Student studentAfterUpdating = new Student(1, null, "one", group, false);
            studentDao.create(studentBeforeUpdating);
            studentDao.update(studentAfterUpdating);
        });
        assertTrue(thrown.getMessage().contains("Student can not be updated. Some new field is null"));
    }
    
    @Test
    void whenAddStudentToNonexistentGroupShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.addStudentToGroup(1, 1);
        });
        assertTrue(thrown.getMessage().contains("Student 1 can not be added to group 1. Student or Group does not exist"));
    }
    
    @Test
    void whenRemoveStudentFromNonexistentGroupShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            studentDao.removeStudentFromGroup(1);
        });
        assertTrue(thrown.getMessage().contains("Student can not be removed from Group id = 1. Student does not exist"));
    }
}
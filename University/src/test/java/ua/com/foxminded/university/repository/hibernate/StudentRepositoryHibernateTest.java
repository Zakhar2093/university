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

    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    @Autowired
    public StudentRepositoryHibernateTest(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);

        Student student = new Student(1, "one", "one", group,false);
        studentRepository.create(student);

        Student expected = student;
        Student actual = studentRepository.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);

        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "one", "one", group,false));
        students.add(new Student(2, "two", "two", group,false));
        students.add(new Student(3, "three", "three", group,false));
        studentRepository.create(students.get(0));
        studentRepository.create(students.get(1));
        studentRepository.create(students.get(2));

        List<Student> expected = students;
        List<Student> actual = studentRepository.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);

        Student groupBeforeUpdating = new Student(1, "one", "one", group,false);
        Student groupAfterUpdating = new Student(1, "two", "one", group,false);
        studentRepository.create(groupBeforeUpdating);
        studentRepository.update(groupAfterUpdating);
        Student expected = groupAfterUpdating;
        Student actual = studentRepository.getById(1);
        List<Student> groups = studentRepository.getAll();

        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInStudentInactive() {
        Student student = createStudentWithGroup();
        studentRepository.deactivate(student.getStudentId());
        assertTrue(studentRepository.getById(1).isStudentInactive());
        assertTrue(groupRepository.getById(1).getStudents().isEmpty());
    }

    @Test
    void activateShouldSetFalseInStudentInactive() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);
        Student student = new Student(1, "one", "one", group, true);
        studentRepository.create(student);

        assertTrue(studentRepository.getById(1).isStudentInactive());
        studentRepository.activate(1);
        assertFalse(studentRepository.getById(1).isStudentInactive());
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsRepositoryException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            studentRepository.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Student with such id 1 does not exist"));
    }

    @Test
    void getStudentsByGroupIdShouldReturnCorrectData(){
        Group group1 = new Group(1, "Java", false);
        Group group2 = new Group(2, "C++", false);
        groupRepository.create(group1);
        groupRepository.create(group2);
        Student student1 = new Student(1, "one", "one", group1, false);
        Student student2 = new Student(2, "two", "two", group1, false);
        Student student3 = new Student(3, "three", "three", group2, false);
        studentRepository.create(student1);
        studentRepository.create(student2);
        studentRepository.create(student3);
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        expected.add(student2);
        List<Student> actual = studentRepository.getStudentsByGroupId(1);
        assertEquals(expected, actual);
    }

    private Student createStudentWithGroup() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);
        Student student = new Student(1, "one", "one", group, false);
        studentRepository.create(student);
        return student;
    }
}
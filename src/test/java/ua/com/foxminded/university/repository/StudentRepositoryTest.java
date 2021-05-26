package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentRepositoryTest {
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    @Autowired
    public StudentRepositoryTest(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    void getStudentsByGroupIdShouldReturnCorrectData(){
        Group group1 = new Group(1, "Java", false);
        Group group2 = new Group(2, "C++", false);
        groupRepository.save(group1);
        groupRepository.save(group2);
        Student student1 = new Student(1, "one", "one", group1, false);
        Student student2 = new Student(2, "two", "two", group1, false);
        Student student3 = new Student(3, "three", "three", group2, false);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        expected.add(student2);
        List<Student> actual = studentRepository.findByGroupGroupId(1);
        assertEquals(expected, actual);
    }
}

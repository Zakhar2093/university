package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GroupRepositoryTest {

    private LessonRepository lessonRepository;
    private GroupRepository groupRepository;
    private StudentRepository studentRepository;

    @Autowired
    public GroupRepositoryTest(LessonRepository lessonRepository, GroupRepository groupRepository, StudentRepository studentRepository) {
        this.lessonRepository = lessonRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    void removeGroupFromAllLessonsShouldSetNullInLessonGroup(){
        Group group1 = new Group(1, "one",  false);
        groupRepository.save(group1);
        Group group2 = new Group(2, "one",  false);
        groupRepository.save(group2);
        Lesson lesson1 = new Lesson(1, "Math", null, group1, null, LocalDateTime.now(), false);
        lessonRepository.save(lesson1);
        Lesson lesson2 = new Lesson(2, "Bio", null, group2, null, LocalDateTime.now(), false);
        lessonRepository.save(lesson2);
        Lesson lesson3 = new Lesson(3, "History", null, group1, null, LocalDateTime.now(), false);
        lessonRepository.save(lesson3);

        groupRepository.removeGroupFromAllLessons(group1.getGroupId());

        assertNull(lessonRepository.findById(1).get().getGroup());
        assertNotNull(lessonRepository.findById(2).get().getGroup());
        assertNull(lessonRepository.findById(3).get().getGroup());
    }

    @Test
    void removeGroupFromAllStudentsShouldSetNullInStudentGroup(){
        Group group1 = new Group(1, "one",  false);
        groupRepository.save(group1);
        Group group2 = new Group(2, "one",  false);
        groupRepository.save(group2);
        Student student1 = new Student(1, "one", "one", group1, false);
        studentRepository.save(student1);
        Student student2 = new Student(2, "one", "one", group2,  false);
        studentRepository.save(student2);
        Student student3 = new Student(3, "one", "one", group1,  false);
        studentRepository.save(student3);

        groupRepository.removeGroupFromAllStudents(group1.getGroupId());

        assertNull(studentRepository.findById(1).get().getGroup());
        assertNotNull(studentRepository.findById(2).get().getGroup());
        assertNull(studentRepository.findById(3).get().getGroup());
    }
}

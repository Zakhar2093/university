package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LessonRepositoryTest {

    private static final LocalDate date = LocalDate.parse("2121-01-20");

    private GroupRepository groupRepository;
    private LessonRepository lessonRepository;
    private TeacherRepository teacherRepository;
    private RoomRepository roomRepository;
    private StudentRepository studentRepository;

    @Autowired
    public LessonRepositoryTest(GroupRepository groupRepository, LessonRepository lessonRepository, TeacherRepository teacherRepository, RoomRepository roomRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    void getLessonByTeacherForDayShouldReturnCorrectData() {
        List<Lesson> allLessons = saveTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(0));
        expected.add(allLessons.get(3));

        List<Lesson> actual = lessonRepository.getLessonByTeacherIdForDay(1,
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());
        assertEquals(expected, actual);
    }

    @Test
    void getLessonByTeacherForMonthShouldReturnCorrectData() {
        List<Lesson> allLessons = saveTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(0));
        expected.add(allLessons.get(2));
        expected.add(allLessons.get(3));
        expected.add(allLessons.get(4));

        List<Lesson> actual = lessonRepository.getLessonByTeacherIdForMonth(1,
                date.getYear(),
                date.getMonthValue());
        assertEquals(expected, actual);
    }

    @Test
    void getLessonByGroupForDayShouldReturnCorrectData() {
        List<Lesson> allLessons = saveTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(3));
        expected.add(allLessons.get(6));

        Group group = studentRepository.findById(1).get().getGroup();

        List<Lesson> actual = lessonRepository.getLessonByGroupIdForDay(group,
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth());
        assertEquals(expected, actual);
    }

    @Test
    void getLessonByGroupForMonthShouldReturnCorrectData() {
        List<Lesson> allLessons = saveTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(2));
        expected.add(allLessons.get(3));
        expected.add(allLessons.get(4));
        expected.add(allLessons.get(5));
        expected.add(allLessons.get(6));

        Group group = studentRepository.findById(1).get().getGroup();

        List<Lesson> actual = lessonRepository.getLessonByGroupIdForMonth(group,
                date.getYear(),
                date.getMonthValue());
        assertEquals(expected, actual);
    }

    @Test
    void findByGroupGroupIdShouldReturnCorrectData() {
        List<Lesson> lessons = saveTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(1));
        expected.add(lessons.get(7));
        expected.add(lessons.get(8));
        List<Lesson> actual = lessonRepository.findByGroupGroupId(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByRoomRoomIdShouldReturnCorrectData() {
        List<Lesson> lessons = saveTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(2));
        expected.add(lessons.get(4));
        expected.add(lessons.get(6));
        expected.add(lessons.get(8));
        List<Lesson> actual = lessonRepository.findByRoomRoomId(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByTeacherTeacherIdShouldReturnCorrectData() {
        List<Lesson> lessons = saveTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(1));
        expected.add(lessons.get(2));
        expected.add(lessons.get(3));
        expected.add(lessons.get(4));
        List<Lesson> actual = lessonRepository.findByTeacherTeacherId(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByGroupGroupIdAndDateAndLessonNumberAndLessonInactiveFalseShouldReturnCorrectData(){
        List<Lesson> lessons = saveTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        List<Lesson> actual = lessonRepository.findByGroupGroupIdAndDateAndLessonNumberAndLessonInactiveFalse(1, date, 1);
        assertEquals(expected, actual);
    }

    @Test
    void findByTeacherTeacherIdAndDateAndLessonNumberAndLessonInactiveFalseShouldReturnCorrectData(){
        List<Lesson> lessons = saveTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(3));
        List<Lesson> actual = lessonRepository.findByTeacherTeacherIdAndDateAndLessonNumberAndLessonInactiveFalse(1, date, 1);
        assertEquals(expected, actual);
    }

    @Test
    void findByRoomRoomIdAndDateAndLessonNumberAndLessonInactiveFalseShouldReturnCorrectData(){
        List<Lesson> lessons = saveTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(6));
        List<Lesson> actual = lessonRepository.findByRoomRoomIdAndDateAndLessonNumberAndLessonInactiveFalse(1, date, 1);
        assertEquals(expected, actual);
    }

    private List<Lesson> saveTestData(){
        Group group1 = new Group(1, "Java", false);
        Group group2 = new Group(2, "C++", false);
        groupRepository.save(group1);
        groupRepository.save(group2);
        Student student1 = new Student(1, "one", "one", group1,false);
        Student student2 = new Student(1, "one", "one", group2,false);
        studentRepository.save(student1);
        studentRepository.save(student2);
        Teacher teacher1 = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        Room room1 = new Room(1, 101, 10, false);
        Room room2 = new Room(2, 102, 10, false);
        roomRepository.save(room1);
        roomRepository.save(room2);

        LocalDate date1 = LocalDate.parse("2121-01-20");
        LocalDate date2 = LocalDate.parse("2121-02-21");
        LocalDate date3 = LocalDate.parse("2121-01-21");

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher1, group1, room1, date1, 1));
        lessons.add(new Lesson(2, "History", teacher1, group1, room2, date2, 1));
        lessons.add(new Lesson(3, "English", teacher1, group2, room1, date3, 1));
        lessons.add(new Lesson(4, "Math", teacher1, group2, room2, date1, 1));
        lessons.add(new Lesson(5, "Bio", teacher1, group2, room1, date3, 1));
        lessons.add(new Lesson(6, "History", teacher2, group2, room2, date3, 1));
        lessons.add(new Lesson(7, "English", teacher2, group2, room1, date1, 1));
        lessons.add(new Lesson(8, "Math", teacher2, group1, room2, date2, 1));
        lessons.add(new Lesson(9, "Bio", teacher2, group1, room1, date3, 1));
        lessons.add(new Lesson(10, "Math", teacher2, group2, room2, date2, 1));

        lessons.forEach(p -> lessonRepository.save(p));
        return lessons;
    }
}
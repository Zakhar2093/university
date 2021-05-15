package ua.com.foxminded.university.repository.hibernate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LessonRepositoryHibernateTest {

    private static final String FORMAT = "yyyy.MM.dd-HH.mm.ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private GroupRepository groupRepository;
    private LessonRepository lessonRepository;
    private TeacherRepository teacherRepository;
    private RoomRepository roomRepository;
    private StudentRepository studentRepository;

    @Autowired
    public LessonRepositoryHibernateTest(GroupRepository groupRepository, LessonRepository lessonRepository, TeacherRepository teacherRepository, RoomRepository roomRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.roomRepository = roomRepository;
        this.studentRepository = studentRepository;
    }

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        Lesson expected = createLesson();
        Lesson actual = lessonRepository.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> expected = createTestData();
        List<Lesson> actual = lessonRepository.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);
        Room room = new Room(1, 1, false);
        roomRepository.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherRepository.create(teacher);
        LocalDateTime date = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);

        Lesson groupBeforeUpdating = new Lesson(1, "one", teacher, group, room, date, false);
        Lesson groupAfterUpdating = new Lesson(1, "one", teacher, group, room, date, false);
        lessonRepository.create(groupBeforeUpdating);
        lessonRepository.update(groupAfterUpdating);
        Lesson expected = groupAfterUpdating;
        Lesson actual = lessonRepository.getById(1);
        List<Lesson> groups = lessonRepository.getAll();

        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInLessonInactive() {
        Lesson lesson = createLesson();
        lessonRepository.deactivate(lesson.getLessonId());
        assertTrue(lessonRepository.getById(lesson.getLessonId()).isLessonInactive());
        assertNull(lessonRepository.getById(1).getRoom());
        assertNull(lessonRepository.getById(1).getTeacher());
        assertNull(lessonRepository.getById(1).getGroup());
    }

    @Test
    void activateShouldSetFalseInLessonInactive() {
        Lesson lesson = createLesson();
        lessonRepository.deactivate(lesson.getLessonId());
        assertTrue(lessonRepository.getById(lesson.getLessonId()).isLessonInactive());
        lessonRepository.activate(1);
        assertFalse(lessonRepository.getById(lesson.getLessonId()).isLessonInactive());
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsRepositoryException() {
        RepositoryException thrown = assertThrows(RepositoryException.class, () -> {
            lessonRepository.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 does not exist"));
    }

    @Test
    void getLessonByTeacherForDayShouldReturnCorrectData() {
        LocalDateTime date = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        List<Lesson> allLessons = createTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(0));
        expected.add(allLessons.get(3));

        List<Lesson> actual = lessonRepository.getLessonByTeacherIdForDay(1, date);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonByTeacherForMonthShouldReturnCorrectData() {
        LocalDateTime date = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        List<Lesson> allLessons = createTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(0));
        expected.add(allLessons.get(2));
        expected.add(allLessons.get(3));
        expected.add(allLessons.get(4));

        List<Lesson> actual = lessonRepository.getLessonByTeacherIdForMonth(1, date);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonByStudentForDayShouldReturnCorrectData() {
        LocalDateTime date = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        List<Lesson> allLessons = createTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(0));

        List<Lesson> actual = lessonRepository.getLessonByStudentIdForDay(1, date);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonByStudentForMonthShouldReturnCorrectData() {
        LocalDateTime date = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        List<Lesson> allLessons = createTestData();

        List<Lesson> expected = new ArrayList<>();
        expected.add(allLessons.get(0));
        expected.add(allLessons.get(8));

        List<Lesson> actual = lessonRepository.getLessonByStudentIdForMonth(1, date);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByGroupIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(1));
        expected.add(lessons.get(7));
        expected.add(lessons.get(8));
        List<Lesson> actual = lessonRepository.getLessonsByGroupId(1);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByRoomIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(2));
        expected.add(lessons.get(4));
        expected.add(lessons.get(6));
        expected.add(lessons.get(8));
        List<Lesson> actual = lessonRepository.getLessonsByRoomId(1);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByTeacherIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(1));
        expected.add(lessons.get(2));
        expected.add(lessons.get(3));
        expected.add(lessons.get(4));
        List<Lesson> actual = lessonRepository.getLessonsByTeacherId(1);
        assertEquals(expected, actual);
    }

    private List<Lesson> createTestData(){
        Group group1 = new Group(1, "Java", false);
        Group group2 = new Group(2, "C++", false);
        groupRepository.create(group1);
        groupRepository.create(group2);
        Teacher teacher1 = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherRepository.create(teacher1);
        teacherRepository.create(teacher2);
        Room room1 = new Room(1, 101);
        Room room2 = new Room(2, 102);
        roomRepository.create(room1);
        roomRepository.create(room2);
        Student student1 = new Student(1, "student1", "student1", group1, false);
        Student student2 = new Student(2, "student2", "student2", group2, false);
        studentRepository.create(student1);
        studentRepository.create(student2);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.02.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher1, group1, room1, date1, false));
        lessons.add(new Lesson(2, "History", teacher1, group1, room2, date2, false));
        lessons.add(new Lesson(3, "English", teacher1, group2, room1, date3, false));
        lessons.add(new Lesson(4, "Math", teacher1, group2, room2, date1, false));
        lessons.add(new Lesson(5, "Bio", teacher1, group2, room1, date3, false));
        lessons.add(new Lesson(6, "History", teacher2, group2, room2, date3, false));
        lessons.add(new Lesson(7, "English", teacher2, group2, room1, date1, false));
        lessons.add(new Lesson(8, "Math", teacher2, group1, room2, date2, false));
        lessons.add(new Lesson(9, "Bio", teacher2, group1, room1, date3, false));
        lessons.add(new Lesson(10, "Math", teacher2, group2, room2, date2, false));

        lessons.stream().forEach(p -> lessonRepository.create(p));
        return lessons;
    }

    private Lesson createLesson() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);
        Room room = new Room(1, 1, false);
        roomRepository.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherRepository.create(teacher);
        LocalDateTime date = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        Lesson lesson = new Lesson(1, "one", teacher, group, room, date, false);
        lessonRepository.create(lesson);
        return lesson;
    }
}
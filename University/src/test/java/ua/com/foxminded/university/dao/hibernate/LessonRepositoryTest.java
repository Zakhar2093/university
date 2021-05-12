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
import ua.com.foxminded.university.dao.interfaces.*;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LessonRepositoryTest {

    private static final String FORMAT = "yyyy.MM.dd-HH.mm.ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private StudentDao studentDao;

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        Lesson expected = createLesson();
        Lesson actual = lessonDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Room room = new Room(1, 1, false);
        roomDao.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);


        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false));
        lessons.add(new Lesson(2, "two", teacher, group, room, LocalDateTime.now(), false));
        lessons.add(new Lesson(3, "three", teacher, group, room, LocalDateTime.now(), false));
        lessonDao.create(lessons.get(0));
        lessonDao.create(lessons.get(1));
        lessonDao.create(lessons.get(2));

        List<Lesson> expected = lessons;
        List<Lesson> actual = lessonDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Room room = new Room(1, 1, false);
        roomDao.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);

        Lesson groupBeforeUpdating = new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false);
        Lesson groupAfterUpdating = new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false);
        lessonDao.create(groupBeforeUpdating);
        lessonDao.update(groupAfterUpdating);
        Lesson expected = groupAfterUpdating;
        Lesson actual = lessonDao.getById(1);
        List<Lesson> groups = lessonDao.getAll();

        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInLessonInactive() {
        Lesson lesson = createLesson();
        lessonDao.deactivate(lesson.getLessonId());
        assertTrue(lessonDao.getById(lesson.getLessonId()).isLessonInactive());
        assertTrue(teacherDao.getById(1).getLessons().isEmpty());
        assertTrue(roomDao.getById(1).getLessons().isEmpty());
        assertTrue(groupDao.getById(1).getLessons().isEmpty());
    }

    @Test
    void activateShouldSetFalseInLessonInactive() {
        Lesson lesson = createLesson();
        lessonDao.deactivate(lesson.getLessonId());
        assertTrue(lessonDao.getById(lesson.getLessonId()).isLessonInactive());
        lessonDao.activate(1);
        assertFalse(lessonDao.getById(lesson.getLessonId()).isLessonInactive());
    }

    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 does not exist"));
    }

    @Test
    void getLessonByTeacherForDayShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "bio", teacher2, group, room, date2, false);
        Lesson lesson4 = new Lesson(4, "history", teacher, group, room, date3, false);
        Lesson lesson5 = new Lesson(5, "english", teacher, group, room, date4, false);

        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);
        lessonDao.create(lesson5);

        List<Lesson> actual = lessonDao.getLessonByTeacherIdForDay(teacher.getTeacherId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson2);
        expected.add(lesson4);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonByTeacherForMonthShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "bio", teacher2, group, room, date2, false);
        Lesson lesson4 = new Lesson(4, "history", teacher, group, room, date3, false);
        Lesson lesson5 = new Lesson(5, "english", teacher, group, room, date4, false);

        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);
        lessonDao.create(lesson5);

        List<Lesson> actual = lessonDao.getLessonByTeacherIdForMonth(teacher.getTeacherId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);
        expected.add(lesson4);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonByStudentForDayShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        Student student1 = new Student(1, "student1", "student1", group, false);
        Student student2 = new Student(2, "student2", "student2", group, false);
        studentDao.create(student1);
        studentDao.create(student2);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "history", teacher, group, room, date3, false);
        Lesson lesson4 = new Lesson(4, "english", teacher, group, room, date4, false);

        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);

        List<Lesson> actual = lessonDao.getLessonByStudentIdForDay(student1.getStudentId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson2);
        expected.add(lesson3);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonByStudentForMonthShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        Student student1 = new Student(1, "student1", "student1", group, false);
        Student student2 = new Student(2, "student2", "student2", group, false);
        studentDao.create(student1);
        studentDao.create(student2);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "history", teacher, group, room, date3, false);
        Lesson lesson4 = new Lesson(4, "english", teacher, group, room, date4, false);

        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);

        List<Lesson> actual = lessonDao.getLessonByStudentIdForMonth(student1.getStudentId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);
        expected.add(lesson3);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByGroupIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(1));
        expected.add(lessons.get(3));
        List<Lesson> actual = lessonDao.getLessonsByGroupId(1);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByRoomIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(2));
        expected.add(lessons.get(3));
        expected.add(lessons.get(4));
        List<Lesson> actual = lessonDao.getLessonsByRoomId(1);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByTeacherIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(0));
        expected.add(lessons.get(1));
        expected.add(lessons.get(2));
        List<Lesson> actual = lessonDao.getLessonsByTeacherId(1);
        assertEquals(expected, actual);
    }

    private List<Lesson> createTestData(){
        Group group1 = new Group(1, "Java", false);
        Group group2 = new Group(2, "C++", false);
        groupDao.create(group1);
        groupDao.create(group2);
        Teacher teacher1 = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherDao.create(teacher1);
        teacherDao.create(teacher2);
        Room room1 = new Room(1, 101);
        Room room2 = new Room(2, 102);
        roomDao.create(room1);
        roomDao.create(room2);

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher1, group2, room2, LocalDateTime.now(), false));
        lessons.add(new Lesson(2, "History", teacher1, group1, room2, LocalDateTime.now(), false));
        lessons.add(new Lesson(3, "English", teacher1, group2, room1, LocalDateTime.now(), false));
        lessons.add(new Lesson(4, "Math", teacher2, group1, room1, LocalDateTime.now(), false));
        lessons.add(new Lesson(5, "Bio", teacher2, group2, room1, LocalDateTime.now(), false));
        lessonDao.create(lessons.get(0));
        lessonDao.create(lessons.get(1));
        lessonDao.create(lessons.get(2));
        lessonDao.create(lessons.get(3));
        lessonDao.create(lessons.get(4));
        return lessons;
    }

    private Lesson createLesson() {
        Group group = new Group(1, "any", false);
        groupDao.create(group);
        Room room = new Room(1, 1, false);
        roomDao.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Lesson lesson = new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        return lesson;
    }
}
package ua.com.foxminded.university.repository.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.*;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.RoomRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
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
        Group group = new Group(1, "any", false);
        groupRepository.create(group);
        Room room = new Room(1, 1, false);
        roomRepository.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherRepository.create(teacher);


        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false));
        lessons.add(new Lesson(2, "two", teacher, group, room, LocalDateTime.now(), false));
        lessons.add(new Lesson(3, "three", teacher, group, room, LocalDateTime.now(), false));
        lessonRepository.create(lessons.get(0));
        lessonRepository.create(lessons.get(1));
        lessonRepository.create(lessons.get(2));

        List<Lesson> expected = lessons;
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

        Lesson groupBeforeUpdating = new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false);
        Lesson groupAfterUpdating = new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false);
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
        assertTrue(teacherRepository.getById(1).getLessons().isEmpty());
        assertTrue(roomRepository.getById(1).getLessons().isEmpty());
        assertTrue(groupRepository.getById(1).getLessons().isEmpty());
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
        Group group = new Group(1, "any name", false);
        groupRepository.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherRepository.create(teacher);
        teacherRepository.create(teacher2);
        Room room = new Room(1, 101);
        roomRepository.create(room);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "bio", teacher2, group, room, date2, false);
        Lesson lesson4 = new Lesson(4, "history", teacher, group, room, date3, false);
        Lesson lesson5 = new Lesson(5, "english", teacher, group, room, date4, false);

        lessonRepository.create(lesson1);
        lessonRepository.create(lesson2);
        lessonRepository.create(lesson3);
        lessonRepository.create(lesson4);
        lessonRepository.create(lesson5);

        List<Lesson> actual = lessonRepository.getLessonByTeacherIdForDay(teacher.getTeacherId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson2);
        expected.add(lesson4);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonByTeacherForMonthShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupRepository.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherRepository.create(teacher);
        teacherRepository.create(teacher2);
        Room room = new Room(1, 101);
        roomRepository.create(room);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "bio", teacher2, group, room, date2, false);
        Lesson lesson4 = new Lesson(4, "history", teacher, group, room, date3, false);
        Lesson lesson5 = new Lesson(5, "english", teacher, group, room, date4, false);

        lessonRepository.create(lesson1);
        lessonRepository.create(lesson2);
        lessonRepository.create(lesson3);
        lessonRepository.create(lesson4);
        lessonRepository.create(lesson5);

        List<Lesson> actual = lessonRepository.getLessonByTeacherIdForMonth(teacher.getTeacherId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);
        expected.add(lesson4);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonByStudentForDayShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupRepository.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherRepository.create(teacher);
        teacherRepository.create(teacher2);
        Room room = new Room(1, 101);
        roomRepository.create(room);
        Student student1 = new Student(1, "student1", "student1", group, false);
        Student student2 = new Student(2, "student2", "student2", group, false);
        studentRepository.create(student1);
        studentRepository.create(student2);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "history", teacher, group, room, date3, false);
        Lesson lesson4 = new Lesson(4, "english", teacher, group, room, date4, false);

        lessonRepository.create(lesson1);
        lessonRepository.create(lesson2);
        lessonRepository.create(lesson3);
        lessonRepository.create(lesson4);

        List<Lesson> actual = lessonRepository.getLessonByStudentIdForDay(student1.getStudentId(), date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson2);
        expected.add(lesson3);

        assertEquals(expected, actual);
    }

    @Test
    void getLessonByStudentForMonthShouldReturnCorrectData() {
        Group group = new Group(1, "any name", false);
        groupRepository.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherRepository.create(teacher);
        teacherRepository.create(teacher2);
        Room room = new Room(1, 101);
        roomRepository.create(room);
        Student student1 = new Student(1, "student1", "student1", group, false);
        Student student2 = new Student(2, "student2", "student2", group, false);
        studentRepository.create(student1);
        studentRepository.create(student2);

        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", FORMATTER);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", FORMATTER);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", FORMATTER);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", FORMATTER);

        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1, false);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2, false);
        Lesson lesson3 = new Lesson(3, "history", teacher, group, room, date3, false);
        Lesson lesson4 = new Lesson(4, "english", teacher, group, room, date4, false);

        lessonRepository.create(lesson1);
        lessonRepository.create(lesson2);
        lessonRepository.create(lesson3);
        lessonRepository.create(lesson4);

        List<Lesson> actual = lessonRepository.getLessonByStudentIdForMonth(student1.getStudentId(), date2);
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
        List<Lesson> actual = lessonRepository.getLessonsByGroupId(1);
        assertEquals(expected, actual);
    }

    @Test
    void getLessonsByRoomIdShouldReturnCorrectData() {
        List<Lesson> lessons = createTestData();
        List<Lesson> expected = new ArrayList<>();
        expected.add(lessons.get(2));
        expected.add(lessons.get(3));
        expected.add(lessons.get(4));
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

        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher1, group2, room2, LocalDateTime.now(), false));
        lessons.add(new Lesson(2, "History", teacher1, group1, room2, LocalDateTime.now(), false));
        lessons.add(new Lesson(3, "English", teacher1, group2, room1, LocalDateTime.now(), false));
        lessons.add(new Lesson(4, "Math", teacher2, group1, room1, LocalDateTime.now(), false));
        lessons.add(new Lesson(5, "Bio", teacher2, group2, room1, LocalDateTime.now(), false));
        lessonRepository.create(lessons.get(0));
        lessonRepository.create(lessons.get(1));
        lessonRepository.create(lessons.get(2));
        lessonRepository.create(lessons.get(3));
        lessonRepository.create(lessons.get(4));
        return lessons;
    }

    private Lesson createLesson() {
        Group group = new Group(1, "any", false);
        groupRepository.create(group);
        Room room = new Room(1, 1, false);
        roomRepository.create(room);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherRepository.create(teacher);
        Lesson lesson = new Lesson(1, "one", teacher, group, room, LocalDateTime.now(), false);
        lessonRepository.create(lesson);
        return lesson;
    }
}
package ua.com.foxminded.university.dao.imlementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementation.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementation.LessonDaoImpl;
import ua.com.foxminded.university.dao.implementation.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementation.StudentDaoImpl;
import ua.com.foxminded.university.dao.implementation.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

class LessonDaoImlpTest {
    private static final String FORMATTER = "yyyy.MM.dd-HH.mm.ss";
    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private LessonDao lessonDao;
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;
    private StudentDao studentDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        lessonDao = context.getBean("lessonDaoImpl", LessonDaoImpl.class);
        groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        teacherDao = context.getBean("teacherDaoImpl", TeacherDaoImpl.class);
        roomDao = context.getBean("roomDaoImpl", RoomDaoImpl.class);
        studentDao = context.getBean("studentDaoImpl", StudentDaoImpl.class);
        dbInit.initialization();
    }

    @Test
    void whenCreateAndGetByIdAreColledSouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        LocalDateTime date = LocalDateTime.now();
        
        
        Lesson lesson = new Lesson(1, "Math", teacher, group, room, date);
        lessonDao.create(lesson);
        Lesson expected = lesson;
        Lesson actual = lessonDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateAndGetAllAreColledSouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        LocalDateTime date = LocalDateTime.now();
        
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher, group, room, date));
        lessons.add(new Lesson(2, "Bio", teacher, group, room, date));
        lessons.add(new Lesson(3, "History", teacher, group, room, date));
        lessonDao.create(lessons.get(0));
        lessonDao.create(lessons.get(1));
        lessonDao.create(lessons.get(2));
        List<Lesson> expected = lessons;
        List<Lesson> actual = lessonDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteIsColledSouldDeleteCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        LocalDateTime date = LocalDateTime.now();
        
        lessonDao.create(new Lesson(1, "Math", teacher, group, room, date));
        lessonDao.delete(1);
        List<Lesson> actual = lessonDao.getAll();
        assertTrue(actual.size() == 0);
    }

    @Test
    void whenUpdateIsColledSouldUpdateCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Teacher teacher2 = new Teacher(2, "two", "two");
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        LocalDateTime date = LocalDateTime.now();
        
        
        Lesson lessonBeforeUpdating = new Lesson(1, "Math", teacher, group, room, date);
        Lesson lessonAfterUpdating = new Lesson(1, "Bio", teacher, group, room, date);
        lessonDao.create(lessonBeforeUpdating);
        lessonDao.update(lessonAfterUpdating);
        Lesson expected = lessonAfterUpdating;
        Lesson actual = lessonDao.getById(1);
        List<Lesson> students = lessonDao.getAll();
        assertTrue(students.size() == 1);
        assertEquals(expected, actual);
    }
    
    @Test
    void getLessonByTeacherForDayShouldReturnCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        Teacher teacher2 = new Teacher(2, "two", "two");
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", formatter);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", formatter);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", formatter);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", formatter);
        
        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2);
        Lesson lesson3 = new Lesson(3, "bio", teacher2, group, room, date2);
        Lesson lesson4 = new Lesson(4, "history", teacher, group, room, date3);
        Lesson lesson5 = new Lesson(5, "english", teacher, group, room, date4);
        
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);
        lessonDao.create(lesson5);
        
        List<Lesson> actual = lessonDao.getLessonByTeacherForDay(teacher, date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson2);
        expected.add(lesson4);
        
        assertEquals(expected, actual);
    }
    
    @Test
    void getLessonByTeacherForMonceShouldReturnCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        Teacher teacher2 = new Teacher(2, "two", "two");
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", formatter);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", formatter);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", formatter);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", formatter);
        
        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2);
        Lesson lesson3 = new Lesson(3, "bio", teacher2, group, room, date2);
        Lesson lesson4 = new Lesson(4, "history", teacher, group, room, date3);
        Lesson lesson5 = new Lesson(5, "english", teacher, group, room, date4);
        
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);
        lessonDao.create(lesson5);
        
        List<Lesson> actual = lessonDao.getLessonByTeacherForMonth(teacher, date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);
        expected.add(lesson4);
        
        assertEquals(expected, actual);
    }
    
    @Test
    void getLessonByStudentForDayShouldReturnCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        Teacher teacher2 = new Teacher(2, "two", "two");
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        Student student1 = new Student(1, "student1", "student1", group);
        Student student2 = new Student(2, "student2", "student2", group);
        studentDao.create(student1);
        studentDao.create(student2);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", formatter);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", formatter);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", formatter);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", formatter);
        
        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2);
        Lesson lesson3 = new Lesson(3, "history", teacher, group, room, date3);
        Lesson lesson4 = new Lesson(4, "english", teacher, group, room, date4);
        
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);
        
        List<Lesson> actual = lessonDao.getLessonByStudentForDay(student1, date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson2);
        expected.add(lesson3);
        
        assertEquals(expected, actual);
    }
    
    @Test
    void getLessonByStudentForMonthShouldReturnCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        Teacher teacher2 = new Teacher(2, "two", "two");
        teacherDao.create(teacher);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        Student student1 = new Student(1, "student1", "student1", group);
        Student student2 = new Student(2, "student2", "student2", group);
        studentDao.create(student1);
        studentDao.create(student2);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER);
        LocalDateTime date1 = LocalDateTime.parse("2021.01.20-23.55.11", formatter);
        LocalDateTime date2 = LocalDateTime.parse("2021.01.21-03.00.00", formatter);
        LocalDateTime date3 = LocalDateTime.parse("2021.01.21-23.55.11", formatter);
        LocalDateTime date4 = LocalDateTime.parse("2021.02.11-23.55.11", formatter);
        
        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, date1);
        Lesson lesson2 = new Lesson(2, "bio", teacher, group, room, date2);
        Lesson lesson3 = new Lesson(3, "history", teacher, group, room, date3);
        Lesson lesson4 = new Lesson(4, "english", teacher, group, room, date4);
        
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        lessonDao.create(lesson4);
        
        List<Lesson> actual = lessonDao.getLessonByStudentForMonth(student1, date2);
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson1);
        expected.add(lesson2);
        expected.add(lesson3);
        
        assertEquals(expected, actual);
    }
    
    @AfterEach
    void closeConext() {
        context.close();
    }
}
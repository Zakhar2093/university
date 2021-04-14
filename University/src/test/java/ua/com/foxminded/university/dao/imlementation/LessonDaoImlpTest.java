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
import ua.com.foxminded.university.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
class LessonDaoImlpTest {
    private static final String FORMAT = "yyyy.MM.dd-HH.mm.ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

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
        Lesson expected = createLesson();
        Lesson actual = lessonDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateSouldInsertAndGetCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher, group, room, LocalDateTime.now(), false));
        lessons.add(new Lesson(2, "Bio", teacher, group, room, LocalDateTime.now(), false));
        lessons.add(new Lesson(3, "History", teacher, group, room, LocalDateTime.now(), false));
        lessonDao.create(lessons.get(0));
        lessonDao.create(lessons.get(1));
        lessonDao.create(lessons.get(2));
        List<Lesson> expected = lessons;
        List<Lesson> actual = lessonDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateSouldUpdateCorrectData() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Teacher teacher2 = new Teacher(2, "two", "two", false);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        
        Lesson lessonBeforeUpdating = new Lesson(1, "Math", teacher, group, room, LocalDateTime.now(), false);
        Lesson lessonAfterUpdating = new Lesson(1, "Bio", teacher, group, room, LocalDateTime.now(), false);
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
    void getLessonByTeacherForMonceShouldReturnCorrectData() {
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
    void removeTeacherFromLessonSouldSetNullInStudentTeacher() {
        createLesson();
        lessonDao.removeTeacherFromLesson(1);
        assertTrue(lessonDao.getById(1).getTeacher() == null);
    }
    
    @Test
    void addTeacherToLessonSouldSetTeacherInLesson() {
        createLesson();
        lessonDao.removeTeacherFromLesson(1);
        assertTrue(lessonDao.getById(1).getTeacher() == null);
        lessonDao.addTeacherToLesson(1, 1);
        assertTrue(lessonDao.getById(1).getTeacher().equals(new Teacher(1, "one", "one", false)));
    }
    
    @Test
    void removeRoomFromLessonSouldSetNullInStudentRoom() {
        createLesson();
        lessonDao.removeTeacherFromLesson(1);
        assertTrue(lessonDao.getById(1).getTeacher() == null);
    }
    
    @Test
    void addRoomToLessonSouldSetRoomInLesson() {
        createLesson();
        lessonDao.removeRoomFromLesson(1);
        assertTrue(lessonDao.getById(1).getRoom() == null);
        lessonDao.addRoomToLesson(1, 1);
        assertTrue(lessonDao.getById(1).getRoom().equals(new Room(1, 101)));
    }
    
    @Test
    void removeGroupFromLessonSouldSetNullInStudentGroup() {
        createLesson();
        lessonDao.removeGroupFromLesson(1);
        assertTrue(lessonDao.getById(1).getGroup() == null);
    }
    
    @Test
    void addGroupToLessonSouldSetGroupInLesson() {
        createLesson();
        lessonDao.removeGroupFromLesson(1);
        assertTrue(lessonDao.getById(1).getGroup() == null);
        lessonDao.addGroupToLesson(1, 1);
        assertTrue(lessonDao.getById(1).getGroup().equals(new Group(1, "any name", false)));
    }
    
    @Test
    void deactivateSouldSetTrueInLessonInactive() {
        createLesson();
        lessonDao.removeGroupFromLesson(1);
        lessonDao.removeRoomFromLesson(1);
        lessonDao.removeTeacherFromLesson(1);
        lessonDao.deactivate(1);
        assertTrue(lessonDao.getById(1).isLessonInactive());
    }
    
    @Test
    void activateSouldSetFolseInLessonInactive() {
        createLesson();
        lessonDao.removeGroupFromLesson(1);
        lessonDao.removeRoomFromLesson(1);
        lessonDao.removeTeacherFromLesson(1);
        lessonDao.deactivate(1);
        assertTrue(lessonDao.getById(1).isLessonInactive());
        lessonDao.activate(1);
        assertFalse(lessonDao.getById(1).isLessonInactive());
    }
    
    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 does not exist"));
    }
    
    @Test 
    void whenUpdateNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group group = new Group(1, "any name", false);
            groupDao.create(group);
            Teacher teacher = new Teacher(1, "one", "one", false);
            teacherDao.create(teacher);
            Room room = new Room(1, 101);
            roomDao.create(room);
            lessonDao.update(new Lesson(1, "Math", teacher, group, room, LocalDateTime.now(), false));
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 can not be updated"));
    }
    
    @Test 
    void whenDeactivateNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.deactivate(1);
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 can not be deactivated"));
    }
    
    @Test 
    void whenActivateNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.activate(1);
        });
        assertTrue(thrown.getMessage().contains("Lesson with such id 1 can not be activated"));
    }
    
    @Test
    void whenCreateLessonWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group group = new Group(1, "any name", false);
            groupDao.create(group);
            Teacher teacher = new Teacher(1, "one", "one", false);
            teacherDao.create(teacher);
            Room room = new Room(1, 101);
            roomDao.create(room);
            Lesson lesson = new Lesson(1, null, teacher, group, room, LocalDateTime.now(), false);
            lessonDao.create(lesson);
        });
        assertTrue(thrown.getMessage().contains("Lesson can not be created. Some field is null"));
    }
    
    @Test
    void whenUpdateLessonWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group group = new Group(1, "any name", false);
            groupDao.create(group);
            Teacher teacher = new Teacher(1, "one", "one", false);
            teacherDao.create(teacher);
            Teacher teacher2 = new Teacher(2, "two", "two", false);
            teacherDao.create(teacher2);
            Room room = new Room(1, 101);
            roomDao.create(room);
            
            Lesson lessonBeforeUpdating = new Lesson(1, "Math", teacher, group, room, LocalDateTime.now(), false);
            Lesson lessonAfterUpdating = new Lesson(1, null, teacher, group, room, LocalDateTime.now(), false);
            lessonDao.create(lessonBeforeUpdating);
            lessonDao.update(lessonAfterUpdating);
        });
        assertTrue(thrown.getMessage().contains("Lesson can not be updated. Some new field is null"));
    }
    
    @Test 
    void whenGetLessonByNonexistentTeacherForDayShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Teacher teacher = new Teacher(1, "one", "one", false);
            lessonDao.getLessonByTeacherIdForDay(teacher.getTeacherId(), LocalDateTime.now());
        });
        assertTrue(thrown.getMessage().contains("Can not get lessons by Teacher id = 1. Teacher does not exist"));
    }
    
    @Test 
    void whenGetLessonByNonexistentTeacherForMonceShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Teacher teacher = new Teacher(1, "one", "one", false);
            lessonDao.getLessonByTeacherIdForMonth(teacher.getTeacherId(), LocalDateTime.now());
        });
        assertTrue(thrown.getMessage().contains("Can not get lessons by Teacher id = 1. Teacher does not exist"));
    }
    
    @Test 
    void whenGetLessonByNonexistentStudentForDayShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Student student = new Student(1, "any", "any", null, false);
            lessonDao.getLessonByStudentIdForDay(student.getStudentId(), LocalDateTime.now());
        });
        assertTrue(thrown.getMessage().contains("Can not get lessons by Student id = 1. Student does not exist"));
    }
    
    @Test 
    void whenGetLessonByNonexistentStudentForMonceShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Student student = new Student(1, "any", "any", null, false);
            lessonDao.getLessonByStudentIdForMonth(student.getStudentId(), LocalDateTime.now());
        });
        assertTrue(thrown.getMessage().contains("Can not get lessons by Student id = 1. Student does not exist"));
    }
    
    @Test
    void whenAddGroupToNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.addGroupToLesson(1, 1);
        });
        assertTrue(thrown.getMessage().contains("Group 1 can not be added to lesson id = 1. Group or lesson does not exist"));
    }    
    
    @Test
    void whenAddTeacherToNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.addTeacherToLesson(1, 1);
        });
        assertTrue(thrown.getMessage().contains("Teacher 1 can not be added to lesson id = 1. Teacher or lesson does not exist"));
    }
    
    @Test
    void whenRemoveGroupFromNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.removeGroupFromLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Group can not be removed from lesson id = 1. Lesson does not exist"));
    }
    
    @Test
    void whenRemoveRoomFromNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.removeRoomFromLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Room can not be removed from lesson id = 1. Lesson does not exist"));
    }
    
    @Test
    void whenRemoveTeacherFromNonexistentLessonShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            lessonDao.removeTeacherFromLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher can not be removed from lesson id = 1. Lesson does not exist"));
    }
    
    private Lesson createLesson() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        LocalDateTime date = LocalDateTime.now();
        
        Lesson lesson = new Lesson(1, "Math", teacher, group, room, date, false);
        lessonDao.create(lesson);
        return lesson;
    }
}
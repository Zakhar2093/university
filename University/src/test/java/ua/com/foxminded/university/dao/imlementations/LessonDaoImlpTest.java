package ua.com.foxminded.university.dao.imlementations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementations.LessonDaoImpl;
import ua.com.foxminded.university.dao.implementations.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementations.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Room;
import ua.com.foxminded.university.models.Teacher;

class LessonDaoImlpTest {
    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private LessonDao lessonDao;
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        lessonDao = context.getBean("lessonDaoImpl", LessonDaoImpl.class);
        groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        teacherDao = context.getBean("teacherDaoImpl", TeacherDaoImpl.class);
        roomDao = context.getBean("roomDaoImpl", RoomDaoImpl.class);
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
        
        Lesson lesson = new Lesson(1, "Math", teacher, group, room, null);
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
        
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson(1, "Math", teacher, group, room, new Date(1)));
        lessons.add(new Lesson(2, "Bio", teacher, group, room, new Date(1)));
        lessons.add(new Lesson(3, "History", teacher, group, room, new Date(1)));
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
        
        lessonDao.create(new Lesson(1, "Math", teacher, group, room, new Date(1)));
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
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        
        
        Lesson lessonBeforeUpdating = new Lesson(1, "Math", teacher, group, room, new Date(1));
        Lesson lessonAfterUpdating = new Lesson(1, "Math", teacher2, group, room, new Date(1));
        lessonDao.create(lessonBeforeUpdating);
        lessonDao.update(lessonAfterUpdating);
        Lesson expected = lessonAfterUpdating;
        Lesson actual = lessonDao.getById(1);
        List<Lesson> students = lessonDao.getAll();
        assertTrue(students.size() == 1);
        assertEquals(expected, actual);
    }

    @AfterEach
    void closeConext() {
        context.close();
    }
}
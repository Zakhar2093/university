package ua.com.foxminded.university.dao.imlementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementation.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementation.LessonDaoImpl;
import ua.com.foxminded.university.dao.implementation.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementation.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;

class RoomDaoImplTest {
    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private JdbcTemplate jdbcTemplate;
    private PropertyReader propertyReader;
    private LessonDao lessonDao;
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        propertyReader = context.getBean("propertyReader", PropertyReader.class);
        roomDao = new RoomDaoImpl(jdbcTemplate, propertyReader);
        groupDao = new GroupDaoImpl(jdbcTemplate, propertyReader);
        teacherDao = new TeacherDaoImpl(jdbcTemplate, propertyReader);
        lessonDao = new LessonDaoImpl(jdbcTemplate, propertyReader, groupDao, teacherDao, roomDao);
        dbInit.initialization();;
    }

    @Test
    void getByIdAndCreateSouldInsertAndGetCorrectData() {
        Room room = new Room(1, 101);
        roomDao.create(room);
        Room expected = room;
        Room actual = roomDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateSouldInsertAndGetCorrectData() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, 101));
        rooms.add(new Room(2, 102));
        rooms.add(new Room(3, 103));
        roomDao.create(rooms.get(0));
        roomDao.create(rooms.get(1));
        roomDao.create(rooms.get(2));
        List<Room> expected = rooms;
        List<Room> actual = roomDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void deleteSouldDeleteCorrectData() {
        Group group = new Group(1, "any name");
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);   
        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, LocalDateTime.now());
        lessonDao.create(lesson1);
        
        roomDao.delete(1);
        List<Room> actual = roomDao.getAll();
        assertTrue(actual.size() == 0);
    }

    @Test
    void updateIsColledSouldUpdateCorrectData() {
        Room groupBeforeUpdating = new Room(1, 101);
        Room groupAfterUpdating = new Room(1, 102);
        roomDao.create(groupBeforeUpdating);
        roomDao.update(groupAfterUpdating);
        Room expected = groupAfterUpdating;
        Room actual = roomDao.getById(1);
        List<Room> groups = roomDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @AfterEach
    void closeConext() {
        context.close();
    }
}
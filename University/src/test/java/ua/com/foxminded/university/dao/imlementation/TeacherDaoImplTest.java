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

class TeacherDaoImplTest {
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
        dbInit.initialization();
    }

    @Test
    void getByIdAndCreateSouldInsertAndGetCorrectData() {
        Teacher teacher = new Teacher(1, "one", "one");
        teacherDao.create(teacher);
        Teacher expected = teacher;
        Teacher actual = teacherDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateSouldInsertAndGetCorrectData() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one"));
        teachers.add(new Teacher(2, "two", "two"));
        teachers.add(new Teacher(3, "three", "three"));
        teacherDao.create(teachers.get(0));
        teacherDao.create(teachers.get(1));
        teacherDao.create(teachers.get(2));
        List<Teacher> expected = teachers;
        List<Teacher> actual = teacherDao.getAll();
        assertEquals(expected, actual);
    }

//        Group group = new Group(1, "any name");
//        groupDao.create(group);
//        Teacher teacher = new Teacher(1, "one", "one");
//        teacherDao.create(teacher);
//        Room room = new Room(1, 101);
//        roomDao.create(room);   
//        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room, LocalDateTime.now());
//        lessonDao.create(lesson1);

    @Test
    void updateSouldUpdateCorrectData() {
        Teacher groupBeforeUpdating = new Teacher(1, "one", "one");
        Teacher groupAfterUpdating = new Teacher(1, "two", "one");
        teacherDao.create(groupBeforeUpdating);
        teacherDao.update(groupAfterUpdating);
        Teacher expected = groupAfterUpdating;
        Teacher actual = teacherDao.getById(1);
        List<Teacher> groups = teacherDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @AfterEach
    void closeConext() {
        context.close();
    }
}
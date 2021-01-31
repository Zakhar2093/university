package ua.com.foxminded.university.dao.imlementation;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DaoException;
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
    private StudentDao studentDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
        propertyReader = context.getBean("propertyReader", PropertyReader.class);
        roomDao = new RoomDaoImpl(jdbcTemplate, propertyReader);
        groupDao = new GroupDaoImpl(jdbcTemplate, propertyReader);
        teacherDao = new TeacherDaoImpl(jdbcTemplate, propertyReader);
        studentDao = new StudentDaoImpl(jdbcTemplate, propertyReader, groupDao);
        lessonDao = new LessonDaoImpl(jdbcTemplate, propertyReader, groupDao, teacherDao, roomDao, studentDao);
        dbInit.initialization();
    }

    @Test
    void getByIdAndCreateSouldInsertAndGetCorrectData() {
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Teacher expected = teacher;
        Teacher actual = teacherDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateSouldInsertAndGetCorrectData() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher(1, "one", "one", false));
        teachers.add(new Teacher(2, "two", "two", false));
        teachers.add(new Teacher(3, "three", "three", false));
        teacherDao.create(teachers.get(0));
        teacherDao.create(teachers.get(1));
        teacherDao.create(teachers.get(2));
        List<Teacher> expected = teachers;
        List<Teacher> actual = teacherDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateSouldUpdateCorrectData() {
        Teacher groupBeforeUpdating = new Teacher(1, "one", "one", false);
        Teacher groupAfterUpdating = new Teacher(1, "two", "one", false);
        teacherDao.create(groupBeforeUpdating);
        teacherDao.update(groupAfterUpdating);
        Teacher expected = groupAfterUpdating;
        Teacher actual = teacherDao.getById(1);
        List<Teacher> groups = teacherDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }
    
    @Test
    void deactivateSouldSetTrueInTeacherInactive() {
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);   
        teacherDao.deactivate(1);
        assertTrue(teacherDao.getById(1).isTeacherInactive());
    }
    
    @Test
    void activateSouldSetFolseInTeacherInactive() {
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);   
        teacherDao.deactivate(1);
        assertTrue(teacherDao.getById(1).isTeacherInactive());
        teacherDao.activate(1);
        assertFalse(teacherDao.getById(1).isTeacherInactive());
    }
    
    @Test
    void removeTeacherFromLessonsSouldSetNullInLessonsTeacherId() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher1 = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher1);
        Teacher teacher2 = new Teacher(2, "one", "one", false);
        teacherDao.create(teacher2);
        Room room = new Room(1, 101);
        roomDao.create(room);
        Lesson lesson1 = new Lesson(1, "Math", teacher1, group, room, LocalDateTime.now(), false);
        Lesson lesson2 = new Lesson(2, "Math", teacher1, group, room, LocalDateTime.now(), false);
        Lesson lesson3 = new Lesson(3, "Math", teacher2, group, room, LocalDateTime.now(), false);
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        
        teacherDao.removeTeacherFromAllLessons(1);
        
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson3);
        List<Lesson> actual = lessonDao.getAll()
                .stream()
                .filter((x) -> x.getTeacher() != null)
                .collect(Collectors.toList()
                        ); 
        assertEquals(expected, actual);
    }
    
    @Test
    void getTeacherByLessonSouldReturnCorrectTeacher() {
        Teacher expected = createLesson().getTeacher();
        Teacher actual = teacherDao.getTeacherByLesson(1);
        assertEquals(expected, actual);
    }
    
    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            teacherDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 does not exist"));
    }
    
    @Test
    void whenGetTeacherByLessonGetNonexistentDataShouldThrowsDaoException() {
        createLesson();
        teacherDao.removeTeacherFromAllLessons(1);
        DaoException thrown = assertThrows(DaoException.class, () -> {
            teacherDao.getTeacherByLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Such lesson (id = 1) does not have any teacher"));
    }
    
    @Test 
    void whenUpdateNonexistentTeacherShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            teacherDao.update(new Teacher(1, "any", "any", false));
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 can not be updated"));
    }
    
    @Test 
    void whenDeactivateNonexistentTeacherShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            teacherDao.deactivate(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 can not be deactivated"));
    }
    
    @Test 
    void whenActivateNonexistentTeacherShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            teacherDao.activate(1);
        });
        assertTrue(thrown.getMessage().contains("Teacher with such id 1 can not be activated"));
    }
    
    @Test
    void whenCreateTeacherWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            teacherDao.create(new Teacher());
        });
        assertTrue(thrown.getMessage().contains("Teacher can not be created. Some field is null"));
    }
    
    @Test
    void whenUpdateTeacherWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Teacher groupBeforeUpdating = new Teacher(1, "one", "one", false);
            Teacher groupAfterUpdating = new Teacher(1, null, null, false);
            teacherDao.create(groupBeforeUpdating);
            teacherDao.update(groupAfterUpdating);
        });
        assertTrue(thrown.getMessage().contains("Teacher can not be updated. Some new field is null"));
    }
    
    private Lesson createLesson() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Room room1 = new Room(1, 101);
        roomDao.create(room1);
        Lesson lesson = new Lesson(1, "Math", teacher, group, room1, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        return lesson;
    }

    @AfterEach
    void closeConext() {
        context.close();
    }
}
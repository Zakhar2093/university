package ua.com.foxminded.university.dao.imlementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementation.*;
import ua.com.foxminded.university.dao.interfaces.*;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
class RoomDaoImplTest {
//    private AnnotationConfigApplicationContext context;

    @Autowired
    private PropertyReader propertyReader;

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    @Autowired
//    public RoomDaoImplTest(JdbcTemplate jdbcTemplate, PropertyReader propertyReader) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.propertyReader = propertyReader;
//    }

    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private LessonDao lessonDao;
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;
    private StudentDao studentDao;

    @BeforeEach
    void createBean() {
//        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
//        jdbcTemplate = context.getBean("jdbcTemplate", JdbcTemplate.class);
//        propertyReader = context.getBean("propertyReader", PropertyReader.class);
        roomDao = new RoomDaoImpl(jdbcTemplate, propertyReader);
        groupDao = new GroupDaoImpl(jdbcTemplate, propertyReader);
        teacherDao = new TeacherDaoImpl(jdbcTemplate, propertyReader);
        studentDao = new StudentDaoImpl(jdbcTemplate, propertyReader, groupDao);
        lessonDao = new LessonDaoImpl(jdbcTemplate, propertyReader, groupDao, teacherDao, roomDao, studentDao);
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
    void deactivateSouldSetTrueInRoomInactive() {
        Room room = new Room(1, 101);
        roomDao.create(room);   
        roomDao.deactivate(1);
        assertTrue(roomDao.getById(1).isRoomInactive());
    }
    
    @Test
    void activateSouldSetFolseInRoomInactive() {
        Room room = new Room(1, 101);
        roomDao.create(room);   
        roomDao.deactivate(1);
        assertTrue(roomDao.getById(1).isRoomInactive());
        roomDao.activate(1);
        assertFalse(roomDao.getById(1).isRoomInactive());
    }

    @Test
    void updateSouldUpdateCorrectData() {
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
    
    @Test
    void removeRoomFromLessonsSouldSetNullInLessonsRoomId() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Room room1 = new Room(1, 101);
        roomDao.create(room1);
        Room room2 = new Room(2, 202);
        roomDao.create(room2);
        Lesson lesson1 = new Lesson(1, "Math", teacher, group, room1, LocalDateTime.now(), false);
        Lesson lesson2 = new Lesson(2, "Math", teacher, group, room1, LocalDateTime.now(), false);
        Lesson lesson3 = new Lesson(3, "Math", teacher, group, room2, LocalDateTime.now(), false);
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        
        roomDao.removeRoomFromAllLessons(1);
        
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson3);
        List<Lesson> actual = lessonDao.getAll()
                .stream()
                .filter((x) -> x.getRoom() != null)
                .collect(Collectors.toList()
                        ); 
        assertEquals(expected, actual);
    }
    
    @Test
    void getRoomByLessonSouldReturnCorrectRoom() {
        Room expected = createLesson().getRoom();
        Room actual = roomDao.getRoomByLesson(1);
        assertEquals(expected, actual);
    }
    
    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            roomDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Room with such id 1 does not exist"));
    }
    
    @Test
    void whenGetRoomByLessonGetNonexistentDataShouldThrowsDaoException() {
        createLesson();
        roomDao.removeRoomFromAllLessons(1);
        DaoException thrown = assertThrows(DaoException.class, () -> {
            roomDao.getRoomByLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Such lesson (id = 1) does not have any room"));
    }
    
    @Test 
    void whenUpdateNonexistentRoomShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            roomDao.update(new Room(1, 101));
        });
        assertTrue(thrown.getMessage().contains("Room with such id 1 can not be updated"));
    }
    
    @Test 
    void whenDeactivateNonexistentRoomShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            roomDao.deactivate(1);
        });
        assertTrue(thrown.getMessage().contains("Room with such id 1 can not be deactivated"));
    }
    
    @Test 
    void whenActivateNonexistentRoomShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            roomDao.activate(1);
        });
        assertTrue(thrown.getMessage().contains("Room with such id 1 can not be activated"));
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

//    @AfterEach
//    void closeConext() {
//        context.close();
//    }
}
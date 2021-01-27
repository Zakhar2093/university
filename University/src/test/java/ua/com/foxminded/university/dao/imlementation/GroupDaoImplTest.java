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

class GroupDaoImplTest {
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
        lessonDao = new LessonDaoImpl(jdbcTemplate, propertyReader, groupDao, teacherDao, roomDao);
        dbInit.initialization();
    }

    @Test
    void getByIdAndCreateSouldInsertAndGetCorrectData() {
        Group group = new Group();
        group.setGroupId(1);
        group.setGroupName("any name");
        groupDao.create(group);
        Group expected = group;
        Group actual = groupDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateSouldInsertAndGetCorrectData() {
        List<Group> groups = createTestGroups();
        groupDao.create(groups.get(0));
        groupDao.create(groups.get(1));
        groupDao.create(groups.get(2));
        List<Group> expected = groups;
        List<Group> actual = groupDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateSouldUpdateCorrectData() {
        Group groupBeforeUpdating = new Group(1, "one", false);
        Group groupAfterUpdating = new Group(1, "two", false);
        groupDao.create(groupBeforeUpdating);
        groupDao.update(groupAfterUpdating);
        Group expected = groupAfterUpdating;
        Group actual = groupDao.getById(1);
        List<Group> groups = groupDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }
    
    @Test
    void deactivateSouldSetTrueInGroupInactive() {
        Group group = new Group(1, "one", false);
        groupDao.create(group);   
        groupDao.deactivate(1);
        assertTrue(groupDao.getById(1).isGroupInactive());
    }
    
    @Test
    void activateSouldSetFolseInGroupInactive() {
        Group group = new Group(1, "one", false);
        groupDao.create(group);  
        groupDao.deactivate(1);
        assertTrue(groupDao.getById(1).isGroupInactive());
        groupDao.activate(1);
        assertFalse(groupDao.getById(1).isGroupInactive());
    }
    
    @Test
    void removeGroupFromLessonsSouldSetNullInLessonsGroupId() {
        Group group1 = new Group(1, "any name", false);
        groupDao.create(group1);
        Group group2 = new Group(2, "any name", false);
        groupDao.create(group2);
        Teacher teacher = new Teacher(1, "one", "one", false);
        teacherDao.create(teacher);
        Room room = new Room(1, 101);
        roomDao.create(room);
        Lesson lesson1 = new Lesson(1, "Math", teacher, group1, room, LocalDateTime.now(), false);
        Lesson lesson2 = new Lesson(2, "Math", teacher, group1, room, LocalDateTime.now(), false);
        Lesson lesson3 = new Lesson(3, "Math", teacher, group2, room, LocalDateTime.now(), false);
        lessonDao.create(lesson1);
        lessonDao.create(lesson2);
        lessonDao.create(lesson3);
        
        groupDao.removeGroupFromAllLessons(1);
        
        List<Lesson> expected = new ArrayList<>();
        expected.add(lesson3);
        List<Lesson> actual = lessonDao.getAll()
                .stream()
                .filter((x) -> x.getGroup() != null)
                .collect(Collectors.toList()
                        ); 
        assertEquals(expected, actual);
    }

    @Test
    void removeGroupFromLessonsSouldSetNullInStudentsGroupId() {
        Group group1 = new Group(1, "any name", false);
        groupDao.create(group1);
        Group group2 = new Group(2, "any name", false);
        groupDao.create(group2);
        Student student1 = new Student(1, "one", "one", group1, false);
        Student student2 = new Student(2, "one", "one", group1, false);
        Student student3 = new Student(3, "one", "one", group2, false);
        studentDao.create(student1);
        studentDao.create(student2);
        studentDao.create(student3);
        
        groupDao.removeGroupFromAllStudents(1);
        
        List<Student> expected = new ArrayList<>();
        expected.add(student3);
        List<Student> actual = studentDao.getAll()
                .stream()
                .filter((x) -> x.getGroup() != null)
                .collect(Collectors.toList()
                        ); 
        
        assertEquals(expected, actual);
    }

    private List<Group> createTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "one", false));
        groups.add(new Group(2, "two", false));
        groups.add(new Group(3, "three", false));
        return groups;
    }
    
    @AfterEach
    void closeConext() {
        context.close();
    }
}
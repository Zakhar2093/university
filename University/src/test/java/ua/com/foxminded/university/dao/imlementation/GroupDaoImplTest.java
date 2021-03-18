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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
class GroupDaoImplTest {
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
    
    @Test
    void getGroupByLessonSouldReturnCorrectGroup() {
        Group expected = createLesson().getGroup();
        Group actual = groupDao.getGroupByLesson(1);
        assertEquals(expected, actual);
    }
    
    @Test
    void getGroupByStudentSouldReturnCorrectGroup() {
        Group expected = createStudent().getGroup();
        Group actual = groupDao.getGroupByStudent(1);
        assertEquals(expected, actual);
    }
    
    @Test
    void whenGetByIdGetNonexistentDataShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.getById(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 does not exist"));
    }
    
    @Test
    void whenGetGroupByLessontGetNonexistentDataShouldThrowsDaoException() {
        createLesson();
        groupDao.removeGroupFromAllLessons(1);
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.getGroupByLesson(1);
        });
        assertTrue(thrown.getMessage().contains("Such lesson (id = 1) does not have any group"));
    }
    @Test
    void whenGetGroupByStudentGetNonexistentDataShouldThrowsDaoException() {
        createStudent();
        groupDao.removeGroupFromAllStudents(1);
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.getGroupByStudent(1);
        });
        assertTrue(thrown.getMessage().contains("Such student (id = 1) does not have any group"));
    }
    
    @Test 
    void whenUpdateNonexistentGroupShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.update(new Group(1, "any name", false));
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 can not be updated"));
    }
    
    @Test 
    void whenDeactivateNonexistentGroupShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.deactivate(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 can not be deactivated"));
    }
    
    @Test 
    void whenActivateNonexistentGroupShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.activate(1);
        });
        assertTrue(thrown.getMessage().contains("Group with such id 1 can not be activated"));
    }
    
    @Test
    void whenCreateGroupWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            groupDao.create(new Group());
        });
        assertTrue(thrown.getMessage().contains("Group can not be created. Some field is null"));
    }
    
    @Test
    void whenUpdateGroupWithNullShouldThrowsDaoException() {
        DaoException thrown = assertThrows(DaoException.class, () -> {
            Group groupBeforeUpdating = new Group(1, "one", false);
            Group groupAfterUpdating = new Group(1, null, false);
            groupDao.create(groupBeforeUpdating);
            groupDao.update(groupAfterUpdating);
        });
        assertTrue(thrown.getMessage().contains("Group can not be updated. Some new field is null"));
    }
    
    
    private Student createStudent() {
        Group group = new Group(1, "any name", false);
        groupDao.create(group);
        Student student = new Student(1, "one", "one", group,false);
        studentDao.create(student);
        return student;
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

    private List<Group> createTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "one", false));
        groups.add(new Group(2, "two", false));
        groups.add(new Group(3, "three", false));
        return groups;
    }
}
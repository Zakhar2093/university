package ua.com.foxminded.university.dao.imlementations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.models.Group;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;

class GroupDaoImplTest {
    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private GroupDao groupDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        dbInit.initialization();
    }

    @Test
    void whenCreateAndGetByIdAreColledSouldInsertAndGetCorrectData() {
        Group group = new Group();
        group.setGroupId(1);
        group.setGroupName("any name");
        groupDao.create(group);
        Group expected = group;
        Group actual = groupDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateAndGetAllAreColledSouldInsertAndGetCorrectData() {
        List<Group> groups = createTestGroups();
        groupDao.create(groups.get(0));
        groupDao.create(groups.get(1));
        groupDao.create(groups.get(2));
        List<Group> expected = groups;
        List<Group> actual = groupDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void whenDeleteIsColledSouldDeleteCorrectData() {
        groupDao.create(new Group(1, "one"));
        groupDao.delete(1);
        List<Group> actual = groupDao.getAll();
        assertTrue(actual.size() == 0);
    }

    @Test
    void whenUpdateIsColledSouldUpdateCorrectData() {
        Group groupBeforeUpdating = new Group(1, "one");
        Group groupAfterUpdating = new Group(1, "two");
        groupDao.create(groupBeforeUpdating);
        groupDao.update(groupAfterUpdating);
        Group expected = groupAfterUpdating;
        Group actual = groupDao.getById(1);
        List<Group> groups = groupDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    private List<Group> createTestGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group(1, "one"));
        groups.add(new Group(2, "two"));
        groups.add(new Group(3, "three"));
        return groups;
    }

    @AfterEach
    void closeConext() {
        context.close();
    }

}

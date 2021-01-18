package ua.com.foxminded.university.dao.imlementations;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.DatabaseInitialization;
import ua.com.foxminded.university.dao.implementations.RoomDaoImpl;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.models.Room;

class RoomDaoImplTest {
    private DatabaseInitialization dbInit = new DatabaseInitialization();
    private AnnotationConfigApplicationContext context;
    private RoomDao roomDao;

    @BeforeEach
    void createBean() {
        context = new AnnotationConfigApplicationContext(SpringConfigTest.class);
        roomDao = context.getBean("roomDaoImpl", RoomDaoImpl.class);
        dbInit.initialization();
    }

    @Test
    void whenCreateAndGetByIdAreColledSouldInsertAndGetCorrectData() {
        Room room = new Room(1, 101);
        roomDao.create(room);
        Room expected = room;
        Room actual = roomDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void whenCreateAndGetAllAreColledSouldInsertAndGetCorrectData() {
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
    void whenDeleteIsColledSouldDeleteCorrectData() {
        roomDao.create(new Room(1, 101));
        roomDao.delete(1);
        List<Room> actual = roomDao.getAll();
        assertTrue(actual.size() == 0);
    }

    @Test
    void whenUpdateIsColledSouldUpdateCorrectData() {
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
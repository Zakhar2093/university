package ua.com.foxminded.university.dao.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import ua.com.foxminded.university.SpringConfigTest;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringConfigTest.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoomRepositoryTest {

    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private RoomDao roomDao;

    @Test
    void getByIdAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        Room room = new Room(1, 1, lessons,false);
        roomDao.create(room);
        Room expected = room;
        Room actual = roomDao.getById(1);
        assertEquals(expected, actual);
    }

    @Test
    void getAllAndCreateShouldInsertAndGetCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room(1, 1, lessons,false));
        rooms.add(new Room(2, 2, lessons,false));
        rooms.add(new Room(3, 3, lessons,false));
        roomDao.create(rooms.get(0));
        roomDao.create(rooms.get(1));
        roomDao.create(rooms.get(2));
        List<Room> expected = rooms;
        List<Room> actual = roomDao.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void updateShouldUpdateCorrectData() {
        List<Lesson> lessons = new ArrayList<>();
        Room groupBeforeUpdating = new Room(1, 1, lessons,false);
        Room groupAfterUpdating = new Room(1, 1, lessons,false);
        roomDao.create(groupBeforeUpdating);
        roomDao.update(groupAfterUpdating);
        Room expected = groupAfterUpdating;
        Room actual = roomDao.getById(1);
        List<Room> groups = roomDao.getAll();
        assertTrue(groups.size() == 1);
        assertEquals(expected, actual);
    }

    @Test
    void deactivateShouldSetTrueInRoomInactive() {
        createLesson();
        roomDao.deactivate(1);
        assertTrue(roomDao.getById(1).isRoomInactive());
        assertNull(lessonDao.getById(1).getRoom());
    }

    @Test
    void activateShouldSetFalseInRoomInactive() {
        Room room = new Room(1, 1, true);
        roomDao.create(room);
        assertTrue(roomDao.getById(1).isRoomInactive());
        roomDao.activate(1);
        assertFalse(roomDao.getById(1).isRoomInactive());
    }

    @Test
    void removeRoomFromLessonsShouldSetNullInLessonsRoomId() {
        Room room1 = new Room(1, 1, false);
        Room room2 = new Room(2, 2, false);
        roomDao.create(room1);
        roomDao.create(room2);
        Lesson lesson1 = new Lesson(1, "Math", null, null, room1, LocalDateTime.now(), false);
        Lesson lesson2 = new Lesson(2, "Math", null, null, room1, LocalDateTime.now(), false);
        Lesson lesson3 = new Lesson(3, "Math", null, null, room2, LocalDateTime.now(), false);
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
    void getRoomByLessonShouldReturnCorrectRoom() {
        Lesson lesson = createLesson();
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);
        Room expected = new Room(1, 1, lessons ,false);
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

    private Lesson createLesson() {
        Room room = new Room(1, 1, false);
        roomDao.create(room);
        Lesson lesson = new Lesson(1, "Math", null, null, room, LocalDateTime.now(), false);
        lessonDao.create(lesson);
        return lesson;
    }
}
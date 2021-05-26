package ua.com.foxminded.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import ua.com.foxminded.university.Application;
import ua.com.foxminded.university.DataSourceTestConfig;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {Application.class, DataSourceTestConfig.class})
@TestPropertySource(locations = "classpath:testApplication.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RoomRepositoryTest {

    private LessonRepository lessonRepository;
    private RoomRepository roomRepository;

    @Autowired
    public RoomRepositoryTest(LessonRepository lessonRepository, RoomRepository roomRepository) {
        this.lessonRepository = lessonRepository;
        this.roomRepository = roomRepository;
    }

    @Test
    void removeRoomFromAllLessonsShouldSetNullInLessonRoom(){
        Room room1 = new Room(1, 101, false);
        roomRepository.save(room1);
        Room room2 = new Room(2, 102, false);
        roomRepository.save(room2);
        Lesson lesson1 = new Lesson(1, "Math", null, null, room1, LocalDateTime.now(), false);
        lessonRepository.save(lesson1);
        Lesson lesson2 = new Lesson(2, "Bio", null, null, room2, LocalDateTime.now(), false);
        lessonRepository.save(lesson2);
        Lesson lesson3 = new Lesson(3, "History", null, null, room1, LocalDateTime.now(), false);
        lessonRepository.save(lesson3);

        roomRepository.removeRoomFromAllLessons(room1.getRoomId());

        assertNull(lessonRepository.findById(1).get().getRoom());
        assertNotNull(lessonRepository.findById(2).get().getRoom());
        assertNull(lessonRepository.findById(3).get().getRoom());
    }
}

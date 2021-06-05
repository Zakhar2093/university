package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.com.foxminded.university.model.Room;

import javax.transaction.Transactional;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Lesson L SET L.room = null WHERE L.room.roomId = :roomId")
    void removeRoomFromAllLessons(Integer roomId);
}

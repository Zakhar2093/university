package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {

}

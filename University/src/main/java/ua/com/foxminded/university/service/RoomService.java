package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.repository.RoomRepository;

import java.util.List;

@Component
@Transactional
public class RoomService implements GenericService<Room, Integer>{

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        super();
        this.roomRepository = roomRepository;
    }

    public void create(Room room) {
        roomRepository.save(room);
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public List<Room> getAllActivated() {
        List<Room> rooms = roomRepository.findAll();
        rooms.removeIf(p -> (p.isRoomInactive()));
        return rooms;
    }

    public Room getById(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Room with such id %d does not exist", roomId)));
    }

    public void update(Room room) {
        roomRepository.save(room);
    }

    public void deactivate(Integer roomId) {
        roomRepository.deactivate(roomId);
        roomRepository.removeRoomFromAllLessons(roomId);
    }

    public void activate(Integer roomId) {
        Room room = getById(roomId);
        room.setRoomInactive(false);
        roomRepository.save(room);
    }
}

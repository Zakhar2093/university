package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.repository.RoomRepository;

import java.util.List;

@Component
public class RoomService implements GenericService<Room, Integer>{

    private RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        super();
        this.roomRepository = roomRepository;
    }

    public void create(Room room) {
        try {
            roomRepository.save(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Room> getAll() {
        try {
            return roomRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Room> getAllActivated() {
        try {
            List<Room> rooms = roomRepository.findAll();
            rooms.removeIf(p -> (p.isRoomInactive()));
            return rooms;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Room getById(Integer roomId) {
        try {
            return roomRepository.findById(roomId)
                    .orElseThrow(() -> new ServiceException(
                            String.format("Room with such id %d does not exist", roomId)
                    ));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Room room) {
        try {
            roomRepository.save(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public void deactivate(Integer roomId) {
        try {
            roomRepository.deactivate(roomId);
            roomRepository.removeRoomFromAllLessons(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer roomId) {
        try {
            Room room = getById(roomId);
            room.setRoomInactive(false);
            roomRepository.save(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}

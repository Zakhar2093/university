package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.repository.RoomRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Room;

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
            roomRepository.create(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    
    public List<Room> getAll(){
        try {
            return roomRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Room> getAllActivated() {
        try {
            List<Room> rooms = roomRepository.getAll();
            rooms.removeIf(p -> (p.isRoomInactive()));
            return rooms;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Room getById(Integer roomId) {
        try {
            return roomRepository.getById(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Room room) {
        try {
            roomRepository.update(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer roomId) {
        try {
            roomRepository.deactivate(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    
    public void activate(Integer roomId) {
        try {
            roomRepository.activate(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}

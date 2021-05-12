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

    private RoomRepository roomDao;

    @Autowired
    public RoomService(RoomRepository roomDao) {
        super();
        this.roomDao = roomDao;
    }
    
    public void create(Room room) {
        try {
            roomDao.create(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    
    public List<Room> getAll(){
        try {
            return roomDao.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Room> getAllActivated() {
        try {
            List<Room> rooms = roomDao.getAll();
            rooms.removeIf(p -> (p.isRoomInactive()));
            return rooms;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Room getById(Integer roomId) {
        try {
            return roomDao.getById(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Room room) {
        try {
            roomDao.update(room);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer roomId) {
        try {
            roomDao.deactivate(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    
    public void activate(Integer roomId) {
        try {
            roomDao.activate(roomId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}

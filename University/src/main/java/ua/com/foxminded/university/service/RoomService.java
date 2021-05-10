package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Component
public class RoomService implements GenericService<Room, Integer>{

    private RoomDao roomDao;

    @Autowired
    public RoomService(RoomDao roomDao) {
        super();
        this.roomDao = roomDao;
    }
    
    public void create(Room room) {
        try {
            roomDao.create(room);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public List<Room> getAll(){
        try {
            return roomDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Room> getAllActivated() {
        try {
            List<Room> rooms = roomDao.getAll();
            rooms.removeIf(p -> (p.isRoomInactive()));
            return rooms;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Room getById(Integer roomId) {
        try {
            return roomDao.getById(roomId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Room room) {
        try {
            roomDao.update(room);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer roomId) {
        try {
            roomDao.deactivate(roomId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public void activate(Integer roomId) {
        try {
            roomDao.activate(roomId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}

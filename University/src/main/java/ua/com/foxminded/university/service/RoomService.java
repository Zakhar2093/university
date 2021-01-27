package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.model.Room;

@Component
public class RoomService {

    private RoomDao roomDao;

    @Autowired
    public RoomService(RoomDao roomDao) {
        super();
        this.roomDao = roomDao;
    }
    
    public void create(Room room) {
        roomDao.create(room);
    }
    
    public List<Room> getAll(){
        return roomDao.getAll();
    }

    public Room getById(Integer roomId) {
        return roomDao.getById(roomId);
    }

    public void update(Room room) {
        roomDao.update(room);
    }

    @Transactional
    public void deactivate(Integer roomId) {
        roomDao.removeRoomFromLessons(roomId);
        roomDao.deactivate(roomId);
    }
    
    public void activate(Integer roomId) {
        roomDao.activate(roomId);
    }
}

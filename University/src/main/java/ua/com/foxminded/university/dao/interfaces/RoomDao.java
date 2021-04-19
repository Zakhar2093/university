package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Room;

public interface RoomDao extends GenericDao<Room, Integer>{
    
    void removeRoomFromAllLessons(Integer roomId);

    Room getRoomByLesson(Integer lessonId);
}

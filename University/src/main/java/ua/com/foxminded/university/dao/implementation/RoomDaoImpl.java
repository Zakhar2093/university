package ua.com.foxminded.university.dao.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Room;

@Component
public class RoomDaoImpl implements RoomDao {

    private final static String PROPERTY_NAME = "src/main/resources/SqlQueries.properties";
    private final JdbcTemplate jdbcTemplate;
    private final PropertyReader propertyReader;

    @Autowired
    public RoomDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
    }

    public List<Room> getAll() {
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "room.getAll"), 
                new BeanPropertyRowMapper<>(Room.class)
                );
    }

    public Room getById(Integer roomId) {
        return jdbcTemplate
                .query(
                        propertyReader.read(PROPERTY_NAME, "room.getById"), 
                        new Object[] { roomId },
                        new BeanPropertyRowMapper<>(Room.class)
                        )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Room with such id %d does not exist", roomId)));
                        
    }

    public void create(Room room) {
        try {
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.create"), room.getRoomNumber(), room.isRoomInactive());
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Room can not be created. Some field is null"), e);
        }
    }

    public void update(Room room) {
        try {
            getById(room.getRoomId());
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.update"), room.getRoomNumber(), room.isRoomInactive(), room.getRoomId());
        } catch (DaoException e) {
            throw new DaoException(String.format("Room with such id %d can not be updated", room.getRoomId()), e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Room can not be updated. Some new field is null"), e);
        }
    }

    public void removeRoomFromAllLessons(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.removeRoomFromAllLessons"), roomId);
    }
    
    public void deactivate(Integer roomId) {
        try {
            getById(roomId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.deactivate"), roomId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Room with such id %d can not be deactivated", roomId), e);
        }
    }
    
    public void activate(Integer roomId) {
        try {
            getById(roomId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.activate"), roomId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Room with such id %d can not be activated", roomId), e);
        }
    }
    
    public Room getRoomByLesson(Integer lessonId) {
         return jdbcTemplate
                 .query(
                     propertyReader.read(PROPERTY_NAME, "room.getRoomByLesson"), 
                     new Object[] { lessonId },
                     new BeanPropertyRowMapper<>(Room.class)
                 )
                 .stream()
                 .findFirst()
                 .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any room", lessonId))
                         );
    }
}
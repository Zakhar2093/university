package ua.com.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Room;

import java.util.List;

@Component
public class RoomDaoImpl implements RoomDao {

    private static final Logger logger = LoggerFactory.getLogger(RoomDaoImpl.class);
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
        logger.debug("Getting all room");
//        String ss = propertyReader.read(PROPERTY_NAME, "room.getAll");
        String ss = "SELECT * FROM university.rooms";
        return jdbcTemplate.query(
                ss,
                new BeanPropertyRowMapper<>(Room.class)
                );
    }

    public Room getById(Integer roomId) {
        logger.debug("Getting room by id = {}", roomId);
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
        logger.debug("Creating room with number {}", room.getRoomNumber());
        try {
            String ss = "INSERT INTO university.rooms (room_number, room_inactive) VALUES (?, ?)";
            jdbcTemplate.update(ss, room.getRoomNumber(), room.isRoomInactive());
        } catch (DataIntegrityViolationException e) {
            logger.error("Creating was not successful. Room can not be created. Some field is null", e);
            throw new DaoException("Room can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public void update(Room room) {
        logger.debug("Updating room with number {}", room.getRoomNumber());
        try {
            getById(room.getRoomId());
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.update"), room.getRoomNumber(), room.isRoomInactive(), room.getRoomId());
        } catch (DaoException e) {
            logger.error("Updating was not successful. Room with such id = {} can not be updated", room.getRoomId(), e);
            throw new DaoException(String.format("Room with such id %d can not be updated", room.getRoomId()), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Updating was not successful. Room can not be updated. Some new field is null", e);
            throw new DaoException("Room can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void removeRoomFromAllLessons(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.removeRoomFromAllLessons"), roomId);
        logger.debug("removed Room with id = {} from all Lessons", roomId);
    }
    
    public void deactivate(Integer roomId) {
        logger.debug("Deactivating room with id = {}", roomId);
        try {
            getById(roomId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.deactivate"), roomId);
        } catch (DaoException e) {
            logger.error("Deactivating was not successful", e);
            throw new DaoException(String.format("Room with such id %d can not be deactivated", roomId), e);
        }
        logger.debug("Deactivating was successful", roomId);
    }
    
    public void activate(Integer roomId) {
        logger.debug("Activating room with id = {}", roomId);
        try {
            getById(roomId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.activate"), roomId);
        } catch (DaoException e) {
            logger.error("Activating was not successful", e);
            throw new DaoException(String.format("Room with such id %d can not be activated", roomId), e);
        }
        logger.debug("Activating was successful", roomId);
    }
    
    public Room getRoomByLesson(Integer lessonId) {
        logger.debug("Getting Room By Lesson id = {}", lessonId);
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
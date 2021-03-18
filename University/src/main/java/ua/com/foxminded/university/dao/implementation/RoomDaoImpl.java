package ua.com.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Room;

import java.util.List;

@Component
public class RoomDaoImpl implements RoomDao {

    private static final Logger logger = LoggerFactory.getLogger(RoomDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private Environment env;

    @Autowired
    public RoomDaoImpl(JdbcTemplate jdbcTemplate, Environment env) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
    }

    public List<Room> getAll() {
        logger.debug("Getting all room");
        String sql = env.getProperty("room.getAll");
        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(Room.class)
                );
    }

    public Room getById(Integer roomId) {
        logger.debug("Getting room by id = {}", roomId);
        return jdbcTemplate
                .query(
                        env.getProperty("room.getById"),
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
            String sql = "INSERT INTO university.rooms (room_number, room_inactive) VALUES (?, ?)";
            jdbcTemplate.update(sql, room.getRoomNumber(), room.isRoomInactive());
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
            jdbcTemplate.update(env.getProperty("room.update"), room.getRoomNumber(), room.isRoomInactive(), room.getRoomId());
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
        jdbcTemplate.update(env.getProperty("room.removeRoomFromAllLessons"), roomId);
        logger.debug("removed Room with id = {} from all Lessons", roomId);
    }
    
    public void deactivate(Integer roomId) {
        logger.debug("Deactivating room with id = {}", roomId);
        try {
            getById(roomId);
            jdbcTemplate.update(env.getProperty("room.deactivate"), roomId);
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
            jdbcTemplate.update(env.getProperty("room.activate"), roomId);
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
                     env.getProperty("room.getRoomByLesson"),
                     new Object[] { lessonId },
                     new BeanPropertyRowMapper<>(Room.class)
                 )
                 .stream()
                 .findFirst()
                 .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any room", lessonId))
                         );
    }
}
package ua.com.foxminded.university.dao.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
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
                .findAny()
                .orElseThrow(() -> new DaoException("Room with such id does not exist"));
    }

    public void create(Room room) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.create"), room.getRoomNumber());
    }

    public void update(Room room) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.update"), room.getRoomNumber(), room.getRoomId());
    }

    public void delete(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.delete"), roomId);
    }
    
    public void deleteRoomFromLessons(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.deleteRoomFromLessons"), roomId);
    }
    
    public void deactivate(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.deactivate"), roomId);
    }
    
    public void activate(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "room.activate"), roomId);
    }
}
package ua.com.foxminded.university.dao.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.models.Room;

@Component
public class RoomDaoImpl implements RoomDao {

    private final static String propertyName = "src/main/resources/SqlQueries.properties";
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
                propertyReader.read(propertyName, "room.getAll"), 
                new BeanPropertyRowMapper<>(Room.class)
                );
    }

    public Room getById(Integer roomId) {
        return jdbcTemplate
                .query(
                        propertyReader.read(propertyName, "room.getById"), 
                        new Object[] { roomId },
                        new BeanPropertyRowMapper<>(Room.class)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException("Room with such id does not exist"));
    }

    public void create(Room room) {
        jdbcTemplate.update(propertyReader.read(propertyName, "room.create"), room.getRoomNumber());
    }

    public void update(Room room) {
        jdbcTemplate.update(propertyReader.read(propertyName, "room.update"), room.getRoomNumber(), room.getRoomId());
    }

    public void delete(Integer roomId) {
        jdbcTemplate.update(propertyReader.read(propertyName, "room.delete"), roomId);
    }
}
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

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.models.Room;

@Component
public class RoomDaoImpl implements RoomDao{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Room> getAll() {
        return jdbcTemplate.query(readProperty("Room_getAll"), new BeanPropertyRowMapper<>(Room.class));
    }

    public Room getById(Integer roomId) {
        return jdbcTemplate
                .query(readProperty("Room_getById"), new Object[] { roomId }, new BeanPropertyRowMapper<>(Room.class))
                .stream().findAny().orElseThrow(() -> new DaoException("Room with such id does not exist"));
    }

    public void create(Room room) {
        jdbcTemplate.update(readProperty("Room_create"), room.getRoomNumber());
    }

    public void update(Room room) {
        jdbcTemplate.update(readProperty("Room_update"), room.getRoomNumber(), room.getRoomId());
    }

    public void delete(Integer roomId) {
        jdbcTemplate.update(readProperty("Room_delete"), roomId);
    }

    private String readProperty(String key) {
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/SqlQueries.properties");
            property.load(fis);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property.getProperty(key);
    }
}

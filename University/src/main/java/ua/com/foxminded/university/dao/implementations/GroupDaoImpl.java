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
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.models.Group;

@Component
public class GroupDaoImpl implements GroupDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupDaoImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Group> getAll() {
        return jdbcTemplate.query(readProperty("Group_getAll"), new BeanPropertyRowMapper<>(Group.class));
    }

    public Group getById(Integer groupId) {
        return jdbcTemplate
                .query(readProperty("Group_getById"), new Object[] { groupId }, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElseThrow(() -> new DaoException("Group with such id does not exist"));
    }

    public void create(Group group) {
        jdbcTemplate.update(readProperty("Group_create"), group.getGroupName());
    }

    public void update(Group group) {
        jdbcTemplate.update(readProperty("Group_update"), group.getGroupName(), group.getGroupId());
    }

    public void delete(Integer groupId) {
        jdbcTemplate.update(readProperty("Group_delete"), groupId);
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
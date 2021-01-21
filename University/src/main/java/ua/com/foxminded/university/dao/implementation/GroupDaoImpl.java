package ua.com.foxminded.university.dao.implementation;

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
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.model.Group;

@Component
public class GroupDaoImpl implements GroupDao {

    private final static String PROPERTY_NAME = "src/main/resources/SqlQueries.properties";
    private final JdbcTemplate jdbcTemplate;
    private final PropertyReader propertyReader;

    @Autowired
    public GroupDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
    }

    public List<Group> getAll() {
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "group.getAll"), 
                new BeanPropertyRowMapper<>(Group.class)
                );
    }

    public Group getById(Integer groupId) {
        return jdbcTemplate
                .query(
                        propertyReader.read(PROPERTY_NAME, "group.getById"), 
                        new Object[] { groupId }, 
                        new BeanPropertyRowMapper<>(Group.class)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException("Group with such id does not exist"));
    }

    public void create(Group group) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.create"), group.getGroupName());
    }

    public void update(Group group) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.update"), group.getGroupName(), group.getGroupId());
    }

    public void delete(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.delete"), groupId);
    }
}
package ua.com.foxminded.university.dao.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
                        propertyReader.read(PROPERTY_NAME, "group.get"
                                + "ById"), 
                        new Object[] { groupId }, 
                        new BeanPropertyRowMapper<>(Group.class)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException("Group with such id does not exist"));
    }

    public void create(Group group) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.create"), group.getGroupName(), group.isGroupInactive());
    }

    public void update(Group group) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.update"), group.getGroupName(), group.isGroupInactive(), group.getGroupId());
    } 
    
    public void removeGroupFromStudents(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.removeGroupFromStudents"), groupId);
    }

    public void removeGroupFromLessons(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.removeGroupFromLessons"), groupId);
    }
    
    public void deactivate(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.deactivate"), groupId);
    }
    
    public void activate(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.activate"), groupId);
    }
}
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
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Room;

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
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Group with such id %d does not exist", groupId))
                        );
    }

    public void create(Group group) {
        try {
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.create"), group.getGroupName(), group.isGroupInactive());
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Group can not be created. Some field is null"), e);
        }
    }

    public void update(Group group) {
        try {
            getById(group.getGroupId());
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.update"), group.getGroupName(), group.isGroupInactive(), group.getGroupId());
        } catch (DaoException e) {
            throw new DaoException(String.format("Group with such id %d can not be updated", group.getGroupId()), e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Group can not be updated. Some new field is null"), e);
        }
    } 
    
    public void removeGroupFromAllStudents(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.removeGroupFromAllStudents"), groupId);
    }

    public void removeGroupFromAllLessons(Integer groupId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.removeGroupFromAllLessons"), groupId);
    }
    
    public void deactivate(Integer groupId) {
        try {
            getById(groupId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.deactivate"), groupId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Group with such id %d can not be deactivated", groupId), e);
        }
    }
    
    public void activate(Integer groupId) {
        try {
            getById(groupId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "group.activate"), groupId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Group with such id %d can not be activated", groupId), e);
        }
    }
    
    public Group getGroupByLesson(Integer lessonId) {
        return jdbcTemplate
            .query(
                    propertyReader.read(PROPERTY_NAME, "group.getRoomByLesson"), 
                    new Object[] { lessonId },
                    new BeanPropertyRowMapper<>(Group.class)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any group", lessonId))
                        );
   }
    
    public Group getGroupByStudent(Integer studentId) {
        return jdbcTemplate
            .query(
                    propertyReader.read(PROPERTY_NAME, "group.getRoomByStudent"), 
                    new Object[] { studentId },
                    new BeanPropertyRowMapper<>(Group.class)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Such student (id = %d) does not have any group", studentId))
                        );
   }
}
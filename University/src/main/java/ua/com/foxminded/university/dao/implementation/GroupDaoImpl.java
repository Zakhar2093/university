package ua.com.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Group;

import java.util.List;

@Component
public class GroupDaoImpl implements GroupDao {

    private static final Logger logger = LoggerFactory.getLogger(GroupDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private Environment env;

    @Autowired
    public GroupDaoImpl(JdbcTemplate jdbcTemplate, Environment env) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
    }

    public List<Group> getAll() {
        logger.debug("Getting all groups");
        return jdbcTemplate.query(
                env.getProperty("group.getAll"),
                new BeanPropertyRowMapper<>(Group.class)
                );
    }

    public Group getById(Integer groupId) { 
        logger.debug("Getting group by id = {}", groupId);
        return jdbcTemplate
                .query(
                        env.getProperty("group.getById"),
                        new Object[] { groupId }, 
                        new BeanPropertyRowMapper<>(Group.class)
                        )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Group with such id %d does not exist", groupId))
                        );
    }

    public void create(Group group) {
        logger.debug("Creating group with name {}", group.getGroupName());
        try {
            jdbcTemplate.update(env.getProperty("group.create"), group.getGroupName(), group.isGroupInactive());
        } catch (DataIntegrityViolationException e) {
            logger.error("Creating was not successful. Group can not be created. Some field is null", e);
            throw new DaoException("Group can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public void update(Group group) {
        logger.debug("Updating group with name {}", group.getGroupName());
        try {
            getById(group.getGroupId());
            jdbcTemplate.update(env.getProperty("group.update"), group.getGroupName(), group.isGroupInactive(), group.getGroupId());
        } catch (DaoException e) {
            logger.error("Updating was not successful. Group with such id = {} can not be updated", group.getGroupId(), e);
            throw new DaoException(String.format("Group with such id %d can not be updated", group.getGroupId()), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Updating was not successful. Group can not be updated. Some new field is null", e);
            throw new DaoException("Group can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    } 
    
    public void removeGroupFromAllStudents(Integer groupId) {
        jdbcTemplate.update(env.getProperty("group.removeGroupFromAllStudents"), groupId);
        logger.debug("removed Group with id = {} from all Students", groupId);
    }

    public void removeGroupFromAllLessons(Integer groupId) {
        jdbcTemplate.update(env.getProperty("group.removeGroupFromAllLessons"), groupId);
        logger.debug("removed Group with id = {} from all Lessons", groupId);
    }
    
    public void deactivate(Integer groupId) {
        logger.debug("Deactivating group with id = {}", groupId);
        try {
            getById(groupId);
            jdbcTemplate.update(env.getProperty("group.deactivate"), groupId);
        } catch (DaoException e) {
            logger.error("Deactivating was not successful", e);
            throw new DaoException(String.format("Group with such id %d can not be deactivated", groupId), e);
        }
        logger.debug("Deactivating was successful", groupId);
    }
    
    public void activate(Integer groupId) {
        logger.debug("Activating group with id = {}", groupId);
        try {
            getById(groupId);
            jdbcTemplate.update(env.getProperty("group.activate"), groupId);
        } catch (DaoException e) {
            logger.error("Activating was not successful", e);
            throw new DaoException(String.format("Group with such id %d can not be activated", groupId), e);
        }
        logger.debug("Activating was successful", groupId);
    }
    
    public Group getGroupByLesson(Integer lessonId) {
        logger.debug("Getting Group By Lesson id = {}", lessonId);
        return jdbcTemplate
            .query(
                    env.getProperty("group.getRoomByLesson"),
                    new Object[] { lessonId },
                    new BeanPropertyRowMapper<>(Group.class)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any group", lessonId))
                        );
   }
    
    public Group getGroupByStudent(Integer studentId) {
        logger.debug("Getting Group By Student id = {}", studentId);
        return jdbcTemplate
            .query(
                    env.getProperty("group.getRoomByStudent"),
                    new Object[] { studentId },
                    new BeanPropertyRowMapper<>(Group.class)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Such student (id = %d) does not have any group", studentId))
                        );
   }
}
package ua.com.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dao.mapper.StudentMapper;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Component    
public class StudentDaoImpl  {

    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private Environment env;
    private GroupDao groupDaoImpl;

    @Autowired
    public StudentDaoImpl(JdbcTemplate jdbcTemplate, Environment env, GroupDao groupDaoImpl) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
        this.groupDaoImpl = groupDaoImpl;
    }
    
    public void create(Student student) {
        logger.debug("Creating student with name {} {}", student.getFirstName(), student.getLastName());
        try {
            Integer groupId = student.getGroup() == null ? null : student.getGroup().getGroupId();
            jdbcTemplate.update(
                    env.getProperty("student.create"),
                    student.getFirstName(), 
                    student.getLastName(),
                    groupId,
                    student.isStudentInactive()
                    );
        } catch (DataIntegrityViolationException e) {
            logger.error("Creating was not successful. Student can not be created. Some field is null", e);
            throw new DaoException("Student can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Student> getAll() {
        logger.debug("Getting all students");
        return jdbcTemplate.query(env.getProperty("student.getAll"), new StudentMapper(groupDaoImpl));
    }

    public Student getById(Integer studentId) {
        logger.debug("Getting student by id = {}", studentId);
        return jdbcTemplate
                .query(
                        env.getProperty("student.getById"),
                        new Object[]{studentId}, 
                        new StudentMapper(groupDaoImpl)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException(String.format("Student with such id %d does not exist", studentId)));
    }
    
    public void update(Student student) {
        logger.debug("Updating student with id {}", student.getStudentId());
        try {
            getById(student.getStudentId());
            Integer groupId = student.getGroup() == null ? null : student.getGroup().getGroupId();
            jdbcTemplate.update(
                    env.getProperty("student.update"),
                    groupId,
                    student.getFirstName(), 
                    student.getLastName(),
                    student.isStudentInactive(),
                    student.getStudentId()
                    );
        } catch (DaoException e) {
            logger.error("Updating was not successful. Student with such id = {} can not be updated", student.getStudentId(), e);
            throw new DaoException(String.format("Student with such id %d can not be updated", student.getStudentId()), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Updating was not successful. Student can not be updated. Some new field is null", e);
            throw new DaoException("Student can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }
    
    public void deactivate(Integer studentId) {
        logger.debug("Deactivating student with id = {}", studentId);
        try {
            getById(studentId);
            jdbcTemplate.update(env.getProperty("student.deactivate"), studentId);
        } catch (DaoException e) {
            logger.error("Deactivating was not successful", e);
            throw new DaoException(String.format("Student with such id %d can not be deactivated", studentId), e);
        }
        logger.debug("Deactivating was successful", studentId);
    }
    
    public void activate(Integer studentId) {
        logger.debug("Activating student with id = {}", studentId);
        try {
            getById(studentId);
            jdbcTemplate.update(env.getProperty("student.activate"), studentId);
        } catch (DaoException e) {
            logger.error("Activating was not successful", e);
            throw new DaoException(String.format("Student with such id %d can not be activated", studentId), e);
        }
        logger.debug("Activating was successful", studentId);
    }
    
    public void removeStudentFromGroup(Integer studentId) {
        logger.debug("Removing student id = {} from group", studentId);
        try {
            getById(studentId);
            jdbcTemplate.update(env.getProperty("student.removeStudentFromGroup"), studentId);
        } catch (DaoException e) {
            logger.error("Removing was not successful. Student does not exist", e);
            throw new DaoException(String.format("Student can not be removed from Group id = %d. Student does not exist", studentId), e);
        }
        logger.debug("Adding was successful");
    }

    public void addStudentToGroup(Integer groupId, Integer studentId) {
        logger.debug("Adding student id = {} to group id = {}", studentId, groupId);
        try {
            getById(studentId);
            groupDaoImpl.getById(groupId);
            jdbcTemplate.update(env.getProperty("student.addStudentToGroup"), groupId, studentId);
        } catch (DaoException e) {
            logger.error("Adding was not successful. Student or group does not exist", e);
            throw new DaoException(String.format("Student %d can not be added to group %d. Student or Group does not exist", studentId, groupId), e);
        }
        logger.debug("Adding was successful");
    }

    public List<Student> getStudentsByGroupId(Integer groupId) {
        logger.debug("Getting students with group id = {}",  groupId);
        List<Student> students = jdbcTemplate.query(env.getProperty("student.getStudentsByGroupId"), new StudentMapper(groupDaoImpl), groupId);
        logger.debug("Getting was successful");
        return students;
    }
}
package ua.com.foxminded.university.dao.implementation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Teacher;

@Component
public class TeacherDaoImpl implements TeacherDao{
    
    private static final Logger logger = LoggerFactory.getLogger(TeacherDaoImpl.class);
    private final static String PROPERTY_NAME = "src/main/resources/SqlQueries.properties";
    private final JdbcTemplate jdbcTemplate;
    private final PropertyReader propertyReader;

    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
    }
    
    public void create(Teacher teacher) {
        logger.debug("Creating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        try {
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.create"), 
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.isTeacherInactive()
                    );
        } catch (DataIntegrityViolationException e) {
            logger.error("Creating was not successful. Teacher can not be created. Some field is null", e);
            throw new DaoException("Teacher can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public void removeTeacherFromAllLessons(Integer teacherId) {
        logger.debug("removed Teacher with id = {} from all Lessons", teacherId);
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.removeTeacherFromAllLessons"), teacherId);
    }

    public List<Teacher> getAll() {
        logger.debug("Getting all Teachers");
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "teacher.getAll"),
                new BeanPropertyRowMapper<>(Teacher.class)
                );
    }

    public Teacher getById(Integer teacherId) {
        logger.debug("Getting teacher by id = {}", teacherId);
        return jdbcTemplate
                .query(
                        propertyReader.read(PROPERTY_NAME, "teacher.getById"), 
                        new Object[]{teacherId}, 
                        new BeanPropertyRowMapper<>(Teacher.class)
                        )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Teacher with such id %d does not exist", teacherId))
                        );
    }

    public void update(Teacher teacher) {
        logger.debug("Updating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        try {
            getById(teacher.getTeacherId());
            jdbcTemplate.update(
                    propertyReader.read(PROPERTY_NAME, "teacher.update"), 
                    teacher.getFirstName(), 
                    teacher.getLastName(),
                    teacher.isTeacherInactive(),
                    teacher.getTeacherId()
                    );
        } catch (DaoException e) {
            logger.error("Updating was not successful. Teacher with such id = {} can not be updated", teacher.getTeacherId(), e);
            throw new DaoException(String.format("Teacher with such id %d can not be updated", teacher.getTeacherId()), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Updating was not successful. Teacher can not be updated. Some new field is null", e);
            throw new DaoException("Teacher can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }
    
    public void deactivate(Integer teacherId) {
        logger.debug("Deactivating teacher with id = {}", teacherId);
        try {
            getById(teacherId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.deactivate"), teacherId);
        } catch (DaoException e) {
            logger.error("Deactivating was not successful", e);
            throw new DaoException(String.format("Teacher with such id %d can not be deactivated", teacherId), e);
        }
        logger.debug("Deactivating was successful", teacherId);
    }
    
    public void activate(Integer teacherId) {
        logger.debug("Activating teacher with id = {}", teacherId);
        try {
            getById(teacherId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.activate"), teacherId);
        } catch (DaoException e) {
            logger.error("Activating was not successful", e);
            throw new DaoException(String.format("Teacher with such id %d can not be activated", teacherId), e);
        }
        logger.debug("Activating was successful", teacherId);
    }
    
    public Teacher getTeacherByLesson(Integer lessonId) {
        logger.debug("Getting teacher By lesson id = {}", lessonId);
        return jdbcTemplate
            .query(
                    propertyReader.read(PROPERTY_NAME, "teacher.getTeacherByLesson"), 
                    new Object[] { lessonId },
                    new BeanPropertyRowMapper<>(Teacher.class)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any teacher", lessonId))
                        );
    }
}
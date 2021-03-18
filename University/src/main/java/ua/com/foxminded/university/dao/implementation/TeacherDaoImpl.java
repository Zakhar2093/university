package ua.com.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Component
public class TeacherDaoImpl implements TeacherDao{
    
    private static final Logger logger = LoggerFactory.getLogger(TeacherDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private Environment env;

    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate, Environment env) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
    }
    
    public void create(Teacher teacher) {
        logger.debug("Creating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        try {
            jdbcTemplate.update(env.getProperty("teacher.create"),
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
        jdbcTemplate.update(env.getProperty("teacher.removeTeacherFromAllLessons"), teacherId);
    }

    public List<Teacher> getAll() {
        logger.debug("Getting all Teachers");
        return jdbcTemplate.query(
                env.getProperty("teacher.getAll"),
                new BeanPropertyRowMapper<>(Teacher.class)
                );
    }

    public Teacher getById(Integer teacherId) {
        logger.debug("Getting teacher by id = {}", teacherId);
        return jdbcTemplate
                .query(
                        env.getProperty("teacher.getById"),
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
                    env.getProperty("teacher.update"),
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
            jdbcTemplate.update(env.getProperty("teacher.deactivate"), teacherId);
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
            jdbcTemplate.update(env.getProperty("teacher.activate"), teacherId);
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
                    env.getProperty("teacher.getTeacherByLesson"),
                    new Object[] { lessonId },
                    new BeanPropertyRowMapper<>(Teacher.class)
                )
                .stream()
                .findFirst()
                .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any teacher", lessonId))
                        );
    }
}
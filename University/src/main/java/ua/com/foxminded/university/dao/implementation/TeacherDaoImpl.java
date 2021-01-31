package ua.com.foxminded.university.dao.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

@Component
public class TeacherDaoImpl implements TeacherDao{
    
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
        try {
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.create"), 
                    teacher.getFirstName(),
                    teacher.getLastName(),
                    teacher.isTeacherInactive()
                    );
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Teacher can not be created. Some field is null"), e);
        }
    }

    public void removeTeacherFromAllLessons(Integer teacherId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.removeTeacherFromAllLessons"), teacherId);
    }

    public List<Teacher> getAll() {
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "teacher.getAll"),
                new BeanPropertyRowMapper<>(Teacher.class)
                );
    }

    public Teacher getById(Integer teacherId) {
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
            throw new DaoException(String.format("Teacher with such id %d can not be updated", teacher.getTeacherId()), e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Teacher can not be updated. Some new field is null"), e);
        }
    }
    
    public void deactivate(Integer teacherId) {
        try {
            getById(teacherId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.deactivate"), teacherId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Teacher with such id %d can not be deactivated", teacherId), e);
        }
    }
    
    public void activate(Integer teacherId) {
        try {
            getById(teacherId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.activate"), teacherId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Teacher with such id %d can not be activated", teacherId), e);
        }
    }
    
    public Teacher getTeacherByLesson(Integer lessonId) {
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
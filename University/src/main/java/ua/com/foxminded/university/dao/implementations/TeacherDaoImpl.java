package ua.com.foxminded.university.dao.implementations;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.models.Teacher;

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
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.create"), 
                teacher.getFirstName(),
                teacher.getLastName()
                );
    }

    public void delete(Integer teacherId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "teacher.delete"), teacherId);
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
                .findAny()
                .orElseThrow(() -> new DaoException("Teacher with such id does not exist"));
    }

    public void update(Teacher teacher) {
        jdbcTemplate.update(
                propertyReader.read(PROPERTY_NAME, "teacher.update"), 
                teacher.getFirstName(), 
                teacher.getLastName(), 
                teacher.getTeacherId()
                );
    }
}
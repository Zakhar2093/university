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
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.models.Teacher;

@Component
public class TeacherDaoImpl implements TeacherDao{
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TeacherDaoImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void create(Teacher teacher) {
        jdbcTemplate.update(readProperty("Teacher_create"), teacher.getFirstName(), teacher.getLastName());
    }

    public void delete(Integer teacherId) {
        jdbcTemplate.update(readProperty("Teacher_delete"), teacherId);
    }

    public List<Teacher> getAll() {
        return jdbcTemplate.query(readProperty("Teacher_getAll"), new BeanPropertyRowMapper<>(Teacher.class));
    }

    public Teacher getById(Integer teacherId) {
        return jdbcTemplate.query(readProperty("Teacher_getById"), new Object[]{teacherId}, new BeanPropertyRowMapper<>(Teacher.class))
                .stream().findAny().orElseThrow(() -> new DaoException("Teacher with such id does not exist"));
    }

    public void update(Teacher teacher) {
        jdbcTemplate.update(readProperty("Teacher_update"), teacher.getFirstName(), 
                                                            teacher.getLastName(), 
                                                            teacher.getTeacherId());
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
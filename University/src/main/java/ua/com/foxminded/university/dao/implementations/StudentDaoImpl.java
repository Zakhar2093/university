package ua.com.foxminded.university.dao.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.StudentMapper;
import ua.com.foxminded.university.models.Student;

@Component    
public class StudentDaoImpl implements StudentDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDaoImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void create(Student student) {
        jdbcTemplate.update(readProperty("Student_create"), student.getFirstName(), 
                                                            student.getLastName(), 
                                                            student.getGroup().getGroupId());
    }

    public void delete(Integer studentId) {
        jdbcTemplate.update(readProperty("Student_delete"), studentId);
    }

    public List<Student> getAll() {
        return jdbcTemplate.query(readProperty("Student_getAll"), new StudentMapper());
    }

    public Student getById(Integer studentId) {
        return jdbcTemplate.query(readProperty("Student_getById"), new Object[]{studentId}, new StudentMapper())
                .stream().findAny().orElseThrow(() -> new DaoException("Student with such id does not exist"));
    }

    public void update(Student student) {
        jdbcTemplate.update(readProperty("Student_update"), student.getGroup().getGroupId(),
                                                            student.getFirstName(), 
                                                            student.getLastName(), 
                                                            student.getStudentId());
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
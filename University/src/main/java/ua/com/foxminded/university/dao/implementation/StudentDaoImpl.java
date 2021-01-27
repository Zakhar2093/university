package ua.com.foxminded.university.dao.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dao.mapper.StudentMapper;
import ua.com.foxminded.university.model.Student;

@Component    
public class StudentDaoImpl implements StudentDao {

    private final static String PROPERTY_NAME = "src/main/resources/SqlQueries.properties";
    private final JdbcTemplate jdbcTemplate;
    private final PropertyReader propertyReader;
    private GroupDao groupDaoImpl;

    @Autowired
    public StudentDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader , GroupDao groupDaoImpl) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
        this.groupDaoImpl = groupDaoImpl;
    }
    
    public void create(Student student) {
        jdbcTemplate.update(
                propertyReader.read(PROPERTY_NAME, "student.create"), 
                student.getFirstName(), 
                student.getLastName(), 
                student.getGroup().getGroupId(),
                student.isStudentInactive()
                );
    }

    public List<Student> getAll() {
        return jdbcTemplate.query(propertyReader.read(PROPERTY_NAME, "student.getAll"), new StudentMapper(groupDaoImpl));
    }

    public Student getById(Integer studentId) {
        return jdbcTemplate
                .query(
                        propertyReader.read(PROPERTY_NAME, "student.getById"), 
                        new Object[]{studentId}, 
                        new StudentMapper(groupDaoImpl)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException("Student with such id does not exist"));
    }
    
    public void update(Student student) {
        jdbcTemplate.update(
                propertyReader.read(PROPERTY_NAME, "student.update"), 
                student.getGroup().getGroupId(),
                student.getFirstName(), 
                student.getLastName(),
                student.isStudentInactive(),
                student.getStudentId()
                );
    }
    
    public void deactivate(Integer studentId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.deactivate"), studentId);
    }
    
    public void activate(Integer studentId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.activate"), studentId);
    }
    
    public void removeStudentFromGroup(Integer studentId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.removeStudentFromGroup"), studentId);
    }
    
    public void addStudentToGroup(Integer groupId, Integer studentId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.addStudentToGroup"), groupId, studentId);
    } 
}
package ua.com.foxminded.university.dao.implementation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            jdbcTemplate.update(
                    propertyReader.read(PROPERTY_NAME, "student.create"), 
                    student.getFirstName(), 
                    student.getLastName(), 
                    student.getGroup().getGroupId(),
                    student.isStudentInactive()
                    );
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Student can not be created. Some field is null"), e);
        }
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
                .orElseThrow(() -> new DaoException(String.format("Student with such id %d does not exist", studentId)));
    }
    
    public void update(Student student) {
        try {
            getById(student.getStudentId());
            jdbcTemplate.update(
                    propertyReader.read(PROPERTY_NAME, "student.update"), 
                    student.getGroup().getGroupId(),
                    student.getFirstName(), 
                    student.getLastName(),
                    student.isStudentInactive(),
                    student.getStudentId()
                    );
        } catch (DaoException e) {
            throw new DaoException(String.format("Student with such id %d can not be updated", student.getStudentId()), e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Student can not be updated. Some new field is null"), e);
        }
    }
    
    public void deactivate(Integer studentId) {
        try {
            getById(studentId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.deactivate"), studentId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Student with such id %d can not be deactivated", studentId), e);
        }
    }
    
    public void activate(Integer studentId) {
        try {
            getById(studentId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.activate"), studentId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Student with such id %d can not be activated", studentId), e);
        }
    }
    
    public void removeStudentFromGroup(Integer studentId) {
        try {
            getById(studentId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.removeStudentFromGroup"), studentId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Student can not be removed from Group id = %d. Student does not exist", studentId), e);
        }
    }
    
    public void addStudentToGroup(Integer groupId, Integer studentId) {
        try {
            getById(studentId);
            groupDaoImpl.getById(groupId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "student.addStudentToGroup"), groupId, studentId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Student %d can not be added to group %d. Student or Group does not exist", studentId, groupId), e);
        }
    } 
}
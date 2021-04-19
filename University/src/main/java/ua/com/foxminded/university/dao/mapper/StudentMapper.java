package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.model.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StudentMapper implements RowMapper<Student> {
    
    private GroupDao groupDao;
    
    @Autowired
    public StudentMapper(GroupDao groupDao) {
        super();
        this.groupDao = groupDao;
    }

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {    
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setStudentInactive(rs.getBoolean("student_inactive"));
        
        if (rs.getInt("group_id") != 0) {
            student.setGroup(groupDao.getGroupByStudent(rs.getInt("student_id")));
        }
        return student;
    }
}
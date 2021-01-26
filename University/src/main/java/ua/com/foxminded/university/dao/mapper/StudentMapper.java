package ua.com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.implementation.GroupDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentMapper implements RowMapper<Student> {
    
//  @Autowired 
    private GroupDao groupDaoImpl;
    
    @Autowired
    public StudentMapper(GroupDao groupDaoImpl) {
        super();
        this.groupDaoImpl = groupDaoImpl;
    }
    
//    @Autowired
//    public void setGroupDaoImpl(GroupDaoImpl groupDaoImpl) {
//        this.groupDaoImpl = groupDaoImpl;
//    }

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {    
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGroup(groupDaoImpl.getById(rs.getInt("group_id")));
        return student;
    }
}
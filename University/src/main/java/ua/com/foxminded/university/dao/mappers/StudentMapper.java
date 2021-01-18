package ua.com.foxminded.university.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;
import ua.com.foxminded.university.models.Student;

@Component
public class StudentMapper implements RowMapper<Student> {

//    private final GroupDaoImpl groupDao;
//    
//    @Autowired
//    public StudentMapper(GroupDaoImpl groupDao) {
//        super();
//        this.groupDao = groupDao;
//    }

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        GroupDaoImpl groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        context.close();
        
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGroup(groupDao.getById(rs.getInt("group_id")));
        return student;
    }
    
}

package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Student;

import java.util.List;

public interface StudentDao extends GenericDao<Student, Integer>{
    void addStudentToGroup(Integer groupId, Integer studentId);
    
    void removeStudentFromGroup(Integer studentId);

    List<Student> getStudentsByGroupId(Integer groupId);
}

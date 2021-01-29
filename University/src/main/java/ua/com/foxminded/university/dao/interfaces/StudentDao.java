package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Student;

public interface StudentDao extends GenericDao<Student, Integer>{
    void addStudentToGroup(Integer groupId, Integer studentId);
    
    void removeStudentFromGroup(Integer studentId);
}

package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group, Integer> {
    
    void addStudentToGroup(Integer groupId, Integer studentId);
    
    void removeStudentFromGroup(Integer studentId);
    
    void removeGroupFromStudents(Integer groupId);
    
    void removeGroupFromLessons(Integer groupId);
}

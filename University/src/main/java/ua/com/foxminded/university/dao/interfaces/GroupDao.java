package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group, Integer> {
    
    void addStudentToGroup(Integer groupId, Integer studentId);
    
    void deleteStudentFromGroup(Integer studentId);
    
    void deleteGroupFromStudents(Integer groupId);
    
    void deleteGroupFromLessons(Integer groupId);
}

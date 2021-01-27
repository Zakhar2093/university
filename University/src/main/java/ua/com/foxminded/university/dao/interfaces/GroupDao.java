package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group, Integer> {
    
    void removeGroupFromAllStudents(Integer groupId);
    
    void removeGroupFromAllLessons(Integer groupId);
}

package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Group;

public interface GroupDao extends GenericDao<Group, Integer> {
    
    void removeGroupFromStudents(Integer groupId);
    
    void removeGroupFromLessons(Integer groupId);
}

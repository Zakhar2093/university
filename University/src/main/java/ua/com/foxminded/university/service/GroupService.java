package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.model.Group;

@Component
public class GroupService {
    
    private GroupDao groupDao;

    @Autowired
    public GroupService(GroupDao groupDao) {
        super();
        this.groupDao = groupDao;
    }
    
    public void create(Group group) {
        groupDao.create(group);
    }
    
    public List<Group> getAll(){
        return groupDao.getAll();
    }

    public Group getById(Integer groupId) {
        return groupDao.getById(groupId);
    }

    public void update(Group group) {
        groupDao.update(group);
    }
    
    @Transactional
    public void deactivate(Integer groupId) {
        groupDao.deleteGroupFromLessons(groupId);
        groupDao.deleteGroupFromStudents(groupId);
        groupDao.deactivate(groupId);
    }
    
    public void activate(Integer groupId) {
        groupDao.activate(groupId);
    }
    
    public void addStudentToGroup(Integer groupId, Integer studentId) {
        groupDao.addStudentToGroup(groupId, studentId);
    }
    
    public void deleteStudentFromGroup(Integer studentId) {
        groupDao.deleteStudentFromGroup(studentId);
    }
}

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
        groupDao.removeGroupFromAllLessons(groupId);
        groupDao.removeGroupFromAllStudents(groupId);
        groupDao.deactivate(groupId);
    }
    
    public void activate(Integer groupId) {
        groupDao.activate(groupId);
    }
    
    public void getGroupByLesson(Integer lessonId) {
        groupDao.getGroupByLesson(lessonId);
    }
    
    public void getGroupByStudent(Integer studentId) {
        groupDao.getGroupByStudent(studentId);
    }
}

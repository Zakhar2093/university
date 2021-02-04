package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
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
        try {
            groupDao.create(group);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Group> getAll() {
        try {
            return groupDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Group getById(Integer groupId) {
        try {
            return groupDao.getById(groupId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Group group) {
        try {
            groupDao.update(group);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public void deactivate(Integer groupId) {
        try {
            groupDao.removeGroupFromAllLessons(groupId);
            groupDao.removeGroupFromAllStudents(groupId);
            groupDao.deactivate(groupId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer groupId) {
        try {
            groupDao.activate(groupId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void getGroupByLesson(Integer lessonId) {
        try {
            groupDao.getGroupByLesson(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void getGroupByStudent(Integer studentId) {
        try {
            groupDao.getGroupByStudent(studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}

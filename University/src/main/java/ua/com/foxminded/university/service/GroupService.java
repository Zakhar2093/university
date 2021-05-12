package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;

import java.util.List;

@Component
public class GroupService implements GenericService<Group, Integer>{

    private GroupRepository groupDao;

    @Autowired
    public GroupService(GroupRepository groupDao) {
        super();
        this.groupDao = groupDao;
    }

    public void create(Group group) {
        try {
            groupDao.create(group);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Group> getAll() {
        try {
            return groupDao.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Group> getAllActivated() {
        try {
            List<Group> groups = groupDao.getAll();
            groups.removeIf(p -> (p.isGroupInactive()));
            return groups;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Group getById(Integer groupId) {
        try {
            return groupDao.getById(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Group group) {
        try {
            groupDao.update(group);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


    public void deactivate(Integer groupId) {
        try {
            groupDao.deactivate(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer groupId) {
        try {
            groupDao.activate(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}

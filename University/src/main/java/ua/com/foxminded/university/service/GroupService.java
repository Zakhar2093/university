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

    private GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        super();
        this.groupRepository = groupRepository;
    }

    public void create(Group group) {
        try {
            groupRepository.create(group);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Group> getAll() {
        try {
            return groupRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Group> getAllActivated() {
        try {
            List<Group> groups = groupRepository.getAll();
            groups.removeIf(p -> (p.isGroupInactive()));
            return groups;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Group getById(Integer groupId) {
        try {
            return groupRepository.getById(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Group group) {
        try {
            groupRepository.update(group);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }


    public void deactivate(Integer groupId) {
        try {
            groupRepository.deactivate(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer groupId) {
        try {
            groupRepository.activate(groupId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}

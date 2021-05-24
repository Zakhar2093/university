package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.repository.GroupRepository;

import java.util.List;

@Component
@Transactional
public class GroupService implements GenericService<Group, Integer>{

    private GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        super();
        this.groupRepository = groupRepository;
    }

    public void create(Group group) {
        groupRepository.save(group);
    }

    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    public List<Group> getAllActivated() {
        List<Group> groups = groupRepository.findAll();
        groups.removeIf(p -> (p.isGroupInactive()));
        return groups;
    }

    public Group getById(Integer groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Group with such id %d does not exist", groupId)));
    }

    public void update(Group group) {
        groupRepository.save(group);
    }

    public void deactivate(Integer groupId) {
        groupRepository.removeGroupFromAllLessons(groupId);
        groupRepository.removeGroupFromAllStudents(groupId);
        Group group = getById(groupId);
        group.setGroupInactive(true);
        groupRepository.save(group);
    }

    public void activate(Integer groupId) {
        Group group = getById(groupId);
        group.setGroupInactive(false);
        groupRepository.save(group);
    }
}
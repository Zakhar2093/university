package ua.com.foxminded.university.api.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/groups")
public class GroupRestController {

    private GroupService groupService;

    @Autowired
    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }


    @GetMapping
    public List<Group> findAll(){
        List<Group> groups = groupService.findAll();
        return groups;
    }

    @GetMapping("/{id}")
    public Group findById(@PathVariable("id") int id){
        return groupService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Group group) {
        groupService.save(group);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id, @RequestBody Group group) {
        group.setGroupId(id);
        groupService.save(group);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable("id") int id) {
        groupService.deactivate(id);
    }
}

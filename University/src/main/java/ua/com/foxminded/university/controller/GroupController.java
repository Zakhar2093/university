package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public String getAll(@ModelAttribute("group") Group group, Model model) {
        model.addAttribute("groups", groupService.getAllActivated());
        return "groups/index";
    }

    @PostMapping
    public String create(@ModelAttribute("group") Group group) {
        groupService.create(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.getById(id));
        return "groups/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") Group group, @PathVariable("id") int id) {
        group.setGroupId(id);
        groupService.update(group);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        groupService.deactivate(id);
        return "redirect:/groups";
    }
}
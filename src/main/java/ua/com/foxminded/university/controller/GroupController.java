package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;

import javax.validation.Valid;
import javax.validation.ValidationException;

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
        model.addAttribute("groups", groupService.findAll());
        return "groups/index";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("group") Group group, BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        groupService.save(group);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.findById(id));
        return "groups/update";
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("group") Group group, BindingResult result, @PathVariable("id") int id) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        group.setGroupId(id);
        groupService.save(group);
        return "redirect:/groups";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        groupService.deactivate(id);
        return "redirect:/groups";
    }
}
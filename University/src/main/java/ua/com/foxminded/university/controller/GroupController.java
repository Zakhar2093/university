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

    private LessonService lessonService;
    private RoomService roomService;
    private GroupService groupService;
    private TeacherService teacherService;
    private StudentService studentService;

    @Autowired
    public GroupController(LessonService lessonService, GroupService groupService, RoomService roomService, TeacherService teacherService, StudentService studentService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.roomService = roomService;
        this.teacherService = teacherService;
        this.studentService = studentService;
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

    @GetMapping("/{id}/lessons")
    public String showLessonsByGroup(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.getById(id));
        model.addAttribute("lessons", lessonService.getLessonsByGroupId(id));
        model.addAttribute("rooms", roomService.getAllActivated());
        model.addAttribute("teachers", teacherService.getAllActivated());
        return "lessons/index";
    }

    @GetMapping("/{id}/students")
    public String showStudentsInGroup(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.getById(id));
        model.addAttribute("students", studentService.getStudentsByGroupId(id));
        return "students/index";
    }
}
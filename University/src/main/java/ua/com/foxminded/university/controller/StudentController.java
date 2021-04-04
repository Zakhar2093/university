package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.controller.model_dto.StudentDto;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;
    private GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getAll(@ModelAttribute("student") Student student,
                         Model model) {
        List<Student> students = studentService.getAllActivated();

        List<Group> groups = groupService.getAll();
        groups.removeIf(p -> (p.isGroupInactive()));

        model.addAttribute("students", students);
        model.addAttribute("groups", groups);
        return "students/index";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("studentDto") StudentDto studentDto, Model model){
        List<Group> groups = groupService.getAll();
        groups.removeIf(p -> (p.isGroupInactive()));
        model.addAttribute("groups", groups);
        return "students/add";
    }

    @PostMapping
    public String submitCreate(@ModelAttribute("studentDto") StudentDto studentDto) {
        studentService.create(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) {
        List<Group> groups = groupService.getAll();
        groups.removeIf(p -> (p.isGroupInactive()));
        model.addAttribute("groups", groups);
        model.addAttribute("student", studentService.getDtoById(id));
        return "students/update";
    }

    @PatchMapping("/{id}")
    public String updateCreate(@ModelAttribute("studentDto") StudentDto studentDto, @PathVariable("id") int id) {
        studentDto.setStudentId(id);
        studentService.update(studentDto);
        return "redirect:/students";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        studentService.deactivate(id);
        return "redirect:/students";
    }
}
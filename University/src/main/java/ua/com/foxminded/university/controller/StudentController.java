package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

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
        model.addAttribute("students", studentService.getAllActivated());
        model.addAttribute("groups", groupService.getAllActivated());
        return "students/index";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("studentDto") StudentDto studentDto, Model model){
        model.addAttribute("groups", groupService.getAllActivated());
        return "students/add";
    }

    @PostMapping
    public String submitCreate(@ModelAttribute("studentDto") StudentDto studentDto) {
        studentService.create(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/{id}/update")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("groups", groupService.getAllActivated());
        model.addAttribute("studentDto", studentService.getDtoById(id));
        return "students/update";
    }

    @PatchMapping("/{id}")
    public String submitUpdate(@ModelAttribute("studentDto") StudentDto studentDto, @PathVariable("id") int id) {
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
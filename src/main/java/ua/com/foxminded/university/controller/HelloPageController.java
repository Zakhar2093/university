package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.com.foxminded.university.service.StudentService;
import ua.com.foxminded.university.service.TeacherService;

@Controller
public class HelloPageController {

    private TeacherService teacherService;
    private StudentService studentService;

    @Autowired
    public HelloPageController(TeacherService teacherService, StudentService studentService) {
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("students", studentService.findAll());
        return "homePage";
    }
}

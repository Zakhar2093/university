package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.com.foxminded.university.model.Lesson;
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
        model.addAttribute("teachers", teacherService.getAllActivated());
        model.addAttribute("students", studentService.getAllActivated());
        return "homePage";
    }
}

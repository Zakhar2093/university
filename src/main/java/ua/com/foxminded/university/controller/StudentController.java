package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Controller
public class StudentController {
    private StudentService studentService;
    private GroupService groupService;

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/students")
    public String getAll(Model model) {
        model.addAttribute("students", studentService.getAllActivated());
        model.addAttribute("groups", groupService.getAllActivated());
        return "students/index";
    }

    @GetMapping("/students/add")
    public String create(@ModelAttribute("studentDto") StudentDto studentDto, Model model){
        model.addAttribute("groups", groupService.getAllActivated());
        return "students/add";
    }

    @PostMapping("/students")
    public String submitCreate(@Valid @ModelAttribute("studentDto") StudentDto studentDto, BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        studentService.create(studentDto);
        return "redirect:/students";
    }

    @GetMapping("/students/{id}/update")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("groups", groupService.getAllActivated());
        model.addAttribute("studentDto", studentService.getDtoById(id));
        return "students/update";
    }

    @PatchMapping("/students/{id}")
    public String submitUpdate(@Valid @ModelAttribute("studentDto") StudentDto studentDto, BindingResult result, @PathVariable("id") int id) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        studentDto.setStudentId(id);
        studentService.update(studentDto);
        return "redirect:/students";
    }

    @DeleteMapping("/students/{id}")
    public String delete(@PathVariable("id") int id) {
        studentService.deactivate(id);
        return "redirect:/students";
    }

    @GetMapping("groups/{id}/students")
    public String showStudentsInGroup(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.getById(id));
        model.addAttribute("students", studentService.getStudentsByGroupId(id));
        return "students/index";
    }
}
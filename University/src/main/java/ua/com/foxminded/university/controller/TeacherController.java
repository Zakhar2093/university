package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.dao.implementation.hibernate.TeacherDaoHibernate;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private TeacherService teacherService;
    private TeacherDaoHibernate teacherDaoHibernate;

    @Autowired
    public TeacherController(TeacherService teacherService, TeacherDaoHibernate teacherDaoHibernate) {
        this.teacherService = teacherService;
        this.teacherDaoHibernate = teacherDaoHibernate;
    }

    @GetMapping
    public String getAll(@ModelAttribute("teacher") Teacher teacher, Model model) {
        System.out.println(teacherDaoHibernate.findById(1));
        model.addAttribute("teachers", teacherService.getAllActivated());
        return "teachers/index";
    }

    @PostMapping
    public String create(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.create(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("teacher", teacherService.getById(id));
        return "teachers/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher, @PathVariable("id") int id) {
        teacher.setTeacherId(id);
        teacherService.update(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        teacherService.deactivate(id);
        return "redirect:/teachers";
    }
}
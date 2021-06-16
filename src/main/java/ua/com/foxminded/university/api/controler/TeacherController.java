package ua.com.foxminded.university.api.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getAll(@ModelAttribute("teacher") Teacher teacher, Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers/index";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        teacherService.save(teacher);
        return "redirect:/teachers";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("teacher", teacherService.findById(id));
        return "teachers/update";
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("teacher") Teacher teacher, BindingResult result, @PathVariable("id") int id) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        teacher.setTeacherId(id);
        teacherService.save(teacher);
        return "redirect:/teachers";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        teacherService.deactivate(id);
        return "redirect:/teachers";
    }
}
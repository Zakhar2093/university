package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.service.LessonService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    private LessonService lessonService;

    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public String getAll(@ModelAttribute("lesson") Lesson lesson, Model model) {
        List<Lesson> lessons = lessonService.getAll();
        lessons.removeIf(p -> (p.isLessonInactive()));
        model.addAttribute("lessons", lessons);
        return "lessons/index";
    }

    @PostMapping
    public String create(@ModelAttribute("lesson") Lesson lesson) {
        //todo change .now
        lesson.setDate(LocalDateTime.now());
        lessonService.create(lesson);
        return "redirect:/lessons";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("lesson", lessonService.getById(id));
        return "lessons/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lesson") Lesson lesson, @PathVariable("id") int id) {
        lesson.setLessonId(id);
        //todo change .now
        lesson.setDate(LocalDateTime.now());
        lessonService.update(lesson);
        return "redirect:/lessons";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        lessonService.deactivate(id);
        return "redirect:/lessons";
    }
}
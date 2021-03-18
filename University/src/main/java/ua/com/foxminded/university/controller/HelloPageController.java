package ua.com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ua.com.foxminded.university.model.Lesson;

@Controller
public class HelloPageController {

    @GetMapping
    public String getAll(@ModelAttribute("lesson") Lesson lesson, Model model) {
        return "homePage";
    }
}

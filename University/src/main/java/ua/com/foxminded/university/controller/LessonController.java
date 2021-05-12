package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.TeacherService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    private static final String FORMAT = "dd MM yyyy hh:mm a";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private LessonService lessonService;
    private RoomService roomService;
    private GroupService groupService;
    private TeacherService teacherService;

    @Autowired
    public LessonController(LessonService lessonService, GroupService groupService, RoomService roomService, TeacherService teacherService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.roomService = roomService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("lessons", lessonService.getAllActivated());
        model.addAttribute("groups", groupService.getAllActivated());
        model.addAttribute("rooms", roomService.getAllActivated());
        model.addAttribute("teachers", teacherService.getAllActivated());
        return "lessons/index";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("lessonDto") LessonDto lessonDto, Model model){
        model.addAttribute("groups", groupService.getAllActivated());
        model.addAttribute("rooms", roomService.getAllActivated());
        model.addAttribute("teachers", teacherService.getAllActivated());
        return "lessons/add";
    }

    @PostMapping
    public String submitCreate(@ModelAttribute("lessonDto") LessonDto lessonDto) {
        lessonService.create(lessonDto);
        return "redirect:/lessons";
    }

    @GetMapping("/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("groups", groupService.getAllActivated());
        model.addAttribute("rooms", roomService.getAllActivated());
        model.addAttribute("teachers", teacherService.getAllActivated());
        model.addAttribute("lessonDto", lessonService.getDtoById(id));
        return "lessons/update";
    }

    @PatchMapping("/{id}")
    public String submitUpdate(@ModelAttribute("lessonDto") LessonDto lessonDto, @PathVariable("id") int id) {
        lessonDto.setLessonId(id);
        lessonService.update(lessonDto);
        return "redirect:/lessons";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        lessonService.deactivate(id);
        return "redirect:/lessons";
    }

    @GetMapping("/Schedule")
    public String getSchedule(Model model,
                              @RequestParam("entity") String entity,
                              @RequestParam("duration") String duration,
                              @RequestParam("id") int id,
                              @RequestParam("date") String date){
        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);
        List<Lesson> lessons = null;
        if (entity.equals("Student") && duration.equals("Day")){
            lessons = lessonService.getLessonByStudentIdForDay(id, localDateTime);
        } else if (entity.equals("Student") && duration.equals("Month")){
            lessons = lessonService.getLessonByStudentIdForMonth(id, localDateTime);
        } else if (entity.equals("Teacher") && duration.equals("Day")){
            lessons = lessonService.getLessonByTeacherIdForDay(id, localDateTime);
        } else if (entity.equals("Teacher") && duration.equals("Month")){
            lessons = lessonService.getLessonByTeacherIdForMonth(id, localDateTime);
        }
        model.addAttribute("lessons", lessons);
        return "lessons/index";
    }
}
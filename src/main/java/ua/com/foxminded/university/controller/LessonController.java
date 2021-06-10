package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.TeacherService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class LessonController {

    private static final String FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private LessonService lessonService;
    private RoomService roomService;
    private GroupService groupService;
    private TeacherService teacherService;

    @Autowired
    public LessonController(LessonService lessonService,
                            GroupService groupService,
                            RoomService roomService,
                            TeacherService teacherService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.roomService = roomService;
        this.teacherService = teacherService;
    }

    @GetMapping("/lessons")
    public String getAll(Model model) {
        model.addAttribute("lessons", lessonService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "lessons/index";
    }

    @GetMapping("/lessons/add")
    public String create(@ModelAttribute("lessonDto") LessonDto lessonDto, Model model){
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "lessons/add";
    }

    @PostMapping("/lessons")
    public String submitCreate(@Valid @ModelAttribute("lessonDto") LessonDto lessonDto, BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        lessonService.save(lessonDto);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/{id}/edit")
    public String update(Model model, @PathVariable("id") int id) {
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("lessonDto", lessonService.findDtoById(id));
        return "lessons/update";
    }

    @PatchMapping("/lessons/{id}")
    public String submitUpdate(@Valid @ModelAttribute("lessonDto") LessonDto lessonDto, @PathVariable("id") int id, BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        lessonDto.setLessonId(id);
        lessonService.save(lessonDto);
        return "redirect:/lessons";
    }

    @DeleteMapping("/lessons/{id}")
    public String delete(@PathVariable("id") int id) {
        lessonService.deactivate(id);
        return "redirect:/lessons";
    }

    @GetMapping("/lessons/Schedule")
    public String getSchedule(Model model,
                              @RequestParam("entity") String entity,
                              @RequestParam("duration") String duration,
                              @RequestParam("id") int id,
                              @RequestParam("date") String date){
        LocalDate localDate = LocalDate.parse(date, FORMATTER);
        List<Lesson> lessons = null;
        if (entity.equals("Student") && duration.equals("Day")){
            lessons = lessonService.getLessonByStudentIdForDay(id, localDate);
        } else if (entity.equals("Student") && duration.equals("Month")){
            lessons = lessonService.getLessonByStudentIdForMonth(id, localDate);
        } else if (entity.equals("Teacher") && duration.equals("Day")){
            lessons = lessonService.getLessonByTeacherIdForDay(id, localDate);
        } else if (entity.equals("Teacher") && duration.equals("Month")){
            lessons = lessonService.getLessonByTeacherIdForMonth(id, localDate);
        }
        model.addAttribute("lessons", lessons);
        return "lessons/index";
    }

    @GetMapping("teachers/{id}/lessons")
    public String showLessonsByTeacher(Model model, @PathVariable("id") int id) {
        model.addAttribute("teacher", teacherService.findById(id));
        model.addAttribute("lessons", lessonService.getLessonsByTeacherId(id));
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("groups", groupService.findAll());
        return "lessons/index";
    }

    @GetMapping("rooms/{id}/lessons")
    public String showLessonsByRoom(Model model, @PathVariable("id") int id) {
        model.addAttribute("room", roomService.findById(id));
        model.addAttribute("lessons", lessonService.getLessonsByRoomId(id));
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("groups", groupService.findAll());
        return "lessons/index";
    }

    @GetMapping("groups/{id}/lessons")
    public String showLessonsByGroup(Model model, @PathVariable("id") int id) {
        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("lessons", lessonService.getLessonsByGroupId(id));
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        return "lessons/index";
    }
}
package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.RoomService;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;
    private LessonService lessonService;
    private GroupService groupService;
    private TeacherService teacherService;

    @Autowired
    public RoomController(LessonService lessonService, GroupService groupService, RoomService roomService, TeacherService teacherService) {
        this.lessonService = lessonService;
        this.groupService = groupService;
        this.roomService = roomService;
        this.teacherService = teacherService;
    }
    @GetMapping
    public String getAll(@ModelAttribute("room") Room room, Model model) {
        model.addAttribute("rooms", roomService.getAllActivated());
        model.addAttribute("roomNumber", room.getRoomNumber());
        return "rooms/index";
    }

    @PostMapping
    public String create(@ModelAttribute("room") Room room) {
        roomService.create(room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("room", roomService.getById(id));
        return "rooms/update";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("room") Room room, @PathVariable("id") int id) {
        room.setRoomId(id);
        roomService.update(room);
        return "redirect:/rooms";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        roomService.deactivate(id);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/lessons")
    public String showLessonsByRoom(Model model, @PathVariable("id") int id) {
        model.addAttribute("room", roomService.getById(id));
        model.addAttribute("lessons", lessonService.getLessonsByRoomId(id));
        model.addAttribute("teachers", teacherService.getAllActivated());
        model.addAttribute("groups", groupService.getAllActivated());
        return "lessons/index";
    }
}
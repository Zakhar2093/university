package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {

    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getAll(Model model ) {
        roomService.create(new Room(1, 102));
        model.addAttribute("rooms", roomService.getAll());
        return "room/index";
    }
}

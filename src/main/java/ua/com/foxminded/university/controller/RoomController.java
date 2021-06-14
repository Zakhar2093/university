package ua.com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.RoomService;

import javax.validation.Valid;
import javax.validation.ValidationException;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    @GetMapping
    public String getAll(@ModelAttribute("room") Room room, Model model) {
        model.addAttribute("rooms", roomService.findAll());
        model.addAttribute("roomNumber", room.getRoomNumber());
        return "rooms/index";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("room") Room room, BindingResult result) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        roomService.save(room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("room", roomService.findById(id));
        return "rooms/update";
    }

    @PatchMapping("/{id}")
    public String update(@Valid @ModelAttribute("room") Room room, BindingResult result, @PathVariable("id") int id) {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().get(0).getDefaultMessage());
        }
        room.setRoomId(id);
        roomService.save(room);
        return "redirect:/rooms";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        roomService.deactivate(id);
        return "redirect:/rooms";
    }
}
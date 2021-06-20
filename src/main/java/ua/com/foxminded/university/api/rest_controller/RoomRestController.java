package ua.com.foxminded.university.api.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.service.RoomService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/rooms")
public class RoomRestController {

    private RoomService roomService;

    @Autowired
    public RoomRestController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> findAll(){
        List<Room> rooms = roomService.findAll();
        return rooms;
    }

    @GetMapping("/{id}")
    public Room findById(@PathVariable("id") int id){
        return roomService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Room room) {
        roomService.save(room);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id, @RequestBody Room room) {
        room.setRoomId(id);
        roomService.save(room);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable("id") int id) {
        roomService.deactivate(id);
    }
}
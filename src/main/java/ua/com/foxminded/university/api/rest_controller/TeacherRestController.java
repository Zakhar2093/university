package ua.com.foxminded.university.api.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/teachers")
public class TeacherRestController {

    private TeacherService roomService;

    @Autowired
    public TeacherRestController(TeacherService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Teacher> findAll(){
        List<Teacher> teacher = roomService.findAll();
        return teacher;
    }

    @GetMapping("/{id}")
    public Teacher findById(@PathVariable("id") int id){
        return roomService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Teacher room) {
        roomService.save(room);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") int id, @RequestBody Teacher room) {
        room.setTeacherId(id);
        roomService.save(room);
    }

    @DeleteMapping("/{id}")
    public void deactivate(@PathVariable("id") int id) {
        roomService.deactivate(id);
    }
}
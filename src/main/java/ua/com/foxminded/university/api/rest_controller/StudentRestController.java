package ua.com.foxminded.university.api.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.service.StudentService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class StudentRestController {
    private StudentService studentService;

    @Autowired
    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/students")
    public List<StudentDto> findAll(){
        List<StudentDto> students = studentService.findAllDto();
        return students;
    }

    @GetMapping("/students/{id}")
    public StudentDto findById(@PathVariable("id") int id){
        return studentService.findDtoById(id);
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody StudentDto studentDto) {
        studentService.save(studentDto);
    }

    @PutMapping("/students/{id}")
    public void update(@PathVariable("id") int id, @RequestBody StudentDto studentDto) {
        studentDto.setStudentId(id);
        studentService.save(studentDto);
    }

    @DeleteMapping("/students/{id}")
    public void deactivate(@PathVariable("id") int id) {
        studentService.deactivate(id);
    }

    @GetMapping("groups/{id}/students")
    public List<StudentDto> showStudentsDtoInGroup(@PathVariable("id") int groupId) {
        return studentService.getStudentsDtoByGroupId(groupId);
    }
}

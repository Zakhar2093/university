package ua.com.foxminded.university.api.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.model_dto.LessonDto;
import ua.com.foxminded.university.service.LessonService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class LessonRestController {

    private static final String FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    private LessonService lessonService;

    @Autowired
    public LessonRestController(LessonService lessonService) {
        this.lessonService = lessonService;
    }


    @GetMapping("/lessons")
    public List<LessonDto> findAll(){
        List<LessonDto> lessons = lessonService.findAllDto();
        return lessons;
    }

    @GetMapping("/lessons/{id}")
    public LessonDto findById(@PathVariable("id") int id){
        return lessonService.findDtoById(id);
    }

    @PostMapping("/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody LessonDto lessonDto) {
        lessonService.save(lessonDto);
    }

    @PutMapping("/lessons/{id}")
    public void update(@PathVariable("id") int id, @RequestBody LessonDto lessonDto) {
        lessonDto.setLessonId(id);
        lessonService.save(lessonDto);
    }

    @DeleteMapping("/lessons/{id}")
    public void deactivate(@PathVariable("id") int id) {
        lessonService.deactivate(id);
    }

    @GetMapping("groups/{id}/lessons")
    public List<LessonDto> showLessonDtoByGroup(@PathVariable("id") int groupId) {
        return lessonService.getLessonsDtoByGroupId(groupId);
    }

    @GetMapping("rooms/{id}/lessons")
    public List<LessonDto> showLessonsByRoom(@PathVariable("id") int roomId) {
        return lessonService.getLessonsDtoByRoomId(roomId);
    }

    @GetMapping("teachers/{id}/lessons")
    public List<LessonDto> showLessonsByTeacher(@PathVariable("id") int teacherId) {
        return lessonService.getLessonsDtoByTeacherId(teacherId);
    }

    @GetMapping("/lessons/Schedule")
    public List<LessonDto> getSchedule(@RequestParam("entity") String entity,
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
        return lessonService.mapListOfLessonsToListOfLessonsDto(lessons);
    }
}

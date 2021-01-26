package ua.com.foxminded.university.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

@Component
public class LessonService {

    private LessonDao lessonDao;
    
    @Autowired
    public LessonService(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }
    
    public void create(Lesson lesson) {
        lessonDao.create(lesson);
    }
    
    public List<Lesson> getAll(){
        return lessonDao.getAll();
    }

    public Lesson getById(Integer lessonId) {
        return lessonDao.getById(lessonId);
    }

    @Transactional
    public void delete(Integer lessonId) {
        lessonDao.deleteGroupFromLesson(lessonId);
        lessonDao.deleteRoomFromLesson(lessonId);
        lessonDao.deleteTeacherFromLesson(lessonId);
        lessonDao.delete(lessonId);
    }

    public void update(Lesson lesson) { 
        lessonDao.update(lesson);
    }
    
    @Transactional
    public void deactivate(Integer lessonId) {
        lessonDao.deleteGroupFromLesson(lessonId);
        lessonDao.deleteRoomFromLesson(lessonId);
        lessonDao.deleteTeacherFromLesson(lessonId);
        lessonDao.deactivate(lessonId);
    }
    
    public void activate(Integer lessonId) {
        lessonDao.activate(lessonId);
    }
    
    public void addGroupToLesson(Integer groupId, Integer lessonId) {
        lessonDao.addGroupToLesson(groupId, lessonId);
    }    
    
    public void deleteGroupFromLesson(Integer lessonId) {
        lessonDao.deleteGroupFromLesson(lessonId);
    }
    
    public void addRoomToLesson(Integer roomId, Integer lessonId) {
        lessonDao.addRoomToLesson(roomId, lessonId);
    }
    
    public void deleteRoomFromLesson(Integer lessonId) {
        lessonDao.deleteRoomFromLesson(lessonId);
    }
    
    public void addTeacherToLesson(Integer teacherId, Integer lessonId) {
        lessonDao.addTeacherToLesson(teacherId, lessonId);
    }
    
    public void deleteTeacherFromLesson(Integer lessonId) {
        lessonDao.deleteTeacherFromLesson(lessonId);
    }
    
    public List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date){
        return lessonDao.getLessonByTeacherForDay(teacher, date);
    }

    public List<Lesson> getLessonByTeacherForMonth(Teacher teacher, LocalDateTime date){
        return lessonDao.getLessonByTeacherForMonth(teacher, date);
    }

    public List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date){
        return lessonDao.getLessonByStudentForDay(student, date);
    }

    public List<Lesson> getLessonByStudentForMonth(Student student, LocalDateTime date){
        return lessonDao.getLessonByStudentForMonth(student, date);
    }
}
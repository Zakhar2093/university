package ua.com.foxminded.university.dao.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

public interface LessonDao extends GenericDao<Lesson, Integer> {
    List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date);

    List<Lesson> getLessonByTeacherForMonth(Teacher teacher, LocalDateTime date);

    List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date);

    List<Lesson> getLessonByStudentForMonth(Student student, LocalDateTime date);
    
    void addGroupToLesson(Integer groupId, Integer lessonId);
    
    void removeGroupFromLesson(Integer lessonId);
    
    void addRoomToLesson(Integer roomId, Integer lessonId);
    
    void removeRoomFromLesson(Integer lessonId);
    
    void addTeacherToLesson(Integer teacherId, Integer lessonId);
    
    void removeTeacherFromLesson(Integer lessonId);
    
}
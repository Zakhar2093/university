package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonDao extends GenericDao<Lesson, Integer> {
    List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date);

    List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date);

    List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date);

    List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date);
    
    void addGroupToLesson(Integer groupId, Integer lessonId);
    
    void removeGroupFromLesson(Integer lessonId);
    
    void addRoomToLesson(Integer roomId, Integer lessonId);
    
    void removeRoomFromLesson(Integer lessonId);
    
    void addTeacherToLesson(Integer teacherId, Integer lessonId);
    
    void removeTeacherFromLesson(Integer lessonId);
    
}
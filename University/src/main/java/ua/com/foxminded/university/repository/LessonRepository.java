package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.model.Lesson;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository extends GenericRepository<Lesson, Integer> {
    List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date);

    List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date);

    List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date);

    List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date);
    List<Lesson> getLessonsByGroupId(Integer groupId);

    List<Lesson> getLessonsByRoomId(Integer roomId);

    List<Lesson> getLessonsByTeacherId(Integer teacherId);
}
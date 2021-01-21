package ua.com.foxminded.university.dao.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Student;
import ua.com.foxminded.university.models.Teacher;

public interface LessonDao extends GenericDao<Lesson, Integer> {
    List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date);

    List<Lesson> getLessonByTeacherForMonth(Teacher teacher, LocalDateTime date);

    List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date);

    List<Lesson> getLessonByStudentForMonth(Student student, LocalDateTime date);
}
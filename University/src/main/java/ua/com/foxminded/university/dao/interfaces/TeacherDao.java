package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.model.Teacher;

public interface TeacherDao extends GenericDao<Teacher, Integer>{

    void removeTeacherFromAllLessons(Integer teacherId);
    
    Teacher getTeacherByLesson(Integer lessonId);
}

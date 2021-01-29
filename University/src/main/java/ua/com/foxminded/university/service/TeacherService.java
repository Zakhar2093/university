package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Teacher;

@Component
public class TeacherService {

    private TeacherDao teacherDao;
    
    @Autowired
    public TeacherService(TeacherDao teacherDao) {
        super();
        this.teacherDao = teacherDao;
    }
    
    public void create(Teacher teacher) {
        teacherDao.create(teacher);
    }
    
    public List<Teacher> getAll(){
        return teacherDao.getAll();
    }

    public Teacher getById(Integer teacherId) {
        return teacherDao.getById(teacherId);
    }

    public void update(Teacher teacher) {
        teacherDao.update(teacher);
    }
    
    @Transactional
    public void deactivate(Integer teacherId) {
        teacherDao.removeTeacherFromAllLessons(teacherId);
        teacherDao.deactivate(teacherId);
    }
    
    public void activate(Integer teacherId) {
        teacherDao.activate(teacherId);
    }
    
    public void getTeacherByLesson(Integer lessonId) {
        teacherDao.getTeacherByLesson(lessonId);
    }
}

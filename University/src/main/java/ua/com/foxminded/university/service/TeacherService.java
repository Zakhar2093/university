package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
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
        try {
            teacherDao.create(teacher);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Teacher> getAll() {
        try {
            return teacherDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Teacher getById(Integer teacherId) {
        try {
            return teacherDao.getById(teacherId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Teacher teacher) {
        try {
            teacherDao.update(teacher);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public void deactivate(Integer teacherId) {
        try {
            teacherDao.removeTeacherFromAllLessons(teacherId);
            teacherDao.deactivate(teacherId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer teacherId) {
        try {
            teacherDao.activate(teacherId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void getTeacherByLesson(Integer lessonId) {
        try {
            teacherDao.getTeacherByLesson(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
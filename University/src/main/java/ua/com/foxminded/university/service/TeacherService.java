package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Room;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Component
public class TeacherService implements GenericService<Teacher, Integer>{

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

    public List<Teacher> getAllActivated() {
        try {
            List<Teacher> teachers = teacherDao.getAll();
            teachers.removeIf(p -> (p.isTeacherInactive()));
            return teachers;
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

    public void deactivate(Integer teacherId) {
        try {
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
}
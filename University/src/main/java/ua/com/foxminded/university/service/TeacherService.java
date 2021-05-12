package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;

import java.util.List;

@Component
public class TeacherService implements GenericService<Teacher, Integer>{

    private TeacherRepository teacherDao;

    @Autowired
    public TeacherService(TeacherRepository teacherDao) {
        super();
        this.teacherDao = teacherDao;
    }

    public void create(Teacher teacher) {
        try {
            teacherDao.create(teacher);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Teacher> getAll() {
        try {
            return teacherDao.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Teacher> getAllActivated() {
        try {
            List<Teacher> teachers = teacherDao.getAll();
            teachers.removeIf(p -> (p.isTeacherInactive()));
            return teachers;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Teacher getById(Integer teacherId) {
        try {
            return teacherDao.getById(teacherId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Teacher teacher) {
        try {
            teacherDao.update(teacher);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer teacherId) {
        try {
            teacherDao.deactivate(teacherId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer teacherId) {
        try {
            teacherDao.activate(teacherId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
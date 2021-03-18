package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LessonService {

    private LessonDao lessonDao;

    @Autowired
    public LessonService(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    public void create(Lesson lesson) {
        try {
            lessonDao.create(lesson);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getAll() {
        try {
            return lessonDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Lesson getById(Integer lessonId) {
        try {
            return lessonDao.getById(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Lesson lesson) {
        try {
            lessonDao.update(lesson);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Transactional
    public void deactivate(Integer lessonId) {
        try {
            lessonDao.removeGroupFromLesson(lessonId);
            lessonDao.removeRoomFromLesson(lessonId);
            lessonDao.removeTeacherFromLesson(lessonId);
            lessonDao.deactivate(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer lessonId) {
        try {
            lessonDao.activate(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void addGroupToLesson(Integer groupId, Integer lessonId) {
        try {
            lessonDao.addGroupToLesson(groupId, lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void removeGroupFromLesson(Integer lessonId) {
        try {
            lessonDao.removeGroupFromLesson(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void addRoomToLesson(Integer roomId, Integer lessonId) {
        try {
            lessonDao.addRoomToLesson(roomId, lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void removeRoomFromLesson(Integer lessonId) {
        try {
            lessonDao.removeRoomFromLesson(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void addTeacherToLesson(Integer teacherId, Integer lessonId) {
        try {
            lessonDao.addTeacherToLesson(teacherId, lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void removeTeacherFromLesson(Integer lessonId) {
        try {
            lessonDao.removeTeacherFromLesson(lessonId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date) {
        try {
            return lessonDao.getLessonByTeacherForDay(teacher, date);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByTeacherForMonth(Teacher teacher, LocalDateTime date) {
        try {
            return lessonDao.getLessonByTeacherForMonth(teacher, date);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date) {
        try {
            return lessonDao.getLessonByStudentForDay(student, date);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public List<Lesson> getLessonByStudentForMonth(Student student, LocalDateTime date) {
        try {
            return lessonDao.getLessonByStudentForMonth(student, date);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
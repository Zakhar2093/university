package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Component
public class StudentService {
    
    private StudentDao studentDao;
    
    @Autowired
    public StudentService(StudentDao studentDao) {
        super();
        this.studentDao = studentDao;
    }
    
    public void create(Student student) {
        try {
            studentDao.create(student);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public List<Student> getAll(){
        try {
            return studentDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public Student getById(Integer studentId) {
        try {
            return studentDao.getById(studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Student student) {
        try {
            studentDao.update(student);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    @Transactional
    public void deactivate(Integer studentId) {
        try {
            studentDao.removeStudentFromGroup(studentId);
            studentDao.deactivate(studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public void activate(Integer studentId) {
        try {
            studentDao.activate(studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public void addStudentToGroup(Integer groupId, Integer studentId) {
        try {
            studentDao.addStudentToGroup(groupId, studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    public void removeStudentFromGroup(Integer studentId) {
        try {
            studentDao.removeStudentFromGroup(studentId);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}

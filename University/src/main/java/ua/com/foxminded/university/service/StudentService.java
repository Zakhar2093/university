package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentService {
    
    private StudentDao studentDao;
    
    @Autowired
    public StudentService(StudentDao studentDao) {
        super();
        this.studentDao = studentDao;
    }
    
    public void create(Student student) {
        studentDao.create(student);
    }
    
    public List<Student> getAll(){
        return studentDao.getAll();
    }

    public Student getById(Integer studentId) {
        return studentDao.getById(studentId);
    }

    public void update(Student student) {
        studentDao.update(student);
    }
    
    @Transactional
    public void deactivate(Integer studentId) {
        studentDao.removeStudentFromGroup(studentId);
        studentDao.deactivate(studentId);
    }
    
    public void activate(Integer studentId) {
        studentDao.activate(studentId);
    }
    
    public void addStudentToGroup(Integer groupId, Integer studentId) {
        studentDao.addStudentToGroup(groupId, studentId);
    }
    
    public void removeStudentFromGroup(Integer studentId) {
        studentDao.removeStudentFromGroup(studentId);
    }
}

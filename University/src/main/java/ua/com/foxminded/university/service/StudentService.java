package ua.com.foxminded.university.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.model.Student;

@Component
public class StudentService {
    
    private StudentDao studentDao;
    private GroupDao groupDao;
    
    @Autowired
    public StudentService(StudentDao studentDao, GroupDao groupDao) {
        super();
        this.studentDao = studentDao;
        this.groupDao = groupDao;
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

    public void delete(Integer studentId) {
        studentDao.delete(studentId);
    }

    public void update(Student student) {
        studentDao.update(student);
    }
    
    @Transactional
    public void deactivate(Integer studentId) {
        groupDao.deleteStudentFromGroup(studentId);
        studentDao.deactivate(studentId);
    }
    
    public void activate(Integer studentId) {
        studentDao.activate(studentId);
    }
}

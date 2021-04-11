package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Component
public class StudentService implements GenericService<Student, Integer>{
    
    private StudentDao studentDao;
    private GroupDao groupDao;
    
    @Autowired
    public StudentService(StudentDao studentDao, GroupDao groupDao) {
        super();
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }
    
    public void create(Student student) {
        try {
            studentDao.create(student);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public void create(StudentDto studentDto) {
        try {
            Student student = mapDtoToStudent(studentDto);
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

    public List<Student> getAllActivated(){
        try {
            List<Student> students = studentDao.getAll();
            students.removeIf(p -> (p.isStudentInactive()));
            return students;
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

    public StudentDto getDtoById(Integer studentId) {
        try {
            return mapStudentToDto(studentDao.getById(studentId));
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

    public void update(StudentDto studentDto) {
        try {
            Student student = mapDtoToStudent(studentDto);
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

    private Student mapDtoToStudent(StudentDto dto){
        Student student = new Student();
        student.setStudentId(dto.getStudentId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        Group group = groupDao.getById(dto.getGroupId());
        student.setGroup(group);
        student.setStudentInactive(dto.isStudentInactive());
        return student;
    }

    private StudentDto mapStudentToDto(Student student){
        StudentDto dto = new StudentDto();
        dto.setStudentId(student.getStudentId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setStudentInactive(student.isStudentInactive());
        if (student.getGroup() == null) {
            dto.setGroupId(null);
        } else {
            dto.setGroupId(student.getGroup().getGroupId());
        }
        return dto;
    }
}

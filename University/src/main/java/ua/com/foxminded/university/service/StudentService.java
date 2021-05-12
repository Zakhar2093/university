package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.model.model_dto.StudentDto;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Component
public class StudentService implements GenericService<Student, Integer>{
    
    private StudentRepository studentDao;
    private GroupRepository groupDao;
    
    @Autowired
    public StudentService(StudentRepository studentDao, GroupRepository groupDao) {
        super();
        this.studentDao = studentDao;
        this.groupDao = groupDao;
    }
    
    public void create(Student student) {
        try {
            studentDao.create(student);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void create(StudentDto studentDto) {
        try {
            Student student = mapDtoToStudent(studentDto);
            studentDao.create(student);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    
    public List<Student> getAll(){
        try {
            return studentDao.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Student> getAllActivated(){
        try {
            List<Student> students = studentDao.getAll();
            students.removeIf(p -> (p.isStudentInactive()));
            return students;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Student getById(Integer studentId) {
        try {
            return studentDao.getById(studentId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public StudentDto getDtoById(Integer studentId) {
        try {
            return mapStudentToDto(studentDao.getById(studentId));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Student student) {
        try {
            studentDao.update(student);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(StudentDto studentDto) {
        try {
            Student student = mapDtoToStudent(studentDto);
            studentDao.update(student);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer studentId) {
        try {
            studentDao.deactivate(studentId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    
    public void activate(Integer studentId) {
        try {
            studentDao.activate(studentId);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Student> getStudentsByGroupId(Integer groupId) {
        try {
            return studentDao.getStudentsByGroupId(groupId);
        } catch (RepositoryException e) {
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

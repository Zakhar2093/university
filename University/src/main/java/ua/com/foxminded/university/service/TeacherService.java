package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.util.List;

@Component
public class TeacherService implements GenericService<Teacher, Integer>{

    private TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        super();
        this.teacherRepository = teacherRepository;
    }

    public void create(Teacher teacher) {
        try {
            teacherRepository.save(teacher);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Teacher> getAll() {
        try {
            return teacherRepository.findAll();
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public List<Teacher> getAllActivated() {
        try {
            List<Teacher> teachers = teacherRepository.findAll();
            teachers.removeIf(p -> (p.isTeacherInactive()));
            return teachers;
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public Teacher getById(Integer teacherId) {
        try {
            return teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new ServiceException(
                            String.format("Teacher with such id %d does not exist", teacherId)
                    ));
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void update(Teacher teacher) {
        try {
            teacherRepository.save(teacher);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void deactivate(Integer teacherId) {
        try {
            Teacher teacher = getById(teacherId);
            teacherRepository.save(teacher);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

    public void activate(Integer teacherId) {
        try {
            Teacher teacher = getById(teacherId);
            teacher.setTeacherInactive(false);
            teacherRepository.save(teacher);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
}
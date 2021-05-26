package ua.com.foxminded.university.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.Teacher;
import ua.com.foxminded.university.repository.TeacherRepository;

import java.util.List;

@Component
@Transactional
public class TeacherService implements GenericService<Teacher, Integer>{

    private TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        super();
        this.teacherRepository = teacherRepository;
    }

    public void create(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    public List<Teacher> getAllActivated() {
        List<Teacher> teachers = teacherRepository.findAll();
        teachers.removeIf(p -> (p.isTeacherInactive()));
        return teachers;
    }

    public Teacher getById(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Teacher with such id %d does not exist", teacherId)));
    }

    public void update(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public void deactivate(Integer teacherId) {
        teacherRepository.removeTeacherFromAllLessons(teacherId);
        Teacher teacher = getById(teacherId);
        teacher.setTeacherInactive(true);
        teacherRepository.save(teacher);
    }

    public void activate(Integer teacherId) {
        Teacher teacher = getById(teacherId);
        teacher.setTeacherInactive(false);
        teacherRepository.save(teacher);
    }
}
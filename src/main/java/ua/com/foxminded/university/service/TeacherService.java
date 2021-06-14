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

    public void save(Teacher teacher) {
        teacherRepository.save(teacher);
    }

    public List<Teacher> findAll() {
        List<Teacher> teachers = teacherRepository.findAll();
        teachers.removeIf(p -> (p.isTeacherInactive()));
        return teachers;
    }

    public Teacher findById(Integer teacherId) {
        return teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Teacher with such id %d does not exist", teacherId)));
    }

    public void deactivate(Integer teacherId) {
        teacherRepository.removeTeacherFromAllLessons(teacherId);
        Teacher teacher = findById(teacherId);
        teacher.setTeacherInactive(true);
        teacherRepository.save(teacher);
    }

    public void activate(Integer teacherId) {
        Teacher teacher = findById(teacherId);
        teacher.setTeacherInactive(false);
        teacherRepository.save(teacher);
    }
}
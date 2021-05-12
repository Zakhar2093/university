package ua.com.foxminded.university.repository;

import ua.com.foxminded.university.model.Student;

import java.util.List;

public interface StudentRepository extends GenericRepository<Student, Integer> {
    List<Student> getStudentsByGroupId(Integer groupId);
}

package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.university.model.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByGroupGroupId(Integer groupId);
}

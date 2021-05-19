package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.model.Student;

import java.util.List;

@Transactional
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Modifying
    @Query("UPDATE Student S SET S.studentInactive = true, S.group = null WHERE S.studentId = :studentId")
    void deactivate(Integer studentId);

    List<Student> findByGroupGroupId(Integer groupId);
}

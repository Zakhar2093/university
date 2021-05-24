package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.com.foxminded.university.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Modifying
    @Query("UPDATE Lesson L SET L.teacher = null WHERE L.teacher.teacherId = :teacherId")
    void removeTeacherFromAllLessons(Integer teacherId);
}

package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    @Query("FROM Lesson L WHERE L.teacher.teacherId = :teacherId AND EXTRACT(YEAR FROM L.date) = :year AND EXTRACT(MONTH FROM L.date) = :month AND EXTRACT(DAY FROM L.date) = :day")
    List<Lesson> getLessonByTeacherIdForDay(@Param("teacherId") int teacherId,
                                            @Param("year") int year,
                                            @Param("month") int month,
                                            @Param("day") int day);

    @Query("FROM Lesson L WHERE L.teacher.teacherId = :teacherId AND EXTRACT(YEAR FROM L.date) = :year AND EXTRACT(MONTH FROM L.date) = :month")
    List<Lesson> getLessonByTeacherIdForMonth(@Param("teacherId") int teacherId,
                                              @Param("year") int year,
                                              @Param("month") int month);

    @Query("FROM Lesson L WHERE L.group = :group AND EXTRACT(YEAR FROM L.date) = :year AND EXTRACT(MONTH FROM L.date) = :month AND EXTRACT(DAY FROM L.date) = :day")
    List<Lesson> getLessonByGroupIdForDay(@Param("group") Group group,
                                          @Param("year") int year,
                                          @Param("month") int month,
                                          @Param("day") int day);

    @Query("FROM Lesson L WHERE L.group = :group AND EXTRACT(YEAR FROM L.date) = :year AND EXTRACT(MONTH FROM L.date) = :month")
    List<Lesson> getLessonByGroupIdForMonth(@Param("group") Group group,
                                            @Param("year") int year,
                                            @Param("month") int month);

    List<Lesson> findByGroupGroupId(Integer groupId);

    List<Lesson> findByRoomRoomId(Integer roomId);

    List<Lesson> findByTeacherTeacherId(Integer teacherId);
}
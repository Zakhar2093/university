package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.com.foxminded.university.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Modifying
    @Query("UPDATE Group SET groupInactive = true WHERE id = :groupId")
    void deactivate(Integer groupId);

    @Modifying
    @Query("UPDATE Lesson L SET L.group = null WHERE L.group.groupId = :groupId")
    void removeGroupFromAllLessons(Integer groupId);

    @Modifying
    @Query("UPDATE Student S SET S.group = null WHERE S.group.groupId = :groupId")
    void removeGroupFromAllStudents(Integer groupId);
}

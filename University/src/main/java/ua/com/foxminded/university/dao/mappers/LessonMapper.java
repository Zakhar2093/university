package ua.com.foxminded.university.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementations.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementations.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.models.Lesson;

@Component
public class LessonMapper implements RowMapper<Lesson>{
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;
    
    public LessonMapper(GroupDao groupDao, TeacherDao teacherDao, RoomDao roomDao) {
        super();
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.roomDao = roomDao;
    }

    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lesson lesson = new Lesson();
        lesson.setLessonId(rs.getInt("lesson_id"));
        lesson.setLessonName(rs.getString("lesson_name"));
        lesson.setTeacher(teacherDao.getById(rs.getInt("teacher_id")));
        lesson.setGroup(groupDao.getById(rs.getInt("group_id")));
        lesson.setRoom(roomDao.getById(rs.getInt("room_id")));
        lesson.setDate(rs.getDate("lesson_date"));
        return lesson;
    }
}
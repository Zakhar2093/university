package ua.com.foxminded.university.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Lesson;

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
        
        if (rs.wasNull()) {
            lesson.setTeacher(null);
        } else {
            lesson.setTeacher(teacherDao.getById(rs.getInt("teacher_id")));
        }
        
        lesson.setGroup(groupDao.getById(rs.getInt("group_id")));
        
        if (rs.wasNull()) {
            lesson.setRoom(null);
        } else {
            lesson.setRoom(roomDao.getById(rs.getInt("room_id")));
        }
        
        lesson.setDate(rs.getTimestamp("lesson_date").toLocalDateTime());
        lesson.setLessonInactive(rs.getBoolean("lesson_inactive"));
        return lesson;
    }
}
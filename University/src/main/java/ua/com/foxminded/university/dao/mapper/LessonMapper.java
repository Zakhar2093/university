package ua.com.foxminded.university.dao.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.model.Lesson;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LessonMapper implements RowMapper<Lesson>{

    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;

    @Autowired
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
        
        if (rs.getInt("group_id") != 0) {
            lesson.setGroup(groupDao.getGroupByLesson(rs.getInt("lesson_id")));
        }
        if (rs.getInt("teacher_id") != 0) {
            lesson.setTeacher(teacherDao.getTeacherByLesson(rs.getInt("lesson_id")));
        }
        if (rs.getInt("room_id") != 0) {
            lesson.setRoom(roomDao.getRoomByLesson(rs.getInt("lesson_id")));
        }
        if (rs.getTimestamp("lesson_date") != null) {
            lesson.setDate(rs.getTimestamp("lesson_date").toLocalDateTime());
        }
        lesson.setLessonInactive(rs.getBoolean("lesson_inactive"));
        return lesson;
    }
}
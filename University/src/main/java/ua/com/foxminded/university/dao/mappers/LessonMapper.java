package ua.com.foxminded.university.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.RowMapper;

import ua.com.foxminded.university.SpringConfig;
import ua.com.foxminded.university.dao.implementations.GroupDaoImpl;
import ua.com.foxminded.university.dao.implementations.RoomDaoImpl;
import ua.com.foxminded.university.dao.implementations.TeacherDaoImpl;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.models.Lesson;

public class LessonMapper implements RowMapper<Lesson>{
    public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        GroupDao groupDao = context.getBean("groupDaoImpl", GroupDaoImpl.class);
        TeacherDao teacherDao = context.getBean("teacherDaoImpl", TeacherDaoImpl.class);
        RoomDao roomDao = context.getBean("roomDaoImpl", RoomDaoImpl.class);
        context.close();
        
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

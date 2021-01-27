
package ua.com.foxminded.university.dao.implementation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

@Component
public class LessonDaoImpl implements LessonDao{

    private final static String PROPERTY_NAME = "src/main/resources/SqlQueries.properties";
    private final JdbcTemplate jdbcTemplate;
    private final PropertyReader propertyReader;
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;

    @Autowired 
    public LessonDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader, GroupDao groupDao,
            TeacherDao teacherDao, RoomDao roomDao) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.roomDao = roomDao;
    }

    public void create(Lesson lesson) {
        jdbcTemplate.update(
                propertyReader.read(PROPERTY_NAME, "lesson.create"), 
                lesson.getLessonName(), 
                lesson.getTeacher().getTeacherId(), 
                lesson.getGroup().getGroupId(),
                lesson.getRoom().getRoomId(),
                lesson.getDate(),           
                lesson.isLessonInactive()
                );
    }

    public List<Lesson> getAll() {
        return jdbcTemplate.query(propertyReader.read(PROPERTY_NAME, "lesson.getAll"), new LessonMapper(groupDao, teacherDao, roomDao));
    }

    public Lesson getById(Integer lessonId) {
        return jdbcTemplate
                .query(
                        propertyReader.read(PROPERTY_NAME, "lesson.getById"), 
                        new Object[] { lessonId },
                        new LessonMapper(groupDao, teacherDao, roomDao)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException("lesson with such id does not exist"));
    }

    public void update(Lesson lesson) {
        jdbcTemplate.update(
                propertyReader.read(PROPERTY_NAME, "lesson.update"), 
                lesson.getLessonName(), 
                lesson.getTeacher().getTeacherId(), 
                lesson.getGroup().getGroupId(),
                lesson.getRoom().getRoomId(),
                lesson.getDate(),
                lesson.isLessonInactive(),
                lesson.getLessonId()
                );
    }
    
    public List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date){
        int teacherId = teacher.getTeacherId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        int day = date.getDayOfMonth();
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByTeacherForDay"), 
                new Object[] { teacherId, year, mounth, day }, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    public List<Lesson> getLessonByTeacherForMonth(Teacher teacher, LocalDateTime date){
        int teacherId = teacher.getTeacherId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByTeacherForMonth"), 
                new Object[] { teacherId, year, mounth}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    public List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date){
        int studentId = student.getStudentId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        int day = date.getDayOfMonth();
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByStudentForDay"), 
                new Object[] { studentId, year, mounth, day}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    public List<Lesson> getLessonByStudentForMonth(Student student, LocalDateTime date){
        int studentId = student.getStudentId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByStudentForMonth"), 
                new Object[] { studentId, year, mounth}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    public void deactivate(Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.deactivate"), lessonId);
    }
    
    public void activate(Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.activate"), lessonId);
    }
    
    public void addGroupToLesson(Integer groupId, Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addGroupToLesson"), groupId, lessonId);
    }
    
    public void removeGroupFromLesson(Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeGroupFromLesson"), lessonId);
    }
    
    public void addRoomToLesson(Integer roomId, Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addRoomToLesson"), roomId, lessonId);
    }
    
    public void removeRoomFromLesson(Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeRoomFromLesson"), lessonId);
    }
    
    public void addTeacherToLesson(Integer teacherId, Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addTeacherToLesson"), teacherId, lessonId);
    }
    
    public void removeTeacherFromLesson(Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeTeacherFromLesson"), lessonId);
    }
}
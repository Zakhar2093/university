
package ua.com.foxminded.university.dao.implementation;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
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
    private StudentDao studentDao;

    @Autowired 
    public LessonDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader, GroupDao groupDao,
            TeacherDao teacherDao, RoomDao roomDao, StudentDao studentDao) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.roomDao = roomDao;
        this.studentDao = studentDao;
    }
    
    public void create(Lesson lesson) {
        try {
            jdbcTemplate.update(
                    propertyReader.read(PROPERTY_NAME, "lesson.create"), 
                    lesson.getLessonName(), 
                    lesson.getTeacher().getTeacherId(), 
                    lesson.getGroup().getGroupId(),
                    lesson.getRoom().getRoomId(),
                    lesson.getDate(),           
                    lesson.isLessonInactive()
                    );
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Lesson can not be created. Some field is null"), e);
        }
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
                .orElseThrow(() -> new DaoException(String.format("Lesson with such id %d does not exist", lessonId)));
    }

    public void update(Lesson lesson) {
        try {
            getById(lesson.getLessonId());
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
        } catch (DaoException e) {
            throw new DaoException(String.format("Lesson with such id %d can not be updated", lesson.getLessonId()), e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException(String.format("Lesson can not be updated. Some new field is null"), e);
        }
    }
    
    public List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date){
        checkIfTeacherExsist(teacher);
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
        checkIfTeacherExsist(teacher);
        int teacherId = teacher.getTeacherId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByTeacherForMonth"), 
                new Object[] { teacherId, year, mounth}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    private void checkIfTeacherExsist(Teacher teacher) {
        try {
            teacherDao.getById(teacher.getTeacherId());
        } catch (DaoException e) {
            throw new DaoException(String.format("Can not get lessons by Teacher id = %d. Teacher does not exist", teacher.getTeacherId()), e);
        }
    }
    
    public List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date){
        checkIfStudentExsist(student);
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
        checkIfStudentExsist(student);
        int studentId = student.getStudentId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByStudentForMonth"), 
                new Object[] { studentId, year, mounth}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    private void checkIfStudentExsist(Student student) {
        try {
            studentDao.getById(student.getStudentId());
        } catch (DaoException e) {
            throw new DaoException(String.format("Can not get lessons by Student id = %d. Student does not exist", student.getStudentId()), e);
        }
    }
    
    public void deactivate(Integer lessonId) {
        try {
            getById(lessonId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.deactivate"), lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Lesson with such id %d can not be deactivated", lessonId), e);
        }
    }
    
    public void activate(Integer lessonId) {
        try {
            getById(lessonId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.activate"), lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Lesson with such id %d can not be activated", lessonId), e);
        }
    }
    
    public void addGroupToLesson(Integer groupId, Integer lessonId) {
        try {
            getById(lessonId);
            groupDao.getById(groupId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addGroupToLesson"), groupId, lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Group %d can not be added to lesson id = %d. Group or lesson does not exist", lessonId, groupId), e);
        }
    }
    
    public void removeGroupFromLesson(Integer lessonId) {
        try {
            getById(lessonId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeGroupFromLesson"), lessonId); 
        } catch (DaoException e) {
            throw new DaoException(String.format("Group can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
    }
    
    public void addRoomToLesson(Integer roomId, Integer lessonId) {
        try {
            getById(lessonId);
            roomDao.getById(roomId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addRoomToLesson"), roomId, lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Room %d can not be added to lesson id = %d. Room or lesson does not exist", lessonId, roomId), e);
        }
    }
    
    public void removeRoomFromLesson(Integer lessonId) {
        try {
            getById(lessonId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeRoomFromLesson"), lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Room can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
    }
    
    public void addTeacherToLesson(Integer teacherId, Integer lessonId) {
        try {
            getById(lessonId);
            teacherDao.getById(teacherId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addTeacherToLesson"), teacherId, lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Teacher %d can not be added to lesson id = %d. Teacher or lesson does not exist", lessonId, teacherId), e);
        }
    }
    
    public void removeTeacherFromLesson(Integer lessonId) {
        try {
            getById(lessonId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeTeacherFromLesson"), lessonId);
        } catch (DaoException e) {
            throw new DaoException(String.format("Teacher can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
    }
}
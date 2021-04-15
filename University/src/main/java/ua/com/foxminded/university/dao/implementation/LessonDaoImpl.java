
package ua.com.foxminded.university.dao.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dao.interfaces.*;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.dao.mapper.StudentMapper;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LessonDaoImpl implements LessonDao{

    private static final Logger logger = LoggerFactory.getLogger(LessonDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private Environment env;
    
    private GroupDao groupDao;
    private TeacherDao teacherDao;
    private RoomDao roomDao;
    private StudentDao studentDao;

    @Autowired 
    public LessonDaoImpl(JdbcTemplate jdbcTemplate, Environment env, GroupDao groupDao,
            TeacherDao teacherDao, RoomDao roomDao, StudentDao studentDao) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
        this.groupDao = groupDao;
        this.teacherDao = teacherDao;
        this.roomDao = roomDao;
        this.studentDao = studentDao;
    }
    
    public void create(Lesson lesson) {
        logger.debug("Creating lesson with name {}", lesson.getLessonName());
        try {
            Integer groupId = lesson.getGroup() == null ? null : lesson.getGroup().getGroupId();
            Integer roomId = lesson.getRoom() == null ? null : lesson.getRoom().getRoomId();
            Integer teacherId = lesson.getTeacher() == null ? null : lesson.getTeacher().getTeacherId();
            jdbcTemplate.update(
                    env.getProperty("lesson.create"),
                    lesson.getLessonName(),
                    teacherId,
                    groupId,
                    roomId,
                    lesson.getDate(),           
                    lesson.isLessonInactive()
                    );
        } catch (DataIntegrityViolationException e) {
            logger.error("Creating was not successful. Lesson can not be created. Some field is null", e);
            throw new DaoException("Lesson can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Lesson> getAll() {
        logger.debug("Getting all lessons");
        return jdbcTemplate.query(env.getProperty("lesson.getAll"), new LessonMapper(groupDao, teacherDao, roomDao));
    }

    public Lesson getById(Integer lessonId) {
        logger.debug("Getting lesson by id = {}", lessonId);
        return jdbcTemplate
                .query(
                        env.getProperty("lesson.getById"),
                        new Object[] { lessonId },
                        new LessonMapper(groupDao, teacherDao, roomDao)
                        )
                .stream()
                .findAny()
                .orElseThrow(() -> new DaoException(String.format("Lesson with such id %d does not exist", lessonId)));
    }

    public void update(Lesson lesson) {
        logger.debug("Updating lesson with name {}", lesson.getLessonName());
        try {
            getById(lesson.getLessonId());
            Integer groupId = lesson.getGroup() == null ? null : lesson.getGroup().getGroupId();
            Integer roomId = lesson.getRoom() == null ? null : lesson.getRoom().getRoomId();
            Integer teacherId = lesson.getGroup() == null ? null : lesson.getTeacher().getTeacherId();
            jdbcTemplate.update(
                    env.getProperty("lesson.update"),
                    lesson.getLessonName(),
                    teacherId,
                    groupId,
                    roomId,
                    lesson.getDate(),
                    lesson.isLessonInactive(),
                    lesson.getLessonId()
                    );
        } catch (DaoException e) {
            logger.error("Updating was not successful. Lesson with such id {} can not be updated", lesson.getLessonId(), e);
            throw new DaoException(String.format("Lesson with such id %d can not be updated", lesson.getLessonId()), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Updating was not successful. Lesson can not be updated. Some new field is null", e);
            throw new DaoException("Lesson can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }
    
    public List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        String dateFormat = year + "." + month + "." + day;
        logger.debug("getting Lesson by Teacher id = {} for day = {}", teacherId, dateFormat);
        checkIfTeacherExist(teacherId);
        return jdbcTemplate.query(
                env.getProperty("lesson.getLessonByTeacherForDay"),
                new Object[] { teacherId, year, month, day },
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    public List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date){
        int year = date.getYear();
        int month = date.getMonthValue();
        String dateFormat = year + "." + month;
        logger.debug("getting Lesson by Teacher id = {} for day = {}", teacherId, dateFormat);
        checkIfTeacherExist(teacherId);
        return jdbcTemplate.query(
                env.getProperty("lesson.getLessonByTeacherForMonth"),
                new Object[] { teacherId, year, month},
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }

    private void checkIfTeacherExist(int teacherId) {
        logger.debug("checking if Teacher exist");
        try {
            teacherDao.getById(teacherId);
        } catch (DaoException e) {
            logger.error("Teacher id = {} does not exist", teacherId, e);
            throw new DaoException(String.format("Can not get lessons by Teacher id = %d. Teacher does not exist", teacherId), e);
        }
    }
    
    public List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date){
        int year = date.getYear();
        int mounth = date.getMonthValue();
        int day = date.getDayOfMonth();
        String dateFormat = year + "." + mounth + "." + day;
        logger.debug("getting Lesson by Student id = {} for day = {}", studentId, dateFormat);
        checkIfStudentExist(studentId);
        return jdbcTemplate.query(
                env.getProperty("lesson.getLessonByStudentForDay"),
                new Object[] { studentId, year, mounth, day}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    public List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date){
        int year = date.getYear();
        int month = date.getMonthValue();
        String dateFormat = year + "." + month;
        logger.debug("getting Lesson by Student id = {} for day = {}", studentId, dateFormat);
        checkIfStudentExist(studentId);
        return jdbcTemplate.query(
                env.getProperty("lesson.getLessonByStudentForMonth"),
                new Object[] { studentId, year, month},
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    private void checkIfStudentExist(int studentId) {
        logger.debug("checking if Student exist");
        try {
            studentDao.getById(studentId);
        } catch (DaoException e) {
            logger.error("Student id = {} does not exist", studentId, e);
            throw new DaoException(String.format("Can not get lessons by Student id = %d. Student does not exist", studentId), e);
        }
    }
    
    public void deactivate(Integer lessonId) {
        logger.debug("Deactivating lesson with id = {}", lessonId);
        try {
            getById(lessonId);
            jdbcTemplate.update(env.getProperty("lesson.deactivate"), lessonId);
        } catch (DaoException e) {
            logger.error("Deactivating was not successful", e);
            throw new DaoException(String.format("Lesson with such id %d can not be deactivated", lessonId), e);
        }
        logger.debug("Deactivating was successful", lessonId);
    }
    
    public void activate(Integer lessonId) {
        logger.debug("Activating lesson with id = {}", lessonId);
        try {
            getById(lessonId);
            jdbcTemplate.update(env.getProperty("lesson.activate"), lessonId);
        } catch (DaoException e) {
            logger.error("Activating was not successful", e);
            throw new DaoException(String.format("Lesson with such id %d can not be activated", lessonId), e);
        }
        logger.debug("Activating was successful", lessonId);
    }
    
    public void addGroupToLesson(Integer groupId, Integer lessonId) {
        logger.debug("Adding Group id = {} to Lesson id = {}", groupId, lessonId);
        try {
            getById(lessonId);
            groupDao.getById(groupId);
            jdbcTemplate.update(env.getProperty("lesson.addGroupToLesson"), groupId, lessonId);
        } catch (DaoException e) {
            logger.error("Adding was not successful. Group or lesson does not exist", e);
            throw new DaoException(String.format("Group %d can not be added to lesson id = %d. Group or lesson does not exist", lessonId, groupId), e);
        }
        logger.debug("Adding was successful");
    }
    
    public void removeGroupFromLesson(Integer lessonId) {
        logger.debug("Removing Group from Lesson id = {}", lessonId);
        try {
            getById(lessonId);
            jdbcTemplate.update(env.getProperty("lesson.removeGroupFromLesson"), lessonId);
        } catch (DaoException e) {
            logger.error("Removing was not successful. Lesson does not exist", e);
            throw new DaoException(String.format("Group can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
        logger.debug("Removing was successful");
    }
    
    public void addRoomToLesson(Integer roomId, Integer lessonId) {
        logger.debug("Adding Room id = {} to Lesson id = {}", roomId, lessonId);
        try {
            getById(lessonId);
            roomDao.getById(roomId);
            jdbcTemplate.update(env.getProperty("lesson.addRoomToLesson"), roomId, lessonId);
        } catch (DaoException e) {
            logger.error("Adding was not successful. Room or lesson does not exist", e);
            throw new DaoException(String.format("Room id = %d can not be added to lesson id = %d. Room or lesson does not exist", lessonId, roomId), e);
        }
        logger.debug("Adding was successful");
    }
    
    public void removeRoomFromLesson(Integer lessonId) {
        logger.debug("Removing Room from Lesson id = {}", lessonId);
        try {
            getById(lessonId);
            jdbcTemplate.update(env.getProperty("lesson.removeRoomFromLesson"), lessonId);
        } catch (DaoException e) {
            logger.error("Removing was not successful. Lesson does not exist", e);
            throw new DaoException(String.format("Room can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
    }
    
    public void addTeacherToLesson(Integer teacherId, Integer lessonId) {
        logger.debug("Adding Teacher id = {} to Lesson id = {}", teacherId, lessonId);
        try {
            getById(lessonId);
            teacherDao.getById(teacherId);
            jdbcTemplate.update(env.getProperty("lesson.addTeacherToLesson"), teacherId, lessonId);
        } catch (DaoException e) {
            logger.error("Adding was not successful. Teacher or lesson does not exist", e);
            throw new DaoException(String.format("Teacher %d can not be added to lesson id = %d. Teacher or lesson does not exist", lessonId, teacherId), e);
        }
        logger.debug("Adding was successful");
    }
    
    public void removeTeacherFromLesson(Integer lessonId) {
        logger.debug("Removing Teacher from Lesson id = {}", lessonId);
        try {
            getById(lessonId);
            jdbcTemplate.update(env.getProperty("lesson.removeTeacherFromLesson"), lessonId);
        } catch (DaoException e) {
            logger.error("Removing was not successful. Lesson does not exist", e);
            throw new DaoException(String.format("Teacher can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
        logger.debug("Adding was successful");
    }

    @Override
    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        logger.debug("Getting lessons with group id = {}",  groupId);
        List<Lesson> lessons = jdbcTemplate.query(env.getProperty("lesson.getLessonsByGroupId"), new LessonMapper(groupDao, teacherDao, roomDao), groupId);
        logger.debug("Getting was successful");
        return lessons;
    }

    @Override
    public List<Lesson> getLessonsByRoomId(Integer roomId) {
        logger.debug("Getting lessons with room id = {}",  roomId);
        List<Lesson> lessons = jdbcTemplate.query(env.getProperty("lesson.getLessonsByRoomId"), new LessonMapper(groupDao, teacherDao, roomDao), roomId);
        logger.debug("Getting was successful");
        return lessons;
    }

    @Override
    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        logger.debug("Getting lessons with teacher id = {}",  teacherId);
        List<Lesson> lessons = jdbcTemplate.query(env.getProperty("lesson.getLessonsByTeacherId"), new LessonMapper(groupDao, teacherDao, roomDao), teacherId);
        logger.debug("Getting was successful");
        return lessons;
    }
}
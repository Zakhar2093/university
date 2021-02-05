
package ua.com.foxminded.university.dao.implementation;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.dao.mapper.LessonMapper;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Lesson;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Teacher;

@Component
public class LessonDaoImpl implements LessonDao{

    private static final Logger logger = LoggerFactory.getLogger(LessonDaoImpl.class);
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
        logger.debug("Creating lesson with name {}", lesson.getLessonName());
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
            logger.error("Creating was not successful. Lesson can not be created. Some field is null", e);
            throw new DaoException("Lesson can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Lesson> getAll() {
        logger.debug("Getting all lessons");
        return jdbcTemplate.query(propertyReader.read(PROPERTY_NAME, "lesson.getAll"), new LessonMapper(groupDao, teacherDao, roomDao));
    }

    public Lesson getById(Integer lessonId) {
        logger.debug("Getting lesson by id = {}", lessonId);
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
        logger.debug("Updating lesson with name {}", lesson.getLessonName());
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
            logger.error("Updating was not successful. Lesson with such id {} can not be updated", lesson.getLessonId(), e);
            throw new DaoException(String.format("Lesson with such id %d can not be updated", lesson.getLessonId()), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Updating was not successful. Lesson can not be updated. Some new field is null", e);
            throw new DaoException("Lesson can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }
    
    public List<Lesson> getLessonByTeacherForDay(Teacher teacher, LocalDateTime date){
        int teacherId = teacher.getTeacherId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        int day = date.getDayOfMonth();
        String dateFormat = year + "." + mounth + "." + day;
        logger.debug("getting Lesson by Teacher id = {} for day = {}", teacher.getTeacherId(), dateFormat);
        checkIfTeacherExsist(teacher);
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
        String dateFormat = year + "." + mounth;
        logger.debug("getting Lesson by Teacher id = {} for day = {}", teacher.getTeacherId(), dateFormat);
        checkIfTeacherExsist(teacher);
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByTeacherForMonth"), 
                new Object[] { teacherId, year, mounth}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    private void checkIfTeacherExsist(Teacher teacher) {
        logger.debug("checking if Teacher exsist");
        try {
            teacherDao.getById(teacher.getTeacherId());
        } catch (DaoException e) {
            logger.error("Teacher id = {} does not exist", teacher.getTeacherId(), e);
            throw new DaoException(String.format("Can not get lessons by Teacher id = %d. Teacher does not exist", teacher.getTeacherId()), e);
        }
    }
    
    public List<Lesson> getLessonByStudentForDay(Student student, LocalDateTime date){
        int studentId = student.getStudentId();
        int year = date.getYear();
        int mounth = date.getMonthValue();
        int day = date.getDayOfMonth();
        String dateFormat = year + "." + mounth + "." + day;
        logger.debug("getting Lesson by Student id = {} for day = {}", student.getStudentId(), dateFormat);
        checkIfStudentExsist(student);
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
        String dateFormat = year + "." + mounth;
        logger.debug("getting Lesson by Student id = {} for day = {}", student.getStudentId(), dateFormat);
        checkIfStudentExsist(student);
        return jdbcTemplate.query(
                propertyReader.read(PROPERTY_NAME, "lesson.getLessonByStudentForMonth"), 
                new Object[] { studentId, year, mounth}, 
                new LessonMapper(groupDao, teacherDao, roomDao)
                );
    }
    
    private void checkIfStudentExsist(Student student) {
        logger.debug("checking if Student exsist");
        try {
            studentDao.getById(student.getStudentId());
        } catch (DaoException e) {
            logger.error("Student id = {} does not exist", student.getStudentId(), e);
            throw new DaoException(String.format("Can not get lessons by Student id = %d. Student does not exist", student.getStudentId()), e);
        }
    }
    
    public void deactivate(Integer lessonId) {
        logger.debug("Deactivating lesson with id = {}", lessonId);
        try {
            getById(lessonId);
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.deactivate"), lessonId);
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.activate"), lessonId);
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addGroupToLesson"), groupId, lessonId);
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeGroupFromLesson"), lessonId); 
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addRoomToLesson"), roomId, lessonId);
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeRoomFromLesson"), lessonId);
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.addTeacherToLesson"), teacherId, lessonId);
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
            jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.removeTeacherFromLesson"), lessonId);
        } catch (DaoException e) {
            logger.error("Removing was not successful. Lesson does not exist", e);
            throw new DaoException(String.format("Teacher can not be removed from lesson id = %d. Lesson does not exist", lessonId), e);
        }
        logger.debug("Adding was successful");
    }
}
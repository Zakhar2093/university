package ua.com.foxminded.university.dao.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.PropertyReader;
import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.mappers.LessonMapper;
import ua.com.foxminded.university.models.Lesson;

@Component
public class LessonDaoImpl implements LessonDao{

    private final static String PROPERTY_NAME = "src/main/resources/SqlQueries.properties";
    private final JdbcTemplate jdbcTemplate;
    private final PropertyReader propertyReader;

    @Autowired
    public LessonDaoImpl(JdbcTemplate jdbcTemplate, PropertyReader propertyReader) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.propertyReader = propertyReader;
    }

    
    public void create(Lesson lesson) {
        jdbcTemplate.update(
                propertyReader.read(PROPERTY_NAME, "lesson.create"), 
                lesson.getLessonName(), 
                lesson.getTeacher().getTeacherId(), 
                lesson.getGroup().getGroupId(),
                lesson.getRoom().getRoomId(),
                lesson.getDate()
                );
    }


    public void delete(Integer lessonId) {
        jdbcTemplate.update(propertyReader.read(PROPERTY_NAME, "lesson.delete"), lessonId);
    }

    public List<Lesson> getAll() {
        return jdbcTemplate.query(propertyReader.read(PROPERTY_NAME, "lesson.getAll"), new LessonMapper());
    }

    public Lesson getById(Integer lessonId) {
        return jdbcTemplate
                .query(
                        propertyReader.read(PROPERTY_NAME, "lesson.getById"), 
                        new Object[] { lessonId },
                        new LessonMapper()
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
                lesson.getLessonId()
                );
    }
}
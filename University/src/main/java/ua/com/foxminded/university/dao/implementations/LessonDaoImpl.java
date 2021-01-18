package ua.com.foxminded.university.dao.implementations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.university.dao.DaoException;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.mappers.LessonMapper;
import ua.com.foxminded.university.models.Lesson;
import ua.com.foxminded.university.models.Lesson;

@Component
public class LessonDaoImpl implements LessonDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LessonDaoImpl(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public void create(Lesson lesson) {
        jdbcTemplate.update(readProperty("Lesson_create"), lesson.getLessonName(), 
                                                            lesson.getTeacher().getTeacherId(), 
                                                            lesson.getGroup().getGroupId(),
                                                            lesson.getRoom().getRoomId(),
                                                            lesson.getDate());
    }

    public void delete(Integer lessonId) {
        jdbcTemplate.update(readProperty("Lesson_delete"), lessonId);
    }

    public List<Lesson> getAll() {
        return jdbcTemplate.query(readProperty("Lesson_getAll"), new LessonMapper());
    }

    public Lesson getById(Integer lessonId) {
        return jdbcTemplate.query(readProperty("Lesson_getById"), new Object[]{lessonId}, new LessonMapper())
                .stream().findAny().orElseThrow(() -> new DaoException("Lesson with such id does not exist"));
    }

    public void update(Lesson lesson) {
        jdbcTemplate.update(readProperty("Lesson_update"), lesson.getLessonName(), 
                                                            lesson.getTeacher().getTeacherId(), 
                                                            lesson.getGroup().getGroupId(),
                                                            lesson.getRoom().getRoomId(),
                                                            lesson.getDate(),
                                                            lesson.getLessonId());
    }

    private String readProperty(String key) {
        Properties property = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/SqlQueries.properties");
            property.load(fis);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property.getProperty(key);
    }
}
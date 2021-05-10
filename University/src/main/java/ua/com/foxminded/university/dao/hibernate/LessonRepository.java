package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Lesson;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class LessonRepository implements LessonDao{

    private static final Logger logger = LoggerFactory.getLogger(LessonRepository.class);
    private SessionFactory sessionFactory;
    private TeacherDao teacherDao;
    private StudentDao studentDao;

    @Autowired
    public LessonRepository(SessionFactory sessionFactory, TeacherDao teacherDao, StudentDao studentDao) {
        this.sessionFactory = sessionFactory;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
    }

    public void create(Lesson lesson) {
        logger.debug("Creating lesson with name {} {}", lesson.getLessonName());
        try (Session session = sessionFactory.openSession()){
            session.save(lesson);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Lesson can not be created. Some field is null", e);
            throw new DaoException("Lesson can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Lesson> getAll() {
        logger.debug("Getting all Lessons");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Lesson");
        return query.getResultList();
    }

    public Lesson getById(Integer lessonId) {
        logger.debug("Getting lesson by id = {}", lessonId);
        Session session = sessionFactory.openSession();
        Lesson lesson = Optional.ofNullable(session.get(Lesson.class, lessonId))
                .orElseThrow(() -> new DaoException(String.format("Lesson with such id %d does not exist", lessonId)))
                ;
        return lesson;
    }

    public void update(Lesson lesson) {
        logger.debug("Updating lesson with name {} {}", lesson.getLessonName());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            session.merge(lesson);

            tx.commit();
        } catch (PersistenceException e) {
            logger.error("Updating was not successful. Lesson can not be updated. Some new field is null", e);
            throw new DaoException("Lesson can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer lessonId) {
        logger.debug("Deactivating lesson with id = {}", lessonId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Lesson L SET L.lessonInactive = true, L.group = null, L.room = null, L.teacher = null WHERE L.lessonId =: lessonId");
        query.setParameter("lessonId", lessonId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Deactivating was successful", lessonId);
    }

    public void activate(Integer lessonId) {
        logger.debug("Activating lesson with id = {}", lessonId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Lesson SET lessonInactive = false WHERE id =: lessonId");
        query.setParameter("lessonId", lessonId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", lessonId);
    }


    @Override
    public List<Lesson> getLessonByTeacherIdForDay(int teacherId, LocalDateTime date) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("FROM Lesson L WHERE L.teacher =: teacher AND EXTRACT(YEAR FROM L.date) =: year AND EXTRACT(MONTH FROM L.date) =: month AND EXTRACT(DAY FROM L.date) =: day");
        query.setParameter("teacher", teacherDao.getById(teacherId));
        query.setParameter("year", date.getYear());
        query.setParameter("month", date.getMonthValue());
        query.setParameter("day", date.getDayOfMonth());
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        return lessons;
    }

    @Override
    public List<Lesson> getLessonByTeacherIdForMonth(int teacherId, LocalDateTime date) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("FROM Lesson L WHERE L.teacher =: teacher AND EXTRACT(YEAR FROM L.date) =: year AND EXTRACT(MONTH FROM L.date) =: month");
        query.setParameter("teacher", teacherDao.getById(teacherId));
        query.setParameter("year", date.getYear());
        query.setParameter("month", date.getMonthValue());
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        return lessons;
    }

    @Override
    public List<Lesson> getLessonByStudentIdForDay(int studentId, LocalDateTime date) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("FROM Lesson L WHERE L.group =: group AND EXTRACT(YEAR FROM L.date) =: year AND EXTRACT(MONTH FROM L.date) =: month AND EXTRACT(DAY FROM L.date) =: day");
        query.setParameter("group", studentDao.getById(studentId).getGroup());
        query.setParameter("year", date.getYear());
        query.setParameter("month", date.getMonthValue());
        query.setParameter("day", date.getDayOfMonth());
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        return lessons;
    }

    @Override
    public List<Lesson> getLessonByStudentIdForMonth(int studentId, LocalDateTime date) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("FROM Lesson L WHERE L.group =: group AND EXTRACT(YEAR FROM L.date) =: year AND EXTRACT(MONTH FROM L.date) =: month");
        query.setParameter("group", studentDao.getById(studentId).getGroup());
        query.setParameter("year", date.getYear());
        query.setParameter("month", date.getMonthValue());
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        return lessons;
    }
}
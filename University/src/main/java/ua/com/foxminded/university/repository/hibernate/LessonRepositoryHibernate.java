package ua.com.foxminded.university.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Lesson;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class LessonRepositoryHibernate implements ua.com.foxminded.university.repository.LessonRepository {

    private static final Logger logger = LoggerFactory.getLogger(LessonRepositoryHibernate.class);
    private SessionFactory sessionFactory;
    private StudentRepository studentDao;
    private Environment env;

    @Autowired
    public LessonRepositoryHibernate(SessionFactory sessionFactory, StudentRepository studentDao, Environment env) {
        this.sessionFactory = sessionFactory;
        this.studentDao = studentDao;
        this.env = env;
    }

    public void create(Lesson lesson) {
        logger.debug("Creating lesson with name {} {}", lesson.getLessonName());
        try (Session session = sessionFactory.openSession()){
            session.save(lesson);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Lesson can not be created. Some field is null", e);
            throw new RepositoryException("Lesson can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Lesson> getAll() {
        logger.debug("Getting all Lessons");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(env.getProperty("lesson.getAll"));
        return query.getResultList();
    }

    public Lesson getById(Integer lessonId) {
        logger.debug("Getting lesson by id = {}", lessonId);
        Session session = sessionFactory.openSession();
        Lesson lesson = Optional.ofNullable(session.get(Lesson.class, lessonId))
                .orElseThrow(() -> new RepositoryException(String.format("Lesson with such id %d does not exist", lessonId)))
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
            throw new RepositoryException("Lesson can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer lessonId) {
        logger.debug("Deactivating lesson with id = {}", lessonId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("lesson.deactivate"));
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

        Query query = session.createQuery(env.getProperty("lesson.activate"));
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

        Query query = session.createQuery(env.getProperty("lesson.getLessonByTeacherForDay"));
        query.setParameter("teacherId", teacherId);
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

        Query query = session.createQuery(env.getProperty("lesson.getLessonByTeacherForMonth"));
        query.setParameter("teacherId", teacherId);
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

        Query query = session.createQuery(env.getProperty("lesson.getLessonByStudentForDay"));
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

        Query query = session.createQuery(env.getProperty("lesson.getLessonByStudentForMonth"));
        query.setParameter("group", studentDao.getById(studentId).getGroup());
        query.setParameter("year", date.getYear());
        query.setParameter("month", date.getMonthValue());
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        return lessons;
    }

    @Override
    public List<Lesson> getLessonsByGroupId(Integer groupId) {
        logger.debug("Getting lessons from group id = {}",  groupId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("lesson.getLessonsByGroupId"));
        query.setParameter("groupId", groupId);
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        logger.debug("Getting was successful");
        return lessons;
    }

    @Override
    public List<Lesson> getLessonsByRoomId(Integer roomId) {
        logger.debug("Getting lessons from room id = {}",  roomId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("lesson.getLessonsByRoomId"));
        query.setParameter("roomId", roomId);
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        logger.debug("Getting was successful");
        return lessons;
    }

    @Override
    public List<Lesson> getLessonsByTeacherId(Integer teacherId) {
        logger.debug("Getting lessons from teacher id = {}",  teacherId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("lesson.getLessonsByTeacherId"));
        query.setParameter("teacherId", teacherId);
        List<Lesson> lessons = query.getResultList();

        tx.commit();
        session.close();
        logger.debug("Getting was successful");
        return lessons;
    }
}
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
import ua.com.foxminded.university.exception.RepositoryException;
import ua.com.foxminded.university.model.Teacher;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class TeacherRepositoryHibernate implements ua.com.foxminded.university.repository.TeacherRepository {

    private static final Logger logger = LoggerFactory.getLogger(TeacherRepositoryHibernate.class);
    private SessionFactory sessionFactory;
    private Environment env;

    @Autowired
    public TeacherRepositoryHibernate(SessionFactory sessionFactory, Environment env) {
        this.sessionFactory = sessionFactory;
        this.env = env;
    }

    public void create(Teacher teacher) {
        logger.debug("Creating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        try (Session session = sessionFactory.openSession()){
            session.save(teacher);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Teacher can not be created. Some field is null", e);
            throw new RepositoryException("Teacher can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Teacher> getAll() {
        logger.debug("Getting all Teachers");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(env.getProperty("teacher.getAll"));
        List<Teacher> teachers = query.getResultList();
        session.close();
        return teachers;
    }

    public Teacher getById(Integer teacherId) {
        logger.debug("Getting teacher by id = {}", teacherId);
        Session session = sessionFactory.openSession();
        Teacher teacher = Optional.ofNullable(session.get(Teacher.class, teacherId))
                .orElseThrow(() -> new RepositoryException(String.format("Teacher with such id %d does not exist", teacherId)))
                ;
        session.close();
        return teacher;
    }

    public void update(Teacher teacher) {
        logger.debug("Updating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            session.merge(teacher);

            tx.commit();
        } catch (PersistenceException e) {
            logger.error("Updating was not successful. Teacher can not be updated. Some new field is null", e);
            throw new RepositoryException("Teacher can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer teacherId) {
        logger.debug("Deactivating teacher with id = {}", teacherId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query deactivateTeacher = session.createQuery(env.getProperty("teacher.deactivate"));
        deactivateTeacher.setParameter("teacherId", teacherId);
        deactivateTeacher.executeUpdate();

        Query deleteTeacherFromLesson = session.createQuery(env.getProperty("teacher.removeTeacherFromAllLessons"));
        Teacher teacherG = new Teacher();
        teacherG.setTeacherId(teacherId);
        deleteTeacherFromLesson.setParameter("teacherId", teacherG);
        deleteTeacherFromLesson.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Deactivating was successful", teacherId);
    }

    public void activate(Integer teacherId) {
        logger.debug("Activating teacher with id = {}", teacherId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("teacher.activate"));
        query.setParameter("teacherId", teacherId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", teacherId);
    }
}
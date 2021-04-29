package ua.com.foxminded.university.dao.implementation.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.TeacherDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Teacher;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class TeacherDaoHibernate implements TeacherDao{

    private static final Logger logger = LoggerFactory.getLogger(TeacherDaoHibernate.class);
    private SessionFactory sessionFactory;

    @Autowired
    public TeacherDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Teacher teacher) {
        logger.debug("Creating teacher with name {} {}", teacher.getFirstName(), teacher.getLastName());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();
            session.save(teacher);
            tx.commit();
        } catch (ConstraintViolationException e) {
            logger.error("Creating was not successful. Teacher can not be created. Some field is null", e);
            throw new DaoException("Teacher can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Teacher> getAll() {
        logger.debug("Getting all Teachers");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Teacher");
        return query.getResultList();
    }

    public Teacher getById(Integer teacherId) {
        logger.debug("Getting teacher by id = {}", teacherId);
        Session session = sessionFactory.openSession();
        Teacher teacher = Optional.of(session.get(Teacher.class, teacherId))
                .orElseThrow(() -> new DaoException(String.format("Teacher with such id %d does not exist", teacherId)));
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
            throw new DaoException("Teacher can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer teacherId) {
        logger.debug("Deactivating teacher with id = {}", teacherId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        removeTeacherFromAllLessons(teacherId);
        Query query = session.createQuery("UPDATE Teacher SET teacherInactive = true WHERE id =: teacherId");
        query.setParameter("teacherId", teacherId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Deactivating was successful", teacherId);
    }

    public void activate(Integer teacherId) {
        logger.debug("Activating teacher with id = {}", teacherId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Teacher SET teacherInactive = false WHERE id =: teacherId");
        query.setParameter("teacherId", teacherId);

        tx.commit();
        session.close();
        logger.debug("Activating was successful", teacherId);
    }

    public void removeTeacherFromAllLessons(Integer teacherId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Lesson L SET L.teacher = null WHERE L.teacher =: teacherId");
        Teacher teacherG = new Teacher();
        teacherG.setTeacherId(teacherId);
        query.setParameter("teacherId", teacherG);
        query.executeUpdate();

        tx.commit();
        session.close();

        logger.debug("removed Teacher with id = {} from all Lessons", teacherId);
    }

    public Teacher getTeacherByLesson(Integer lessonId) {
        logger.debug("Getting teacher By lesson id = {}", lessonId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("SELECT L.teacher FROM Lesson L WHERE L.lessonId =: lessonId");
        query.setParameter("lessonId", lessonId);
        Optional<Teacher> first = query.getResultList().stream().findFirst();
        Teacher teacher = Optional.of(first.get())
                .orElseThrow(() -> new DaoException(String.format("Such lesson (id = %d) does not have any teacher", lessonId)));

        tx.commit();
        session.close();
        return teacher;
    }
}

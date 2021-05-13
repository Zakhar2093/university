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
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.repository.StudentRepository;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class StudentRepositoryHibernate implements StudentRepository {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepositoryHibernate.class);
    private SessionFactory sessionFactory;
    private Environment env;

    @Autowired
    public StudentRepositoryHibernate(SessionFactory sessionFactory, Environment env) {
        this.sessionFactory = sessionFactory;
        this.env = env;
    }

    public void create(Student student) {
        logger.debug("Creating student with name {} {}", student.getFirstName(), student.getLastName());
        try (Session session = sessionFactory.openSession()){
            session.save(student);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Student can not be created. Some field is null", e);
            throw new RepositoryException("Student can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Student> getAll() {
        logger.debug("Getting all Students");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(env.getProperty("student.getAll"));
        return query.getResultList();
    }

    public Student getById(Integer studentId) {
        logger.debug("Getting student by id = {}", studentId);
        Session session = sessionFactory.openSession();
        Student student = Optional.ofNullable(session.get(Student.class, studentId))
                .orElseThrow(() -> new RepositoryException(String.format("Student with such id %d does not exist", studentId)))
                ;
        return student;
    }

    public void update(Student student) {
        logger.debug("Updating student with name {} {}", student.getFirstName(), student.getLastName());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            session.merge(student);

            tx.commit();
        } catch (PersistenceException e) {
            logger.error("Updating was not successful. Student can not be updated. Some new field is null", e);
            throw new RepositoryException("Student can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer studentId) {
        logger.debug("Deactivating student with id = {}", studentId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("student.deactivate"));
        query.setParameter("studentId", studentId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Deactivating was successful", studentId);
    }

    public void activate(Integer studentId) {
        logger.debug("Activating student with id = {}", studentId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("student.activate"));
        query.setParameter("studentId", studentId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", studentId);
    }

    public List<Student> getStudentsByGroupId(Integer groupId) {
        logger.debug("Getting students with group id = {}",  groupId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("student.getStudentsByGroupId"));
        query.setParameter("groupId", groupId);
        List<Student> students = query.getResultList();

        tx.commit();
        session.close();
        logger.debug("Getting was successful");
        return students;
    }
}
package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Student;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class StudentRepository implements StudentDao{

    private static final Logger logger = LoggerFactory.getLogger(StudentRepository.class);
    private SessionFactory sessionFactory;

    @Autowired
    public StudentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Student student) {
        logger.debug("Creating student with name {} {}", student.getFirstName(), student.getLastName());
        try (Session session = sessionFactory.openSession()){
            session.save(student);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Student can not be created. Some field is null", e);
            throw new DaoException("Student can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Student> getAll() {
        logger.debug("Getting all Students");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Student");
        return query.getResultList();
    }

    public Student getById(Integer studentId) {
        logger.debug("Getting student by id = {}", studentId);
        Session session = sessionFactory.openSession();
        Student student = Optional.ofNullable(session.get(Student.class, studentId))
                .orElseThrow(() -> new DaoException(String.format("Student with such id %d does not exist", studentId)))
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
            throw new DaoException("Student can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer studentId) {
        logger.debug("Deactivating student with id = {}", studentId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Student S SET S.studentInactive = true, S.group = null WHERE S.studentId =: studentId");
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

        Query query = session.createQuery("UPDATE Student SET studentInactive = false WHERE id =: studentId");
        query.setParameter("studentId", studentId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", studentId);
    }
// todo delete
    public void removeStudentFromGroup(Integer studentId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Student S SET S.group = null WHERE S.studentId =: studentId");
        query.setParameter("studentId", studentId);
        query.executeUpdate();

        tx.commit();
        session.close();

        logger.debug("removed Student with id = {} from group", studentId);
    }

    @Override
    public List<Student> getStudentsByGroupId(Integer groupId) {
        logger.debug("Getting students By groupId = {}", groupId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("SELECT G.students From Group G WHERE G.groupId =: groupId");
        query.setParameter("groupId", groupId);
        List<Student> students = query.getResultList();

        tx.commit();
        session.close();
        return students;
    }
}
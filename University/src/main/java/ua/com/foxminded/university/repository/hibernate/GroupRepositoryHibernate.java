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
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.repository.GroupRepository;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class GroupRepositoryHibernate implements GroupRepository {

    private static final Logger logger = LoggerFactory.getLogger(GroupRepositoryHibernate.class);
    private SessionFactory sessionFactory;
    private Environment env;

    @Autowired
    public GroupRepositoryHibernate(SessionFactory sessionFactory, Environment env) {
        this.sessionFactory = sessionFactory;
        this.env = env;
    }

    public void create(Group group) {
        logger.debug("Creating group with name {} {}", group.getGroupName());
        try (Session session = sessionFactory.openSession()){
            session.save(group);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Group can not be created. Some field is null", e);
            throw new RepositoryException("Group can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Group> getAll() {
        logger.debug("Getting all Groups");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery(env.getProperty("group.getAll"));
        return query.getResultList();
    }

    public Group getById(Integer groupId) {
        logger.debug("Getting group by id = {}", groupId);
        Session session = sessionFactory.openSession();
        return Optional.ofNullable(session.get(Group.class, groupId))
                .orElseThrow(() -> new RepositoryException(String.format("Group with such id %d does not exist", groupId)));

    }

    public void update(Group group) {
        logger.debug("Updating group with name {} {}", group.getGroupName());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            session.merge(group);

            tx.commit();
        } catch (PersistenceException e) {
            logger.error("Updating was not successful. Group can not be updated. Some new field is null", e);
            throw new RepositoryException("Group can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer groupId) {
        logger.debug("Deactivating group with id = {}", groupId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Group group = new Group();
        group.setGroupId(groupId);

        Query deactivateGroup = session.createQuery(env.getProperty("group.deactivate"));
        deactivateGroup.setParameter("groupId", groupId);
        deactivateGroup.executeUpdate();

        Query deleteGroupFromStudent = session.createQuery(env.getProperty("group.removeGroupFromAllStudents"));
        deleteGroupFromStudent.setParameter("groupId", group);
        deleteGroupFromStudent.executeUpdate();

        Query deleteGroupFromLesson = session.createQuery(env.getProperty("group.removeGroupFromAllLessons"));
        deleteGroupFromLesson.setParameter("groupId", group);
        deleteGroupFromLesson.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Deactivating was successful", groupId);
    }

    public void activate(Integer groupId) {
        logger.debug("Activating group with id = {}", groupId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery(env.getProperty("group.activate"));
        query.setParameter("groupId", groupId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", groupId);
    }
}
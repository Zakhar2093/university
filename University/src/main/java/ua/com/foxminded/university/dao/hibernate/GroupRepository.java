package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Group;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class GroupRepository implements GroupDao{

    private static final Logger logger = LoggerFactory.getLogger(GroupRepository.class);
    private SessionFactory sessionFactory;

    @Autowired
    public GroupRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Group group) {
        logger.debug("Creating group with name {} {}", group.getGroupName());
        try (Session session = sessionFactory.openSession()){
            session.save(group);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Group can not be created. Some field is null", e);
            throw new DaoException("Group can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Group> getAll() {
        logger.debug("Getting all Groups");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Group");
        return query.getResultList();
    }

    public Group getById(Integer groupId) {
        logger.debug("Getting group by id = {}", groupId);
        Session session = sessionFactory.openSession();
        Group group = Optional.ofNullable(session.get(Group.class, groupId))
                .orElseThrow(() -> new DaoException(String.format("Group with such id %d does not exist", groupId)))
                ;
        return group;
    }

    public void update(Group group) {
        logger.debug("Updating group with name {} {}", group.getGroupName());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            session.merge(group);

            tx.commit();
        } catch (PersistenceException e) {
            logger.error("Updating was not successful. Group can not be updated. Some new field is null", e);
            throw new DaoException("Group can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer groupId) {
        logger.debug("Deactivating group with id = {}", groupId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Group group = new Group();
        group.setGroupId(groupId);

        Query deactivateGroup = session.createQuery("UPDATE Group SET groupInactive = true WHERE id =: groupId");
        deactivateGroup.setParameter("groupId", groupId);
        deactivateGroup.executeUpdate();

        Query deleteGroupFromStudent = session.createQuery("UPDATE Student S SET S.group = null WHERE S.group =: groupId");
        deleteGroupFromStudent.setParameter("groupId", group);
        deleteGroupFromStudent.executeUpdate();

        Query deleteGroupFromLesson = session.createQuery("UPDATE Lesson L SET L.group = null WHERE L.group =: groupId");
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

        Query query = session.createQuery("UPDATE Group SET groupInactive = false WHERE id =: groupId");
        query.setParameter("groupId", groupId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", groupId);
    }
}
package ua.com.foxminded.university.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.university.dao.interfaces.RoomDao;
import ua.com.foxminded.university.exception.DaoException;
import ua.com.foxminded.university.model.Room;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class RoomRepository implements RoomDao {

    private static final Logger logger = LoggerFactory.getLogger(RoomRepository.class);
    private SessionFactory sessionFactory;

    @Autowired
    public RoomRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Room room) {
        logger.debug("Creating room with name {} {}", room.getRoomNumber());
        try (Session session = sessionFactory.openSession()){
            session.save(room);
        } catch (PersistenceException e) {
            logger.error("Creating was not successful. Room can not be created. Some field is null", e);
            throw new DaoException("Room can not be created. Some field is null", e);
        }
        logger.debug("Creating was successful");
    }

    public List<Room> getAll() {
        logger.debug("Getting all Rooms");
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Room");
        return query.getResultList();
    }

    public Room getById(Integer roomId) {
        logger.debug("Getting room by id = {}", roomId);
        Session session = sessionFactory.openSession();
        Room room = Optional.ofNullable(session.get(Room.class, roomId))
                .orElseThrow(() -> new DaoException(String.format("Room with such id %d does not exist", roomId)))
                ;
        return room;
    }

    public void update(Room room) {
        logger.debug("Updating room with name {} {}", room.getRoomNumber());
        try (Session session = sessionFactory.openSession()){
            Transaction tx = session.beginTransaction();

            session.merge(room);

            tx.commit();
        } catch (PersistenceException e) {
            logger.error("Updating was not successful. Room can not be updated. Some new field is null", e);
            throw new DaoException("Room can not be updated. Some new field is null", e);
        }
        logger.debug("Updating was successful");
    }

    public void deactivate(Integer roomId) {
        logger.debug("Deactivating room with id = {}", roomId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query deactivateRoom = session.createQuery("UPDATE Room SET roomInactive = true WHERE id =: roomId");
        deactivateRoom.setParameter("roomId", roomId);
        deactivateRoom.executeUpdate();

        Query deleteRoomFromLesson = session.createQuery("UPDATE Lesson L SET L.room = null WHERE L.room =: roomId");
        Room roomG = new Room();
        roomG.setRoomId(roomId);
        deleteRoomFromLesson.setParameter("roomId", roomG);
        deleteRoomFromLesson.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Deactivating was successful", roomId);
    }

    public void activate(Integer roomId) {
        logger.debug("Activating room with id = {}", roomId);
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("UPDATE Room SET roomInactive = false WHERE id =: roomId");
        query.setParameter("roomId", roomId);
        query.executeUpdate();

        tx.commit();
        session.close();
        logger.debug("Activating was successful", roomId);
    }
}
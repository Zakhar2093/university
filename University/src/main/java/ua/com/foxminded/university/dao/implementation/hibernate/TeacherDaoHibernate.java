package ua.com.foxminded.university.dao.implementation.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.model.Teacher;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class TeacherDaoHibernate {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TeacherDaoHibernate(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Teacher findById(Integer id) {
        return entityManager.find(Teacher.class, id);
    }
}
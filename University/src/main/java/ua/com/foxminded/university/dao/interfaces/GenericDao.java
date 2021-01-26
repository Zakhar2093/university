package ua.com.foxminded.university.dao.interfaces;

import java.util.List;

public interface GenericDao<T, Id> {
    void create(T t);
    
    List<T> getAll();

    T getById(Id id);

    void delete(Id id);

    void update(T t);
    
    void deactivate(Id id);
    
    void activate(Id id);
}
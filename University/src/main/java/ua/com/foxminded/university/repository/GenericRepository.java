package ua.com.foxminded.university.repository;

import java.util.List;

public interface GenericRepository<T, Id> {
    void create(T t);
    
    List<T> getAll();

    T getById(Id id);

    void update(T t);
    
    void deactivate(Id id);
    
    void activate(Id id);
}
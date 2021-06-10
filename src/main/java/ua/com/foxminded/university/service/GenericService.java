package ua.com.foxminded.university.service;

import java.util.List;

public interface GenericService<T, Id> {

    void save(T t);

    List<T> findAll();

    T findById(Id id);

    void deactivate(Id id);

    void activate(Id id);
}

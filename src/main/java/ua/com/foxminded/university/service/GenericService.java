package ua.com.foxminded.university.service;

import java.util.List;

public interface GenericService<T, Id> {

    void create(T t);

    List<T> getAll();

    List<T> getAllActivated();

    T getById(Id id);

    void update(T t);

    void deactivate(Id id);

    void activate(Id id);
}

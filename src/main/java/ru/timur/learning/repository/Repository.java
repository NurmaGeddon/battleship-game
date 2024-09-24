package ru.timur.learning.repository;

import java.util.List;

public interface Repository<T, IdT> {
    T save(T entity);

    T findById(IdT idT);

    List<T> findAll();

    T update(T entity);

    boolean deleteById(IdT idT);

    void deleteAll();
}

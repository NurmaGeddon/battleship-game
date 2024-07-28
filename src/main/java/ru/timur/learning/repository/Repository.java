package ru.timur.learning.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, IdT> {
    T save(T entity);

    Optional<T> findById(IdT idT);

    List<T> findAll();

    T update(T entity);

    boolean deleteById(IdT idT);
}

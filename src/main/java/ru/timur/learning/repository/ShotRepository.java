package ru.timur.learning.repository;

import ru.timur.learning.model.entity.ShotEntity;

import java.util.List;

public interface ShotRepository {
    ShotEntity save(ShotEntity entity);

    ShotEntity findByKey(Long gameId, Integer shotNumber);

    List<ShotEntity> findAll();

    boolean deleteByKey(Long gameId, Integer playerNumber);

    List<ShotEntity> findShotsForPlayer(Long gameId, Integer playerNumber);

    void deleteAll();
}

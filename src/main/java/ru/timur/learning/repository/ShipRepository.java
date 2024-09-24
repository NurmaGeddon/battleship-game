package ru.timur.learning.repository;

import ru.timur.learning.model.entity.ShipEntity;

import java.util.List;

public interface ShipRepository extends Repository<ShipEntity, Long> {
    List<ShipEntity> findAllForGame(Long gameId);
}

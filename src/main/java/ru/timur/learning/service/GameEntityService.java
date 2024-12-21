package ru.timur.learning.service;

import ru.timur.learning.model.entity.GameEntity;

public interface GameEntityService {
    Long createGameEntity(Long userId);

    Long joinGame(Long gameId, Long userId);

    GameEntity findById(Long gameId);

    void changePlayerShotTurn(Long gameId);

    void changeGameEntityForWinner(Long gameId, Long userId);

    void updateUserStatusToReady(Long gameId, Long userId);

    void tryToStartGame(Long gameId);
}

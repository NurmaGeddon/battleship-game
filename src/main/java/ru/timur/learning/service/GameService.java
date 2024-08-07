package ru.timur.learning.service;

import ru.timur.learning.model.Game;

public interface GameService {
    Long createGame(Long userId);

    Long joinGame(Long userId);

    Game getGame(Long gameId);

    Game getGameForUser(Long gameId, Long userId);
}

package ru.timur.learning.service;

import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.GameDto;

public interface GameService {
    Long createGame(Long userId);

    Long joinGame(Long userId);

    Game getGame(Long gameId);

    GameDto getGameForUser(Long gameId, Long userId);

    void userReadyForGame(Long gameId, Long userId);
}

package ru.timur.learning.service;

import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.GameDto;

public interface GameService {
    Long createGame(Long userId);

    Long joinGame(Long userId);

    Game getGame(Long gameId);

    GameDto getGameDtoForUser(Long gameId, Long userId);

    void changeUserStatusToReady(Long gameId, Long userId);

    void checkIfPlayerWon(Long gameId, Long userId);
}

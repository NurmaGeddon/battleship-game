package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Game;
import ru.timur.learning.repository.GameRepository;
import ru.timur.learning.service.GameService;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    @Override
    public Long createGame(Long userId) {
        Game game = new Game(null, userId, null, Game.GameState.WAITING_FOR_PLAYER);
        Game savedGame = gameRepository.save(game);
        return savedGame.getId();
    }

    @Override
    public Long joinGame(Long userId) {
        Game gameToJoin = null;
        Game updatedGame = new Game(gameToJoin.getId(),
                                    gameToJoin.getPlayer1Id(),
                                    userId,
                                    Game.GameState.SHIP_PLACEMENT);
        gameRepository.update(updatedGame);
        return gameToJoin.getId();
    }

    @Override
    public Game getGame(Long gameId) {
        return null;
    }

    @Override
    public Game getGameForUser(Long gameId, Long userId) {

        return null;
    }
}

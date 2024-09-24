package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Board;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.GameDto;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.repository.GameRepository;
import ru.timur.learning.service.GameService;
import ru.timur.learning.service.ShotService;
import ru.timur.learning.service.ShipService;

import java.util.ArrayDeque;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final ShipService shipService;

    private final ShotService shotService;

    private final Queue<Long> gamesToJoin = new ArrayDeque<>();

    @Override
    public Long createGame(Long userId) {
        GameEntity game = createNewGameEntity(userId);

        GameEntity savedGame = gameRepository.save(game);
        gamesToJoin.add(savedGame.getId());

        return savedGame.getId();
    }

    private GameEntity createNewGameEntity(Long userId) {
        return new GameEntity(
                null,
                userId,
                null,
                false,
                false,
                null,
                null);
    }

    @Override
    public Long joinGame(Long userId) {
        GameEntity gameToJoin = getGameToJoin();
        GameEntity updatedGameEntity = createUpdatedGameEntity(gameToJoin, userId);
        return gameRepository.update(updatedGameEntity).getId();
    }

    private GameEntity getGameToJoin() {
        Long idGameToJoin = gamesToJoin.poll();
        return gameRepository.findById(idGameToJoin);
    }

    private GameEntity createUpdatedGameEntity(GameEntity gameToJoin, Long userId) {
        return new GameEntity(
                gameToJoin.getId(),
                gameToJoin.getPlayer1Id(),
                userId,
                false,
                false,
                GameEntity.GameState.SHIP_PLACEMENT,
                0L);
    }

    @Override
    public Game getGame(Long gameId) {
        return constructGame(gameId);
    }

    @Override
    public GameDto getGameForUser(Long gameId, Long userId) {
        Game game = constructGame(gameId);
        return new GameDto(game, userId);
    }

    @Override
    public void userReadyForGame(Long gameId, Long userId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        changePlayerReadyStatus(gameEntity, userId);
    }

    private void changePlayerReadyStatus(GameEntity gameEntity, Long userId) {
        gameEntity.changePlayerStatusToReady(userId);
        gameRepository.update(gameEntity);
    }

    private Game constructGame(Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        Board player1Board = new Board(shipService, shotService, gameId, 1);
        Board player2Board = new Board(shipService, shotService, gameId, 2);
        return new Game(gameEntity, player1Board, player2Board);
    }
}

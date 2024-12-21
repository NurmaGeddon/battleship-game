package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.repository.GameRepository;
import ru.timur.learning.service.GameEntityService;

@Service
@RequiredArgsConstructor
public class GameEntityServiceImpl implements GameEntityService {
    private final GameRepository gameRepository;

    private static GameEntity createNewGameEntity(final Long userId) {
        return new GameEntity(
                null,
                userId,
                null,
                false,
                false,
                GameEntity.GameState.WAITING_FOR_PLAYER,
                null);
    }

    @Override
    public Long createGameEntity(final Long userId) {
        GameEntity newGameEntity = createNewGameEntity(userId);
        GameEntity savedGameEntity = gameRepository.save(newGameEntity);
        return savedGameEntity.getId();
    }

    @Override
    public Long joinGame(final Long gameId, final Long userId) {
        GameEntity gameToJoin = gameRepository.findById(gameId);
        gameToJoin.tryJoinPlayer2(userId);

        return gameRepository.update(gameToJoin).getId();
    }

    @Override
    public GameEntity findById(final Long gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    public void changePlayerShotTurn(final Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        gameEntity.changePlayerShotTurn();
        gameRepository.update(gameEntity);
    }

    @Override
    public void changeGameEntityForWinner(final Long gameId, final Long userId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        gameEntity.setWinnerId(userId);
        gameEntity.setGameState(GameEntity.GameState.GAME_FINISHED);
        gameRepository.update(gameEntity);
    }

    @Override
    public void updateUserStatusToReady(final Long gameId, final Long userId) {
        GameEntity gameEntity = gameRepository.findById(gameId);
        gameEntity.changePlayerStatusToReady(userId);
        gameRepository.update(gameEntity);
    }

    @Override
    public void tryToStartGame(final Long gameId) {
        GameEntity gameEntity = gameRepository.findById(gameId);

        if (gameEntity.getPlayer1Ready() && gameEntity.getPlayer2Ready()) {
            gameEntity.setGameState(GameEntity.GameState.PLAYER1_TURN);
            gameRepository.update(gameEntity);
        }
    }
}

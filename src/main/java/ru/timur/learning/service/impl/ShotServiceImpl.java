package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.ShotDto;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.repository.ShotRepository;
import ru.timur.learning.service.GameService;
import ru.timur.learning.service.ShotService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShotServiceImpl implements ShotService {
    private final ShotRepository shotRepository;

    @Override
    public void takeShot(Game game, Long userId, ShotDto shotDto) {
        checkPlayerShotTurn(game, userId);

        ShotEntity shotEntity = createShotEntity(game, userId, shotDto);
        shotRepository.save(shotEntity);

    }

    private void checkPlayerShotTurn(Game game, Long userId) {
        GameEntity.GameState gameState = game.getGameState();
        checkStateIsPlayerShots(gameState);

        Integer playerNumber = game.getPlayerNumberForGame(userId);
        checkCorrectPlayerTurn(gameState, playerNumber);
    }

    private void checkStateIsPlayerShots(GameEntity.GameState gameState) {
        if (!gameState.equals(GameEntity.GameState.PLAYER1_TURN)
                && !gameState.equals(GameEntity.GameState.PLAYER2_TURN)) {
            throw new IllegalStateException("Cannot make shot during game state: " + gameState);
        }
    }

    private void checkCorrectPlayerTurn(GameEntity.GameState gameState, Integer playerNumber) {
        if ((gameState.equals(GameEntity.GameState.PLAYER1_TURN) && playerNumber != 1) ||
                (gameState.equals(GameEntity.GameState.PLAYER2_TURN) && playerNumber != 2)) {
            throw new IllegalStateException("Cannot make shot during player turn: "
                    + gameState);
        }
    }

    private ShotEntity createShotEntity(Game game, Long userId, ShotDto shotDto) {

        Integer playerNumber = game.getPlayerNumberForGame(userId);
        Integer nextShotNum = getNextShotNum(game.getId(), playerNumber);
        ShotEntity.Outcome outcome = game.getShotOutcome(userId, shotDto.getPGpoint());

        return new ShotEntity(
                game.getId(),
                nextShotNum,
                playerNumber,
                shotDto.getPGpoint(),
                outcome);
    }

    private Integer getNextShotNum(Long gameId, Integer playerNumber) {
        List<ShotEntity> shotEntities = shotRepository.findShotsForPlayer(gameId, playerNumber);
        if (shotEntities.isEmpty()) {
            return playerNumber;
        }
        return findLastShotNum(shotEntities) + 2;
    }

    private Integer findLastShotNum(List<ShotEntity> shotEntities) {
        return shotEntities.stream()
                .map(ShotEntity::getShotNum)
                .max(Comparator.comparingInt(l -> l))
                .orElseThrow();
    }

    @Override
    public List<PGpoint> getCoordinates(Long gameId, Integer playerNumber) {
        List<ShotEntity> shotEntities = shotRepository.findShotsForPlayer(gameId, playerNumber);
        return shotEntities.stream()
                .map(ShotEntity::getCoordinate).toList();
    }
}

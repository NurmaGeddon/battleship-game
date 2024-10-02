package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.ShotDto;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.repository.ShotRepository;
import ru.timur.learning.service.ShotService;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShotServiceImpl implements ShotService {

    private final ShotRepository shotRepository;

    @Override
    public void takeShot(Game game, Long userId, ShotDto shotDto) {
        PGpoint shotPoint = shotDto.getPGpoint();

        Integer playerNumber = game.getPlayerNumberForGame(userId);
        Integer nextShotNum = getNextShotNum(game.getId(), playerNumber);
        ShotEntity.Outcome outcome = game.getShotOutcome(userId, shotPoint);

        ShotEntity shotEntity = new ShotEntity(
                game.getId(),
                nextShotNum,
                playerNumber,
                shotPoint,
                outcome);

        shotRepository.save(shotEntity);
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

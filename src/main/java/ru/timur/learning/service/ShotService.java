package ru.timur.learning.service;

import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.ShotDto;

import java.util.List;

public interface ShotService {

    void takeShot(Game game, Long userId, ShotDto shotDto);

    List<PGpoint> getCoordinates(Long gameId, Integer playerNumber);
}

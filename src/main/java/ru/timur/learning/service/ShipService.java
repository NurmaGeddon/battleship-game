package ru.timur.learning.service;

import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.InitiallyPlacedShips;
import ru.timur.learning.model.dto.ShipDto;
import ru.timur.learning.model.entity.ShipEntity;

import java.util.List;

public interface ShipService {
    List<ShipEntity> getShipsForPlayer(Long gameId, Integer playerNumber);

    InitiallyPlacedShips createInitiallyPlacedShips(Long gameId, Integer playerNumber);

    void placeShip(Game game, Long userId, PGpoint[] coordinates);

    void changeShipPlacement(Game game, Long userId, Long shipId, PGpoint[] coordinates);

    void deleteShip(Long shipId);
}

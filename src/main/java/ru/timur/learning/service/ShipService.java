package ru.timur.learning.service;

import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.ShipsOnBoard;
import ru.timur.learning.model.dto.ShipDto;

import java.util.List;

public interface ShipService {
    List<PGpoint> getShipsCoordinates(Long gameId, Integer playerNumber);

    ShipsOnBoard createShipsOnBoard(Long gameId, Integer playerNumber);

    void placeShip(Game game, Long userId, ShipDto shipDto);

    void changeShipPlacement(Long gameId, Long shipId, ShipDto shipDto);

    void deleteShip(Long shipId);
}

package ru.timur.learning.service;

import ru.timur.learning.model.Game;
import ru.timur.learning.model.ShipsOnBoard;
import ru.timur.learning.model.dto.ShipDto;
import ru.timur.learning.model.entity.ShipEntity;

import java.util.List;

public interface ShipService {
    List<ShipEntity> getShipsForPlayer(Long gameId, Integer playerNumber);

    ShipsOnBoard createShipsOnBoard(Long gameId, Integer playerNumber);

    void placeShip(Game game, Long userId, ShipDto shipDto);

    void changeShipPlacement(Game game, Long userId, Long shipId, ShipDto shipDto);

    void deleteShip(Long shipId);
}

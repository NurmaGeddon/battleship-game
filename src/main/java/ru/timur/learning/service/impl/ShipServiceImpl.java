package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Board;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.ShipsOnBoard;
import ru.timur.learning.model.dto.ShipDto;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.repository.ShipRepository;
import ru.timur.learning.service.ShipService;

import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipServiceImpl implements ShipService {
    private final ShipRepository shipRepository;

    @Override
    public List<ShipEntity> getShipsForPlayer(Long gameId, Integer playerNumber) {
        return shipRepository.findAllForGameAndPlayer(gameId, playerNumber);
    }

    @Override
    public ShipsOnBoard createShipsOnBoard(Long gameId, Integer playerNumber) {
        List<ShipEntity> shipEntities = shipRepository.findAllForGameAndPlayer(gameId, playerNumber);
        return new ShipsOnBoard(shipEntities);
    }

    @Override
    public void placeShip(Game game, Long userId, ShipDto shipDto) {
        Integer playerNumber = game.getPlayerNumberForGame(userId);
        ShipEntity shipEntity = new ShipEntity(null,
                                                game.getId(),
                                                playerNumber,
                                                shipDto.getCoordinates());

        shipRepository.save(shipEntity);
    }


    @Override
    public void changeShipPlacement(Long gameId, Long shipId, ShipDto shipDto) {
        ShipEntity shipEntity = shipRepository.findById(shipId);
        shipEntity.setCoordinates(shipDto.getCoordinates());
        shipRepository.save(shipEntity);
    }

    @Override
    public void deleteShip(Long shipId) {
        shipRepository.deleteById(shipId);
    }
}

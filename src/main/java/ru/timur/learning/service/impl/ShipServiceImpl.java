package ru.timur.learning.service.impl;

import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Service;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.dto.ShipDto;
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
    public List<PGpoint> getShipsCoordinates(Long gameId, Integer playerNumber) {
        List<ShipEntity> shipEntities = shipRepository.findAllForGame(gameId);
        List<PGpoint[]> shipsCoordinates = shipEntities
                .stream()
                .map(ShipEntity::getCoordinates)
                .toList();
        return makeListOfCoordinates(shipsCoordinates);
    }

    private List<PGpoint> makeListOfCoordinates(List<PGpoint[]> shipsCoordinates) {
        List<PGpoint> result = new ArrayList<>();
        for (PGpoint[] points : shipsCoordinates) {
            result.addAll(Arrays.stream(points).toList());
        }
        return result;
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

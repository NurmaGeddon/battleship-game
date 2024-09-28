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

    private static ShipEntity createNewShipEntity(Long gameId, Integer playerNumber, PGpoint[] coordinates) {
        return new ShipEntity(null,
                gameId,
                playerNumber,
                coordinates);
    }

    @Override
    public List<PGpoint> getShipsCoordinates(Long gameId, Integer playerNumber) {
        List<ShipEntity> shipEntities = shipRepository.findAllForGameAndPlayer(gameId, playerNumber);
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
    public ShipsOnBoard createShipsOnBoard(Long gameId, Integer playerNumber) {
        List<ShipEntity> shipEntities = shipRepository.findAllForGameAndPlayer(gameId, playerNumber);
        return new ShipsOnBoard(shipEntities);
    }

    @Override
    public void placeShip(Game game, Long userId, ShipDto shipDto) {
        checkPlayerCanPlaceShip(game, userId, shipDto);

        Integer playerNumber = game.getPlayerNumberForGame(userId);
        ShipEntity shipEntity = createNewShipEntity(game.getId(),
                                                    playerNumber,
                                                    shipDto.getCoordinates());

        shipRepository.save(shipEntity);
    }

    private void checkPlayerCanPlaceShip(Game game, Long userId, ShipDto shipDto) {
        checkGameState(game);
        checkPlayerCanPlaceShipWithSize(game, userId, shipDto.getCoordinates().length);
        checkCoordinatesAreFree(game, userId, shipDto);
    }

    private void checkGameState(Game game) {
        if (!game.getGameState().equals(GameEntity.GameState.SHIP_PLACEMENT)) {
            throw new IllegalArgumentException();
        }
    }

    private void checkPlayerCanPlaceShipWithSize(Game game, Long userId, int length) {
        ShipsOnBoard shipsOnBoard = game.getMyBoard(userId).getShipsOnBoard();
        shipsOnBoard.checkCanPlaceAnotherShip(length);
    }

    private void checkCoordinatesAreFree(Game game, Long userId, ShipDto shipDto) {
        Board board = game.getMyBoard(userId);
        board.checkCoordinatesAreFree(shipDto.getCoordinates());
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

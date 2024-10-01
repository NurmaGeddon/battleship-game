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
        checkPlayerCanPlaceShip(game, userId, shipDto);

        Integer playerNumber = game.getPlayerNumberForGame(userId);
        ShipEntity shipEntity = createNewShipEntity(game.getId(),
                                                    playerNumber,
                                                    shipDto.getCoordinates());

        shipRepository.save(shipEntity);
    }

    private void checkPlayerCanPlaceShip(Game game, Long userId, ShipDto shipDto) {
        // TODO check that player is not ready for game
        checkGameState(game);
        checkPlayerCanPlaceShipWithSize(game, userId, shipDto.getCoordinates().length);
        checkCoordinatesAreFree(game, userId, shipDto);
    }

    private void checkGameState(Game game) {
        if (!game.getGameState().equals(GameEntity.GameState.SHIP_PLACEMENT)) {
            throw new IllegalArgumentException("Wrong game state");
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
    public void changeShipPlacement(Game game, Long userId, Long shipId, ShipDto shipDto) {
        checkPlayerCanChangeShipPlacement(game, userId, shipId, shipDto);

        ShipEntity shipEntity = shipRepository.findById(shipId);
        shipEntity.setCoordinates(shipDto.getCoordinates());
        shipRepository.update(shipEntity);
    }

    private void checkPlayerCanChangeShipPlacement(Game game, Long userId, Long shipId, ShipDto shipDto) {
        // TODO check that player is not ready for game
        checkGameState(game);
        checkShipLengthsAreSame(shipId, shipDto);
        checkCanChangeShipToCoordinates(game, userId, shipId, shipDto);
    }

    private void checkShipLengthsAreSame(Long shipId, ShipDto shipDto) {
        PGpoint[] fromCoordinates = shipRepository.findById(shipId).getCoordinates();
        PGpoint[] toCoordinates = shipDto.getCoordinates();

        if (fromCoordinates.length != toCoordinates.length) {
            throw new IllegalArgumentException("Trying to change ship " +
                    "to different ship with different size");
        }
    }

    private void checkCanChangeShipToCoordinates(Game game, Long userId, Long shipId, ShipDto shipDto) {
        Board board = game.getMyBoard(userId);
        PGpoint[] fromCoordinates = shipRepository.findById(shipId).getCoordinates();
        PGpoint[] toCoordinates = shipDto.getCoordinates();

        board.checkCanChangeShipCoordinates(fromCoordinates, toCoordinates);
    }

    @Override
    public void deleteShip(Long shipId) {
        shipRepository.deleteById(shipId);
    }
}

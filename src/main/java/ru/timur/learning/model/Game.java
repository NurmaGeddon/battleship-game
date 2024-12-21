package ru.timur.learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.dto.ShipDto;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShotEntity;

@Data
@AllArgsConstructor
public class Game {
    private Long id;

    private Long player1Id;

    private Long player2Id;

    private Board player1Board;

    private Board player2Board;

    private GameEntity.GameState gameState;

    public Game(GameEntity entity, Board player1Board, Board player2Board) {
        id = entity.getId();
        player1Id = entity.getPlayer1Id();
        player2Id = entity.getPlayer2Id();
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        gameState = entity.getGameState();
    }

    public Boolean checkPlayerWon(Long userId) {
        return getOpponentBoard(userId).checkPlayerWon();
    }

    public Integer getPlayerNumberForGame(Long userId) {
        if (userId.equals(player1Id)) {
            return 1;
        } else if (userId.equals(player2Id)) {
            return 2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Board getMyBoard(Long userId) {
        Integer playerNumber = getPlayerNumberForGame(userId);
        if (playerNumber.equals(1)) {
            return player1Board;
        } else {
            return player2Board;
        }
    }

    public Board getOpponentBoard(Long userId) {
        Integer playerNumber = getPlayerNumberForGame(userId);
        if (playerNumber.equals(1)) {
            return player2Board;
        } else {
            return player1Board;
        }
    }

    public ShotEntity.Outcome getShotOutcome(Long userId, PGpoint point) {
        Board opponentBoard = getOpponentBoard(userId);
        return opponentBoard.getShotOutcome(point);
    }

    public void checkPlayerFinishedShipPlacement(Long userId) {
        InitiallyPlacedShips initiallyPlacedShips = getMyBoard(userId).getInitiallyPlacedShips();
        initiallyPlacedShips.checkPlacedAllShips();
    }

    public void checkPlayerCanPlaceShipWithSize(Long userId, int length) {
        InitiallyPlacedShips initiallyPlacedShips = getMyBoard(userId).getInitiallyPlacedShips();
        initiallyPlacedShips.checkCanPlaceAnotherShip(length);
    }

    public void checkStateShipPlacement() {
        if (!getGameState().equals(GameEntity.GameState.SHIP_PLACEMENT)) {
            throw new IllegalArgumentException("Wrong game state");
        }
    }

    public void checkCoordinatesAreFree(Long userId, ShipDto shipDto) {
        Board board = getMyBoard(userId);
        board.checkCoordinatesAreFree(shipDto.getCoordinates());
    }
}
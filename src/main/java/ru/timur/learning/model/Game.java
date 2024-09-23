package ru.timur.learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShotEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    public Integer getPlayerNumberForGame(Long userId) {
        if (userId.equals(player1Id)) {
            return 1;
        } else if (userId.equals(player2Id)) {
            return 2;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public ShotEntity.Outcome getShotOutcome(Long userId, PGpoint point) {
        Integer playerNumber = getPlayerNumberForGame(userId);
        Board opponentBoard = getOpponentBoard(playerNumber);

        return opponentBoard.getShotOutcome(point);
    }

    private Board getOpponentBoard(Integer shooterPlayerNumber) {
        return shooterPlayerNumber.equals(1) ? player2Board :
                shooterPlayerNumber.equals(2) ? player1Board : null;
    }
}
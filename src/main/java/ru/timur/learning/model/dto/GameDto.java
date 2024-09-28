package ru.timur.learning.model.dto;

import lombok.Getter;
import ru.timur.learning.model.Board;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.entity.GameEntity;

@Getter
public class GameDto {
    private final Board.CellState[][] myBoard;

    private final Board.CellState[][] opponentBoard;

    private final GameEntity.GameState state;

    public GameDto(Game game, Long userId) {
        myBoard = game.getMyBoard(userId).getGird();
        opponentBoard = game.getOpponentBoard(userId).getFilteredGrid();
        state = game.getGameState();
    }
}

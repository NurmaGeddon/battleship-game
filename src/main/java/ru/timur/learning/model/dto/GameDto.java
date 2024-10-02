package ru.timur.learning.model.dto;

import lombok.Getter;
import ru.timur.learning.model.Cell;
import ru.timur.learning.model.Game;
import ru.timur.learning.model.entity.GameEntity;

@Getter
public class GameDto {
    private final Cell[][] myBoard;

    private final Cell[][] opponentBoard;

    private final GameEntity.GameState state;

    public GameDto(Game game, Long userId) {
        myBoard = game.getMyBoard(userId).getGrid();
        opponentBoard = game.getOpponentBoard(userId).getFilteredGrid();
        state = game.getGameState();
    }
}

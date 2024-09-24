package ru.timur.learning.model.dto;

import lombok.Getter;
import ru.timur.learning.model.Board;
import ru.timur.learning.model.Game;

@Getter
public class GameDto {
    private final Board.CellState[][] myBoard;

    private final Board.CellState[][] opponentBoard;

    public GameDto(Game game, Long userId) {
        if (game.getPlayer1Id().equals(userId)) {
            myBoard = game.getPlayer1Board().getMyView();
            opponentBoard = game.getPlayer2Board().getOpponentView();
        } else if (game.getPlayer2Id().equals(userId)) {
            myBoard = game.getPlayer2Board().getMyView();
            opponentBoard = game.getPlayer1Board().getOpponentView();
        } else {
            throw new IllegalArgumentException();
        }
    }
}

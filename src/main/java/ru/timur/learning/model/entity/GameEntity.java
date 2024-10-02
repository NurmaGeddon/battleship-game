package ru.timur.learning.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameEntity {
    public enum GameState {
        WAITING_FOR_PLAYER,
        SHIP_PLACEMENT,
        PLAYER1_TURN,
        PLAYER2_TURN,
        GAME_FINISHED,
        CANCELLED
    }

    private Long id;

    private Long player1Id;

    private Long player2Id;

    private Boolean player1Ready;

    private Boolean player2Ready;

    private GameState gameState;

    private Long winnerId;

    public void changePlayerStatusToReady(Long playerId) {
        if (playerId.equals(player1Id)) {
            this.setPlayer1Ready(true);
        } else if (playerId.equals(player2Id)) {
            this.setPlayer2Ready(true);
        } else {
            throw new IllegalArgumentException("Cant change player ready status," +
                                               " player not found in game");
        }
    }

    public void changePlayerShotTurn() {
        if (gameState.equals(GameState.PLAYER1_TURN)) {
            gameState = GameState.PLAYER2_TURN;
        } else if (gameState.equals(GameState.PLAYER2_TURN)) {
            gameState = GameState.PLAYER1_TURN;
        } else {
            throw new IllegalStateException("Cannot change player turn");
        }
    }

    public void updateWithPlayer2(Long player2Id) {
        this.setPlayer2Id(player2Id);
        this.setGameState(GameState.SHIP_PLACEMENT);
    }
}
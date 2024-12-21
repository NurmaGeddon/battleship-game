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

    public void tryJoinPlayer2(Long userId) {
        checkGameWaitsForPlayer2();
        checkUserNotPlayer1(userId);
        updateWithPlayer2(userId);
    }

    public void checkGameWaitsForPlayer2() {
        if (!getGameState().equals(GameEntity.GameState.WAITING_FOR_PLAYER)
                || getPlayer2Id() != null) {
            throw new IllegalArgumentException("Game already taken");
        }
    }

    public void checkUserNotPlayer1(Long userId) {
        if (getPlayer1Id().equals(userId)) {
            throw new IllegalArgumentException("Player trying to join game that he created");
        }
    }

    public void updateWithPlayer2(Long player2Id) {
        this.setPlayer2Id(player2Id);
        this.setGameState(GameState.SHIP_PLACEMENT);
    }

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
}
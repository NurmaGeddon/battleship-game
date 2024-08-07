package ru.timur.learning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Game {

    public enum GameState {
        WAITING_FOR_PLAYER,
        SHIP_PLACEMENT,
        GAME_STARTED,
        GAME_FINISHED
    }

    private Long id;

    private Long player1Id;

    private Long player2Id;

    private GameState gameState;
}
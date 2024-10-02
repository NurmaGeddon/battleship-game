package ru.timur.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class Cell {
    public enum CellState {
        FREE, SHIP, SHIP_HIT, MISSED
    }

    private CellState cellState;

    private Long shipId;

    private Boolean isDestroyed;
}

package ru.timur.learning.model;

import lombok.Getter;
import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.entity.ShotEntity;

import java.util.Arrays;
import java.util.List;

@Getter
public class Board {

    public enum CellState {
        FREE, SHIP, SHIP_HIT, MISSED
    }

    // TODO move to separate settings class
    public static final Integer GRID_SIZE = 10;

    private final ShipsOnBoard shipsOnBoard;

    private final CellState[][] grid;

    {
        this.grid = new CellState[GRID_SIZE][GRID_SIZE];
        for (CellState[] row : this.grid) {
            Arrays.fill(row, CellState.FREE);
        }
    }

    public Board(ShipsOnBoard shipsOnBoard, List<PGpoint> shipsCoordinates, List<PGpoint> shotsCoordinates) {
        this.shipsOnBoard = shipsOnBoard;
        placeShips(shipsCoordinates);
        placeShots(shotsCoordinates);
    }

    private void placeShips(List<PGpoint> coordinates) {
        assert grid != null;
        for (PGpoint point : coordinates) {
            grid[(int) point.x][(int) point.y] = CellState.SHIP;
        }
    }

    private void placeShots(List<PGpoint> shotsCoordinates) {
        for (PGpoint shotPoint : shotsCoordinates) {
            int x = (int) shotPoint.x;
            int y = (int) shotPoint.x;

            assert grid != null;
            CellState cellState = grid[x][y];

            if (cellState.equals(CellState.SHIP)) {
                grid[x][y] = CellState.SHIP_HIT;
            } else {
                grid[x][y] = CellState.MISSED;
            }
        }
    }

    public ShotEntity.Outcome getShotOutcome(PGpoint point) {
        CellState cellState = grid[(int) point.x][(int) point.y];

        if (cellState.equals(CellState.SHIP)) {
            return ShotEntity.Outcome.HIT;
        } else if (cellState.equals(CellState.FREE)) {
            return ShotEntity.Outcome.MISS;
        } else {
            throw new IllegalArgumentException("Cannot take shot here");
        }
    }

    public CellState[][] getGird() {
        return grid;
    }

    public CellState[][] getFilteredGrid() {
        CellState[][] result = grid.clone();

        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {
                if (grid[x][y].equals(CellState.SHIP)) {
                    result[x][y] = CellState.FREE;
                }
            }
        }
        return result;
    }

    public void checkCoordinatesAreFree(PGpoint[] coordinates) {
        Arrays.stream(coordinates).forEach(pGpoint -> {
            int x = (int) pGpoint.x;
            int y = (int) pGpoint.y;

            if (!grid[x][y].equals(CellState.FREE)) {
                throw new IllegalArgumentException("Selected coordinates are already taken");
            }
        });
    }
}

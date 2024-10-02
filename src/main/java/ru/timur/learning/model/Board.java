package ru.timur.learning.model;

import lombok.Getter;
import org.postgresql.geometric.PGpoint;
import org.springframework.beans.factory.annotation.Autowired;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.service.ShipService;
import ru.timur.learning.service.ShotService;

import java.util.Arrays;
import java.util.List;

@Getter
public class Board {

    public enum CellState {
        FREE, SHIP, SHIP_HIT, MISSED
    }

    private static final Integer GRID_SIZE = 10;

    private final ShipService shipService;

    private final ShotService shotService;

    private final Long gameId;

    private final Integer playerNumber;

    private final CellState[][] grid;

    {
        this.grid = new CellState[GRID_SIZE][GRID_SIZE];
        for (CellState[] row : this.grid) {
            Arrays.fill(row, CellState.FREE);
        }
    }

    @Autowired
    public Board(ShipService shipService, ShotService shotService, Long gameId, Integer playerNumber) {
        this.shipService = shipService;
        this.shotService = shotService;
        this.gameId = gameId;
        this.playerNumber = playerNumber;
        placeShips();
        placeShots();
    }

    private void placeShips() {
        List<PGpoint> shipsCoordinates = shipService.getShipsCoordinates(gameId, playerNumber);

        changeCoordinatesStateTo(shipsCoordinates);
    }

    public ShotEntity.Outcome getShotOutcome(PGpoint point) {
        CellState cellState = grid[(int) point.x][(int) point.y];
        if (cellState.equals(CellState.SHIP)) {
            return ShotEntity.Outcome.HIT;
        } else if (cellState.equals(CellState.FREE)) {
            return ShotEntity.Outcome.MISS;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void changeCoordinatesStateTo(List<PGpoint> coordinates) {
        for (PGpoint point : coordinates) {
            grid[(int) point.x][(int) point.y] = CellState.SHIP;
        }
    }

    private void placeShots() {
        Integer opponentPlayerNumber = playerNumber.equals(1) ? 2 : 1;
        List<PGpoint> shotsCoordinates = shotService.getCoordinates(gameId, opponentPlayerNumber);

        for (PGpoint shotPoint : shotsCoordinates) {
            assert grid != null;
            CellState cellState = grid[(int) shotPoint.x][(int) shotPoint.y];

            if (cellState.equals(CellState.SHIP)) {
                grid[(int) shotPoint.x][(int) shotPoint.y] = CellState.SHIP_HIT;
            } else {
                grid[(int) shotPoint.x][(int) shotPoint.y] = CellState.MISSED;
            }
        }
    }

    public CellState[][] getMyView() {
        return grid;
    }

    public CellState[][] getOpponentView() {
        CellState[][] result = new CellState[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                result[i][j] = grid[i][j].equals(CellState.SHIP)
                        ? CellState.FREE
                        : grid[i][j];
            }
        }
        return result;
    }
}

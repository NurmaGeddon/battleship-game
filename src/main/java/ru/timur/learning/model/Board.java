package ru.timur.learning.model;

import lombok.Getter;
import org.postgresql.geometric.PGpoint;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.settings.Settings;

import java.util.Arrays;
import java.util.List;

@Getter
public class Board {

    private final ShipsOnBoard shipsOnBoard;

    private final Cell[][] grid;

    {
        this.grid = new Cell[Settings.GRID_SIZE][Settings.GRID_SIZE];
        for (Cell[] row : this.grid) {
            Arrays.fill(row, new Cell(Cell.CellState.FREE, null, null));
        }
    }

    public Board(ShipsOnBoard shipsOnBoard, List<ShipEntity> shipEntities, List<PGpoint> shotsCoordinates) {
        this.shipsOnBoard = shipsOnBoard;
        placeShots(shotsCoordinates);
        placeShips(shipEntities);
    }

    private void placeShots(List<PGpoint> shotsCoordinates) {
        shotsCoordinates.forEach(pGpoint -> {
            assert grid != null;
            grid[(int) pGpoint.x][(int) pGpoint.y] = createMissedCell();
        });
    }

    private Cell createMissedCell() {
        return new Cell(Cell.CellState.MISSED, null, null);
    }

    private void placeShips(List<ShipEntity> shipEntities) {
        for (ShipEntity shipEntity : shipEntities) {
            Boolean isDestroyed = checkShipIsDestroyed(shipEntity.getCoordinates());
            placeShipOnGrid(shipEntity.getCoordinates(), shipEntity.getId(), isDestroyed);
        }
    }

    private Boolean checkShipIsDestroyed(PGpoint[] coordinates) {
        boolean result = true;
        for (PGpoint pGpoint : coordinates) {
            Cell.CellState gridCellState = getGridCellState(pGpoint);

            if (!gridCellState.equals(Cell.CellState.MISSED)) {
                result = false;
                break;
            }
        }
        return result;
    }

    private Cell.CellState getGridCellState(PGpoint pGpoint) {
        return grid[(int) pGpoint.x][(int) pGpoint.y].getCellState();
    }

    private void placeShipOnGrid(PGpoint[] coordinates, Long shipId, Boolean isDestroyed) {
        for (PGpoint pGpoint : coordinates) {
            assert grid != null;

            Cell.CellState gridCellState = getGridCellState(pGpoint);
            Cell newShipCell = new Cell(null, shipId, isDestroyed);

            if (gridCellState.equals(Cell.CellState.MISSED)) {
                newShipCell.setCellState(Cell.CellState.SHIP_HIT);
            } else if (gridCellState.equals(Cell.CellState.FREE)) {
                newShipCell.setCellState(Cell.CellState.SHIP);
            } else {
                throw new IllegalArgumentException("Wrong grid cell state");
            }

            grid[(int) pGpoint.x][(int) pGpoint.y] = newShipCell;
        }
    }

    public ShotEntity.Outcome getShotOutcome(PGpoint pGpoint) {
        Cell.CellState gridCellState = getGridCellState(pGpoint);

        if (gridCellState.equals(Cell.CellState.SHIP)) {
            return ShotEntity.Outcome.HIT;
        } else if (gridCellState.equals(Cell.CellState.FREE)) {
            return ShotEntity.Outcome.MISS;
        } else {
            throw new IllegalArgumentException("Cannot take shot here");
        }
    }

    public Cell[][] getFilteredGrid() {
        Cell[][] result = grid.clone();

        for (int x = 0; x < result.length; x++) {
            for (int y = 0; y < result[x].length; y++) {

                if (grid[x][y].getCellState()
                        .equals(Cell.CellState.SHIP)) {
                    result[x][y] = createFreeCell();
                }
            }
        }
        return result;
    }

    private Cell createFreeCell() {
        return new Cell(Cell.CellState.FREE, null, null);
    }

    public void checkCoordinatesAreFree(PGpoint[] coordinates) {
        Arrays.stream(coordinates).forEach(pGpoint -> {
            Cell.CellState gridCellState = getGridCellState(pGpoint);

            if (!gridCellState.equals(Cell.CellState.FREE)) {
                throw new IllegalArgumentException("Selected coordinates are already taken");
            }
        });
    }

    public void checkCanChangeShipCoordinates(PGpoint[] fromCoordinates, PGpoint[] toCoordinates) {
        for (PGpoint pGpoint : fromCoordinates) {
            grid[(int) pGpoint.x][(int) pGpoint.y] = createFreeCell();
        }
        checkCoordinatesAreFree(toCoordinates);
    }

    public Boolean checkPlayerWon() {
        boolean result = true;
        for (Cell[] cellRow : grid) {
            for (Cell cell : cellRow) {
                Cell.CellState cellState = cell.getCellState();

                if (cellState.equals(Cell.CellState.SHIP)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
}

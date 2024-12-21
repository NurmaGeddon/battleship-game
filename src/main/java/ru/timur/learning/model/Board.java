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

    private final InitiallyPlacedShips initiallyPlacedShips;

    private final Cell[][] grid;

    private static Cell createMissedCell() {
        return new Cell(Cell.CellState.MISSED, null, null);
    }

    private static Cell createFreeCell() {
        return new Cell(Cell.CellState.FREE, null, null);
    }

    {
        this.grid = new Cell[Settings.GRID_SIZE][Settings.GRID_SIZE];
        for (Cell[] row : this.grid) {
            Arrays.fill(row, createFreeCell());
        }
    }

    public Board(InitiallyPlacedShips initiallyPlacedShips, List<ShipEntity> shipEntities, List<PGpoint> shotsCoordinates) {
        this.initiallyPlacedShips = initiallyPlacedShips;
        placeShots(shotsCoordinates);
        placeShips(shipEntities);
    }

    private void placeShots(List<PGpoint> shotsCoordinates) {
        shotsCoordinates.forEach(pGpoint -> {
            assert grid != null;
            grid[(int) pGpoint.x][(int) pGpoint.y] = createMissedCell();
        });
    }

    private void placeShips(List<ShipEntity> shipEntities) {
        for (ShipEntity shipEntity : shipEntities) {
            Boolean isDestroyed = checkShipIsDestroyed(shipEntity.getCoordinates());
            placeShipOnGrid(shipEntity.getCoordinates(), shipEntity.getId(), isDestroyed);
        }
    }

    private Boolean checkShipIsDestroyed(PGpoint[] shipCoordinates) {
        boolean destroyed = true;
        for (PGpoint shipCoordinate : shipCoordinates) {
            if (isShipHit(shipCoordinate)) {
                destroyed = false;
                break;
            }
        }
        return destroyed;
    }

    private Boolean isShipHit(final PGpoint shipCoordinate) {
        Cell.CellState gridCellState = getGridCellState(shipCoordinate);
        return !gridCellState.equals(Cell.CellState.MISSED);
    }

    private Cell.CellState getGridCellState(PGpoint pGpoint) {
        return grid[(int) pGpoint.x][(int) pGpoint.y].getCellState();
    }

    private void placeShipOnGrid(PGpoint[] shipCoordinates, Long shipId, Boolean isDestroyed) {
        for (PGpoint shipCoordinate : shipCoordinates) {
            assert grid != null;

            Cell.CellState shipCellState = getGridCellState(shipCoordinate);
            Cell.CellState newShipCellState;

            if (shipCellState.equals(Cell.CellState.MISSED)) {
                newShipCellState = Cell.CellState.SHIP_HIT;
            } else if (shipCellState.equals(Cell.CellState.FREE)) {
                newShipCellState = Cell.CellState.SHIP;
            } else {
                throw new IllegalArgumentException("Wrong grid cell state");
            }


            Cell newShipCell = new Cell(newShipCellState, shipId, isDestroyed);
            grid[(int) shipCoordinate.x][(int) shipCoordinate.y] = newShipCell;
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
        Cell[][] filteredGrid = grid.clone();

        for (int x = 0; x < filteredGrid.length; x++) {
            for (int y = 0; y < filteredGrid[x].length; y++) {

                if (grid[x][y].getCellState()
                        .equals(Cell.CellState.SHIP)) {
                    filteredGrid[x][y] = createFreeCell();
                }
            }
        }
        return filteredGrid;
    }

    public void checkCoordinatesAreFree(PGpoint[] coordinates) {
        Arrays.stream(coordinates).forEach(pGpoint -> {
            final Cell.CellState gridCellState = getGridCellState(pGpoint);

            if (isCoordinateTaken(gridCellState)) {
                throw new IllegalArgumentException("Selected coordinates are already taken");
            }
        });
    }

    private Boolean isCoordinateTaken(final Cell.CellState gridCellState) {
        return !gridCellState.equals(Cell.CellState.FREE);
    }

    public void checkCanTakeNewCoordinates(PGpoint[] fromCoordinates, PGpoint[] toCoordinates) {
        emptyShipCoordinates(fromCoordinates);
        checkCoordinatesAreFree(toCoordinates);
    }

    private void emptyShipCoordinates(PGpoint[] fromCoordinates) {
        for (PGpoint shipCoordinate : fromCoordinates) {
            grid[(int) shipCoordinate.x][(int) shipCoordinate.y] = createFreeCell();
        }
    }

    public Boolean checkPlayerWon() {
        boolean playerWon = true;
        for (Cell[] cellRow : grid) {
            for (Cell cell : cellRow) {
                Cell.CellState cellState = cell.getCellState();

                if (cellState.equals(Cell.CellState.SHIP)) {
                    playerWon = false;
                    break;
                }
            }
        }
        return playerWon;
    }
}

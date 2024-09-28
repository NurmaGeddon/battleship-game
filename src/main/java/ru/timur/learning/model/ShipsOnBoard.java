package ru.timur.learning.model;

import lombok.Getter;
import ru.timur.learning.model.entity.ShipEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Class used only for ship placement
 */
@Getter
public class ShipsOnBoard {

    // TODO move to separate settings class
    private static final Map<Integer, Integer> SHIP_LENGTH_TO_MAX_NUM_SHIPS = Map.of(
            2, 1, //5
            3, 1, //7
            4, 1, // 2
            5, 1
    );

    private final Map<Integer, Integer> shipLengthToNumShips;

    public ShipsOnBoard(List<ShipEntity> shipEntities) {
        shipLengthToNumShips = new HashMap<>();
        fillShipLengthToNumShips(shipEntities);
    }

    private void fillShipLengthToNumShips(List<ShipEntity> shipEntities) {
        for (ShipEntity ship : shipEntities) {
            Integer shipLength = ship.getCoordinates().length;
            Integer newNumberOfShips = 1;

            if (shipLengthToNumShips.containsKey(shipLength)) {
                newNumberOfShips += shipLengthToNumShips.get(shipLength);
            }

            shipLengthToNumShips.put(shipLength, newNumberOfShips);
        }
    }

    public void checkCanPlaceAnotherShip(int length) {
        Integer numShips = shipLengthToNumShips.get(length);
        if (numShips != null && numShips >= getMaxNumShips(length)) {
            throw new IllegalArgumentException(
                    "Exceeded limit for number of ships with this length");
        }
    }

    private Integer getMaxNumShips(Integer length) {
        if (!SHIP_LENGTH_TO_MAX_NUM_SHIPS.containsKey(length)) {
            throw new IllegalArgumentException("Illegal ship length");
        }
        return SHIP_LENGTH_TO_MAX_NUM_SHIPS.get(length);
    }

    public void checkPlacedAllShips() {
        if (!shipLengthToNumShips.entrySet()
                .equals(SHIP_LENGTH_TO_MAX_NUM_SHIPS.entrySet())) {
            throw new IllegalArgumentException("Player not finished placing ships");
        }
    }
}

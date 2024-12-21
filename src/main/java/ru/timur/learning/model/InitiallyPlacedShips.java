package ru.timur.learning.model;

import lombok.Getter;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.settings.Settings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Class used only for checking ship placement
 */
@Getter
public class InitiallyPlacedShips {

    private final Map<Integer, Integer> shipLengthToNumShips = new HashMap<>();

    public InitiallyPlacedShips(List<ShipEntity> shipEntities) {
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
        if (!Settings.SHIP_LENGTH_TO_MAX_NUM_SHIPS.containsKey(length)) {
            throw new IllegalArgumentException("Illegal ship length");
        }
        return Settings.SHIP_LENGTH_TO_MAX_NUM_SHIPS.get(length);
    }

    public void checkPlacedAllShips() {
        if (!shipLengthToNumShips.entrySet()
                .equals(Settings.SHIP_LENGTH_TO_MAX_NUM_SHIPS.entrySet())) {
            throw new IllegalArgumentException("Player not finished placing ships");
        }
    }
}

package ru.timur.learning.settings;

import java.util.Map;

//TODO move to application.properties
public class Settings {
    public static final Integer GRID_SIZE = 5;

    public static final Map<Integer, Integer> SHIP_LENGTH_TO_MAX_NUM_SHIPS = Map.of(
            2, 1, //5
            3, 1, //7
            4, 1, // 2
            5, 1
    );
}

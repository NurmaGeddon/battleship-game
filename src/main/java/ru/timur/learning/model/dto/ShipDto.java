package ru.timur.learning.model.dto;

import lombok.*;
import org.postgresql.geometric.PGpoint;
import ru.timur.learning.settings.Settings;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Getter
public class ShipDto {
    private final PGpoint[] coordinates;

    public ShipDto(PGpoint[] coordinates) {
        checkShipLength(coordinates);
        checkCoordinatesAreUnique(coordinates);
        checkCoordinatesNotOverBoard(coordinates);
        checkCoordinatesAreInLine(coordinates);

        this.coordinates = coordinates;
    }

    private void checkShipLength(PGpoint[] coordinates) {
        if (coordinates == null || coordinates.length < 2 || coordinates.length > 5) {
            throw new IllegalArgumentException("Trying to place ship with incorrect length");
        }
    }

    private void checkCoordinatesNotOverBoard(PGpoint[] coordinates) {
        Arrays.stream(coordinates).forEach(pGpoint -> {
            if (pGpoint.isNull() || checkIncorrectnessOfCoordinate(pGpoint)) {
                throw new IllegalArgumentException("Ship coordinates are incorrect");
            }
        });
    }

    private boolean checkIncorrectnessOfCoordinate(PGpoint pGpoint) {
        return pGpoint.x < 0 || pGpoint.x + 1 > Settings.GRID_SIZE ||
                pGpoint.y < 0 || pGpoint.y + 1 > Settings.GRID_SIZE;
    }

    private void checkCoordinatesAreUnique(PGpoint[] coordinates) {
        Integer expected = coordinates.length;
        Set<PGpoint> pGpointSet = Arrays.stream(coordinates).collect(Collectors.toSet());
        Integer actual = pGpointSet.size();

        if (!expected.equals(actual)) {
            throw new IllegalArgumentException("Ship coordinates are duplicating");
        }
    }

    private void checkCoordinatesAreInLine(PGpoint[] coordinates) {
        SortedSet<Integer> setOfX = new TreeSet<Integer>();
        SortedSet<Integer> setOfY = new TreeSet<Integer>();

        fillSets(coordinates, setOfX, setOfY);
        SortedSet<Integer> differentNumbers = checkOneAxisInline(setOfX, setOfY);
        checkSetIncrementsByOne(differentNumbers);
    }

    private void fillSets(PGpoint[] coordinates, SortedSet<Integer> setOfX, SortedSet<Integer> setOfY) {
        Arrays.stream(coordinates).forEach(pGpoint -> {
            setOfX.add((int) pGpoint.x);
            setOfY.add((int) pGpoint.y);
        });
    }

    private SortedSet<Integer> checkOneAxisInline(SortedSet<Integer> setOfX, SortedSet<Integer> setOfY) {
        if (setOfX.size() == 1) {
            return setOfY;
        } else if (setOfY.size() == 1) {
            return setOfX;
        } else {
            throw new IllegalArgumentException("Ship coordinates are not in line");
        }
    }

    private void checkSetIncrementsByOne(SortedSet<Integer> differentNumbers) {
        var integerIterator = differentNumbers.iterator();

        for (Integer i = differentNumbers.first(); integerIterator.hasNext(); i++) {
            Integer nextSetElement = integerIterator.next();
            if (!i.equals(nextSetElement)) {
                throw new IllegalArgumentException("Ship coordinates are not in line");
            }
        }
    }
}

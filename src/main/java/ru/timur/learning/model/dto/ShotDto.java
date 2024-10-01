package ru.timur.learning.model.dto;

import lombok.*;
import org.postgresql.geometric.PGpoint;
import ru.timur.learning.settings.Settings;

@Getter
public class ShotDto {
    private final PGpoint pGpoint;

    public ShotDto(PGpoint pGpoint) {
        checkCorrectnessOfCoordinate(pGpoint);
        this.pGpoint = pGpoint;
    }

    private void checkCorrectnessOfCoordinate(PGpoint pGpoint) {
        if (pGpoint.x < 0 || pGpoint.x + 1 > Settings.GRID_SIZE ||
                pGpoint.y < 0 || pGpoint.y + 1 > Settings.GRID_SIZE) {
            throw new IllegalArgumentException("Shot coordinate is out of board");
        }
    }
}

package ru.timur.learning.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.postgresql.geometric.PGpoint;

@Data
@AllArgsConstructor
public class ShotEntity {
    public enum Outcome {
        HIT,
        MISS
    }

    private Long gameId;

    private Integer shotNum;

    private Integer playerNumber;

    private PGpoint coordinate;

    private Outcome outcome;
}

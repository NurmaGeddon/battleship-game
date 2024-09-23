package ru.timur.learning.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;

@Data
@RequiredArgsConstructor
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

package ru.timur.learning.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.postgresql.geometric.PGpoint;

@Data
@AllArgsConstructor
public class ShipEntity {
    private Long id;

    private Long gameId;

    private Integer playerNumber;

    private PGpoint[] coordinates;
}

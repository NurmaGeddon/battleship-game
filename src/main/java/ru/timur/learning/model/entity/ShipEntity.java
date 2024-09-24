package ru.timur.learning.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.postgresql.geometric.PGpoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ShipEntity {
    private Long id;

    private Long gameId;

    private Integer playerNumber;

    private PGpoint[] coordinates;
}

package ru.timur.learning.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.geometric.PGpoint;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShotDto {
    private PGpoint pGpoint;
}

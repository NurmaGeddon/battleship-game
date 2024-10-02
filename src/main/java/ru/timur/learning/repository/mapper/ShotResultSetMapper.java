package ru.timur.learning.repository.mapper;

import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Component;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.repository.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ShotResultSetMapper implements ResultSetMapper<ShotEntity> {
    @Override
    public List<ShotEntity> parseObjects(ResultSet resultSet) throws SQLException {
        List<ShotEntity> shots = new ArrayList<>();
        while (resultSet.next()) {
            ShotEntity shotEntity = this.parseObject(resultSet);
            shots.add(shotEntity);
        }
        return shots;
    }

    @Override
    public ShotEntity parseObject(ResultSet row) throws SQLException {
        return new ShotEntity(
                (Long) row.getObject(1),
                (Integer) row.getObject(2),
                (Integer) row.getObject(3),
                (PGpoint) row.getObject(4),
                ShotEntity.Outcome.valueOf(row.getString(5))
        );

    }
}

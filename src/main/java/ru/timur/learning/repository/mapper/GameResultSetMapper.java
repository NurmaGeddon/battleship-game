package ru.timur.learning.repository.mapper;

import org.springframework.stereotype.Component;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.repository.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GameResultSetMapper implements ResultSetMapper<GameEntity> {

    @Override
    public GameEntity parseObject(ResultSet row) throws SQLException {
        return new GameEntity(
                (Long) row.getObject(1),
                (Long) row.getObject(2),
                (Long) row.getObject(3),
                (Boolean) row.getObject(4),
                (Boolean) row.getObject(5),
                GameEntity.GameState.valueOf(row.getString(6)),
                row.getLong(7)
                );
    }
}

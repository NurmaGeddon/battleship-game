package ru.timur.learning.repository.mapper;

import org.springframework.stereotype.Component;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.repository.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GameResultSetMapper implements ResultSetMapper<GameEntity> {
    @Override
    public List<GameEntity> parseObjects(ResultSet resultSet) throws SQLException {
        List<GameEntity> games = new ArrayList<>();
        while (resultSet.next()) {
            GameEntity game = this.parseObject(resultSet);
            games.add(game);
        }
        return games;
    }

    @Override
    public GameEntity parseObject(ResultSet row) throws SQLException {
        return new GameEntity(
                row.getLong(1),
                row.getLong(2),
                row.getLong(3),
                row.getBoolean(4),
                row.getBoolean(5),
                GameEntity.GameState.valueOf(row.getString(6)),
                row.getLong(7)
        );

    }
}

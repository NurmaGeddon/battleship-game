package ru.timur.learning.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.timur.learning.exception.InternalServerErrorException;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.repository.ResultSetMapper;
import ru.timur.learning.repository.ShipRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ShipRepositoryImpl implements ShipRepository {

    //language=SQL
    private static final String SQL_INSERT = "insert into ship(game_id, player_number, coordinates) " +
            "values (?, ?, ?) returning id, game_id, player_number, coordinates";

    //language=SQL
    private static final String SQL_FIND_BY_ID = "select * from ship where id=?";

    //language=SQL
    private static final String SQL_FIND_ALL_BY_GAME_ID = "select * from ship where game_id=?";

    //language=SQL
    private static final String SQL_FIND_ALL = "select * from ship order by id";

    //language=SQL
    private static final String SQL_UPDATE = "update ship " +
            "set game_id=?, player_number=?, coordinates=? " +
            "where id=? " +
            "returning id, game_id, player_number, coordinates";

    //language=SQL
    private static final String SQL_DELETE = "delete from ship where id=?";

    //language=SQL
    private static final String SQL_DELETE_ALL = "truncate table ship cascade";

    private final DataSource dataSource;

    private final ResultSetMapper<ShipEntity> shipResultSetMapper;

    @Override
    public ShipEntity save(ShipEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setLong(1, entity.getGameId());
            statement.setInt(2, entity.getPlayerNumber());
            Array coordinates = connection.createArrayOf("point", entity.getCoordinates());
            statement.setArray(3, coordinates);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? shipResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public ShipEntity findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? shipResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<ShipEntity> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            return shipResultSetMapper.parseObjects(resultSet);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public ShipEntity update(ShipEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setLong(1, entity.getGameId());
            statement.setInt(2, entity.getPlayerNumber());
            Array coordinates = connection.createArrayOf("point", entity.getCoordinates());
            statement.setArray(3, coordinates);
            statement.setLong(4, entity.getId());

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? shipResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, id);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_ALL)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<ShipEntity> findAllForGame(Long gameId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_GAME_ID)) {

            statement.setLong(1, gameId);
            ResultSet resultSet = statement.executeQuery();

            return shipResultSetMapper.parseObjects(resultSet);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}

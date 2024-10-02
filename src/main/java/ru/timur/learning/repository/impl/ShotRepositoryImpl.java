package ru.timur.learning.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.timur.learning.exception.InternalServerErrorException;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.repository.ResultSetMapper;
import ru.timur.learning.repository.ShotRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShotRepositoryImpl implements ShotRepository {
    //language=SQL
    private static final String SQL_INSERT = "insert into shot(game_id, " +
            "shot_num, player_number, coordinate, outcome) " +
            "values (?, ?, ?, point(?, ?), ?) returning game_id, shot_num, player_number, coordinate, outcome";

    //language=SQL
    private static final String SQL_FIND_BY_KEY = "select * from shot where game_id=? and shot_num=?";

    //language=SQL
    private static final String SQL_FIND_ALL = "select * from shot order by game_id, shot_num";

    //language=SQL
    private static final String SQL_DELETE = "delete from shot where game_id=? and shot_num=?";

    //language=SQL
    private static final String SQL_DELETE_ALL = "truncate table shot cascade";

    //language=SQL
    private static final String SQL_GET_SHOTS_FOR_PLAYER = "select * from shot " +
            "where game_id=? and player_number=?";

    private final DataSource dataSource;

    private final ResultSetMapper<ShotEntity> shotResultSetMapper;

    @Override
    public ShotEntity save(ShotEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setLong(1, entity.getGameId());
            statement.setInt(2, entity.getShotNum());
            statement.setLong(3, entity.getPlayerNumber());
            statement.setDouble(4, entity.getCoordinate().x);
            statement.setDouble(5, entity.getCoordinate().y);
            statement.setString(6, entity.getOutcome().toString());

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? shotResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public ShotEntity findByKey(Long gameId, Integer shotNumber) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_KEY)) {

            statement.setLong(1, gameId);
            statement.setInt(2, shotNumber);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? shotResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<ShotEntity> findAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);

            return shotResultSetMapper.parseObjects(resultSet);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public boolean deleteByKey(Long gameId, Integer shotNum) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {

            statement.setLong(1, gameId);
            statement.setInt(2, shotNum);

            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<ShotEntity> findShotsForPlayer(Long gameId, Integer playerNumber) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_GET_SHOTS_FOR_PLAYER)) {

            statement.setLong(1, gameId);
            statement.setInt(2, playerNumber);
            ResultSet resultSet = statement.executeQuery();

            return shotResultSetMapper.parseObjects(resultSet);
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
}

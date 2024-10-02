package ru.timur.learning.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.timur.learning.exception.InternalServerErrorException;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.repository.GameRepository;
import ru.timur.learning.repository.ResultSetMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class GameRepositoryImpl implements GameRepository {

    //language=SQL
    private static final String SQL_INSERT = "insert into game(player1_id) " +
            "values (?) returning " +
            "id, player1_id, player2_id, player1_ready, player2_ready, state, winner_id";

    //language=SQL
    private static final String SQL_FIND_BY_ID = "select * from game where id=?";

    //language=SQL
    private static final String SQL_FIND_ALL = "select * from game order by id";

    //language=SQL
    private static final String SQL_UPDATE = "update game " +
            "set player1_id=?, player2_id=?, player1_ready=?, player2_ready=?, state=?, winner_id=? " +
            "where id=? " +
            "returning id, player1_id, player2_id, player1_ready, player2_ready, state, winner_id";
 
    //language=SQL
    private static final String SQL_DELETE = "delete from game where id=?";

    //language=SQL
    private static final String SQL_DELETE_ALL = "truncate table game cascade";

    private final DataSource dataSource;

    private final ResultSetMapper<GameEntity> gameEntityResultSetMapper;

    @Override
    public GameEntity save(GameEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setLong(1, entity.getPlayer1Id());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? gameEntityResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public GameEntity findById(Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? gameEntityResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<GameEntity> findAll() {
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            return gameEntityResultSetMapper.parseObjects(resultSet);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public GameEntity update(GameEntity entity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setLong(1, entity.getPlayer1Id());
            statement.setLong(2, entity.getPlayer2Id());
            statement.setBoolean(3, entity.getPlayer1Ready());
            statement.setBoolean(4, entity.getPlayer2Ready());
            statement.setString(5, entity.getGameState().toString());
            statement.setLong(6, entity.getWinnerId());
            statement.setLong(7, entity.getId());

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? gameEntityResultSetMapper.parseObject(resultSet)
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
}

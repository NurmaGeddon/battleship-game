package ru.timur.learning.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.timur.learning.exception.InternalServerErrorException;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.ResultSetMapper;
import ru.timur.learning.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    //language=SQL
    private static final String SQL_INSERT = "insert into account(login, password) " +
            "values (?, ?) returning id, login, password";

    //language=SQL
    private static final String SQL_FIND_BY_ID = "select * from account where id=?";

    //language=SQL
    private static final String SQL_FIND_BY_LOGIN = "select * from account where login=?";

    //language=SQL
    private static final String SQL_FIND_ALL = "select * from account order by id";

    //language=SQL
    private static final String SQL_UPDATE = "update account " +
            "set login=?, password=? " +
            "where id=? returning id, login, password";

    //language=SQL
    private static final String SQL_DELETE = "delete from account where id=?";

    //language=SQL
    private static final String SQL_DELETE_ALL = "truncate table account cascade";

    private final DataSource dataSource;

    private final ResultSetMapper<User> userResultSetMapper;

    @Override
    public User save(User user) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? userResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public User findById(Long id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? userResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL);

            return userResultSetMapper.parseObjects(resultSet);
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public User update(User user) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? userResultSetMapper.parseObject(resultSet)
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
    public User findByEmail(String login) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? userResultSetMapper.parseObject(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}

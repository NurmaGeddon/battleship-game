package ru.timur.learning.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.timur.learning.exception.InternalServerErrorException;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.UserRepository;
import ru.timur.learning.repository.mapper.UserResultSetMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

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
    private static final String SQL_SELECT_ALL = "select * from account order by id";

    //language=SQL
    private static final String SQL_UPDATE = "update account " +
            "set login=?, password=? " +
            "where id=? returning id, login, password";

    //language=SQL
    private static final String SQL_DELETE = "delete from account where id=?";

    private final DataSource dataSource;

    @Override
    public User save(User user) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? UserResultSetMapper.parseUser(resultSet)
                    : null;
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? Optional.of(UserResultSetMapper.parseUser(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }

    @Override
    public List<User> findAll() {

        try (Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL);

            return UserResultSetMapper.parseUsers(resultSet);
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
                    ? UserResultSetMapper.parseUser(resultSet)
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
    public Optional<User> findByEmail(String login) {
        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_LOGIN)) {

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next()
                    ? Optional.of(UserResultSetMapper.parseUser(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new InternalServerErrorException(e);
        }
    }
}

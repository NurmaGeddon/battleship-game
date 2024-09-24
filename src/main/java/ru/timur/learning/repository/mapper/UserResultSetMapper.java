package ru.timur.learning.repository.mapper;

import org.springframework.stereotype.Component;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserResultSetMapper implements ResultSetMapper<User> {

    @Override
    public List<User> parseObjects(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = this.parseObject(resultSet);
            users.add(user);
        }
        return users;
    }

    @Override
    public User parseObject(ResultSet row) throws SQLException{
        return new User(
                row.getLong(1),
                row.getString(2),
                row.getString(3)
        );
    }
}

package ru.timur.learning.repository.mapper;

import ru.timur.learning.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserResultSetMapper {
    public static List<User> parseUsers(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = UserResultSetMapper.parseUser(resultSet);
            users.add(user);
        }
        return users;
    }

    public static User parseUser(ResultSet row) {
        try {
            return new User(
                    row.getLong(1),
                    row.getString(2),
                    row.getString(3)
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error parsing user from ResultSet", e);
        }
    }
}

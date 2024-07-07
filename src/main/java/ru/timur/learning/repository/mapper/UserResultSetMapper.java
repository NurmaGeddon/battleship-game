package ru.timur.learning.repository.mapper;

import ru.timur.learning.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResultSetMapper {
    public User parse(ResultSet row) {
        try {
            return new User(
                    row.getLong(1),
                    row.getString(2),
                    row.getString(3),
                    row.getLong(4)
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error parsing user from ResultSet", e);
        }
    }
}

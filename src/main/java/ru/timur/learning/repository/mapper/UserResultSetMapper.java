package ru.timur.learning.repository.mapper;

import org.springframework.stereotype.Component;
import ru.timur.learning.model.User;
import ru.timur.learning.repository.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserResultSetMapper implements ResultSetMapper<User> {

    @Override
    public User parseObject(ResultSet row) throws SQLException{
        return new User(
                (Long) row.getObject(1),
                row.getString(2),
                row.getString(3)
        );
    }
}

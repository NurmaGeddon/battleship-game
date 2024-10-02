package ru.timur.learning.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ResultSetMapper<T> {
    List<T> parseObjects(ResultSet resultSet) throws SQLException;

    T parseObject(ResultSet row) throws SQLException;
}

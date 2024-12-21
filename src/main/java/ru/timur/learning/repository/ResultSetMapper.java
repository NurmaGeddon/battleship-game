package ru.timur.learning.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ResultSetMapper<T> {
    default List<T> parseObjects(ResultSet resultSet) throws SQLException {
        List<T> entities = new ArrayList<>();
        while (resultSet.next()) {
            T entity = this.parseObject(resultSet);
            entities.add(entity);
        }
        return entities;
    }

    T parseObject(ResultSet row) throws SQLException;
}

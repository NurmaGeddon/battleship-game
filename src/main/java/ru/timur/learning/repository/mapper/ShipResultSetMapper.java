package ru.timur.learning.repository.mapper;

import org.postgresql.geometric.PGpoint;
import org.springframework.stereotype.Component;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.repository.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ShipResultSetMapper implements ResultSetMapper<ShipEntity> {

    @Override
    public List<ShipEntity> parseObjects(ResultSet resultSet) throws SQLException {
        List<ShipEntity> shipEntities = new ArrayList<>();
        while (resultSet.next()) {
            ShipEntity ship = this.parseObject(resultSet);
            shipEntities.add(ship);
        }
        return shipEntities;
    }

    @Override
    public ShipEntity parseObject(ResultSet row) throws SQLException {
        Object[] objects = (Object []) row.getArray(4).getArray();
        PGpoint[] pGPoints = Arrays.stream(objects).map(ob -> (PGpoint) ob).toArray(PGpoint[]::new);
        return new ShipEntity(
                row.getLong(1),
                row.getLong(2),
                row.getInt(3),
                pGPoints
        );
    }
}

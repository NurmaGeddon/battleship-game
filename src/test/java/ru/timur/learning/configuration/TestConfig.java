package ru.timur.learning.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.timur.learning.model.User;
import ru.timur.learning.model.entity.GameEntity;
import ru.timur.learning.model.entity.ShipEntity;
import ru.timur.learning.model.entity.ShotEntity;
import ru.timur.learning.repository.*;
import ru.timur.learning.repository.impl.GameRepositoryImpl;
import ru.timur.learning.repository.impl.ShipRepositoryImpl;
import ru.timur.learning.repository.impl.ShotRepositoryImpl;
import ru.timur.learning.repository.impl.UserRepositoryImpl;
import ru.timur.learning.repository.mapper.GameResultSetMapper;
import ru.timur.learning.repository.mapper.ShipResultSetMapper;
import ru.timur.learning.repository.mapper.ShotResultSetMapper;
import ru.timur.learning.repository.mapper.UserResultSetMapper;

import javax.sql.DataSource;

@PropertySource(value = "classpath:testApplication.properties")
@Configuration
public class TestConfig {

    @Autowired
    private Environment environment;

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(environment.getProperty("db.url"));
        hikariDataSource.setUsername(environment.getProperty("db.username"));
        hikariDataSource.setPassword(environment.getProperty("db.password"));
        hikariDataSource.setMaximumPoolSize(environment.getProperty("db.hikari.MaxPoolSize", Integer.class));

        return hikariDataSource;
    }

    @Bean
    public UserRepository getUserRepository(DataSource dataSource, ResultSetMapper<User> userResultSetMapper) {
        return new UserRepositoryImpl(dataSource, userResultSetMapper);
    }

    @Bean
    public GameRepository getGameRepository(DataSource dataSource, ResultSetMapper<GameEntity> gameEntityResultSetMapper) {
        return new GameRepositoryImpl(dataSource, gameEntityResultSetMapper);
    }

    @Bean
    public ShipRepository getShipRepository(DataSource dataSource, ResultSetMapper<ShipEntity> shipEntityResultSetMapper) {
        return new ShipRepositoryImpl(dataSource, shipEntityResultSetMapper);
    }

    @Bean
    public ShotRepository getShotRepository(DataSource dataSource, ResultSetMapper<ShotEntity> shotEntityResultSetMapper) {
        return new ShotRepositoryImpl(dataSource, shotEntityResultSetMapper);
    }

    @Bean
    public ResultSetMapper<User> getUserResultSetMapper() {
        return new UserResultSetMapper();
    }

    @Bean
    public ResultSetMapper<GameEntity> getGameResultSetMapper() {
        return new GameResultSetMapper();
    }

    @Bean
    public ResultSetMapper<ShipEntity> getShipResultSetMapper() {
        return new ShipResultSetMapper();
    }

    @Bean
    public ResultSetMapper<ShotEntity> getShotResultSetMapper() {
        return new ShotResultSetMapper();
    }
}

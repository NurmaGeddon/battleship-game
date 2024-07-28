package ru.timur.learning.repository.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@PropertySource(value = "classpath:application.properties")
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
}

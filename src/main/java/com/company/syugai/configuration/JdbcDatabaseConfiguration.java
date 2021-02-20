package com.company.syugai.configuration;

import com.company.syugai.exception.ApplicationException;
import com.company.syugai.model.Game;
import com.company.syugai.model.User;
import com.company.syugai.model.UserGame;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class JdbcDatabaseConfiguration implements DatabaseConfiguration{
    private final ConnectionSource connectionSource;

    public JdbcDatabaseConfiguration(String jdbcConnectionString){
        try {
            connectionSource = new JdbcConnectionSource(jdbcConnectionString);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Game.class);
            TableUtils.createTableIfNotExists(connectionSource, UserGame.class);
        }catch (SQLException throwable){
            throwable.printStackTrace();
            throw new ApplicationException("Can not initialize database connection", throwable);
        }
    }

    @Override
    public ConnectionSource connectionSource(){
        return connectionSource;
    }
}

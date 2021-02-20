package com.company.syugai.service;

import com.company.syugai.exception.ApplicationException;
import com.company.syugai.model.Model;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public interface Service<T extends Model> {
    Dao<T, Integer> dao();

    default List<T> findAll(){
        try {
            return dao().queryForAll();
        }catch (SQLException throwable){
            throwable.printStackTrace();
            throw new ApplicationException("SQL exception occurred", throwable);
        }
    }

    default T findById(int id){
        try {
            return dao().queryForId(id);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new ApplicationException("SQL exception occurred", throwable);
        }
    }

    default List<T> findBy(String columnName, Object object){
        try {
            return dao().queryForEq(columnName, object);
        }catch (SQLException throwable){
            throwable.printStackTrace();
            throw new ApplicationException("SQL exception occurred", throwable);
        }
    }

    default void save(T model){
        try {
            dao().create(model);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new ApplicationException("SQL exception occurred", throwables);
        }
    }

    default void update(T model){
        try {
            dao().update(model);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new ApplicationException("SQL exception occurred", throwables);
        }
    }

    default void delete(T model){
        try {
            dao().delete(model);
        }catch (SQLException throwables){
            throwables.printStackTrace();
            throw new ApplicationException("SQL exception occurred", throwables);
        }
    }
}

package com.company.syugai.service;

import com.company.syugai.model.UserGame;
import com.j256.ormlite.dao.Dao;

public class UserGameService extends AbstractService<UserGame>{
    public UserGameService(Dao<UserGame, Integer> dao){
        super(dao);
    }
}

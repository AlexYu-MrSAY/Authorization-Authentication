package com.company.syugai.json.deserizlization;

import com.company.syugai.model.Game;
import com.company.syugai.model.User;
import com.company.syugai.model.UserGame;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.j256.ormlite.dao.Dao;

import java.io.IOException;
import java.sql.SQLException;

public class LibraryDeserialization extends StdDeserializer<UserGame>{
    private final Dao<User, Integer> userDao;
    private final Dao<Game, Integer> gameDao;

    public LibraryDeserialization(Dao<User, Integer> userDao, Dao<Game, Integer> gameDao){
        super(UserGame.class);
        this.gameDao = gameDao;
        this.userDao = userDao;
    }

    @Override
    public UserGame deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode root = p.getCodec().readTree(p);
        int id = root.get(UserGame.FIELD_ID).asInt();
        int user_Id = root.get(UserGame.USER_ID).asInt();
        int game_Id = root.get(UserGame.GAME_ID).asInt();
        User user = null;
        Game game = null;
        try {
            user = userDao.queryForId(user_Id);
            game = gameDao.queryForId(game_Id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new UserGame(id, user, game);
    }
}

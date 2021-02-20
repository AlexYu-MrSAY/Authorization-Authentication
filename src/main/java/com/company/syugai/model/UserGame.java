package com.company.syugai.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Objects;

@DatabaseTable(tableName = "Libraries")
public class UserGame implements Model{
    @DatabaseField( generatedId = true)
    private int id;
    @DatabaseField(columnName = "User_Id", foreign = true, foreignAutoRefresh = true)
    private User user;
    @DatabaseField(columnName = "Game_Id", foreign = true, foreignAutoRefresh = true)
    private Game game;

    public UserGame(int id, User user, Game game) {
        this.id = id;
        this.user = user;
        this.game = game;
    }

    public UserGame() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public static final String FIELD_ID = "id";
    public static final String USER_ID = "User_Id";
    public static final String GAME_ID = "Game_Id";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGame userGame = (UserGame) o;
        return id == userGame.id && Objects.equals(user, userGame.user) && Objects.equals(game, userGame.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, game);
    }}

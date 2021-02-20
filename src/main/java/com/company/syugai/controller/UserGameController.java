package com.company.syugai.controller;

import com.company.syugai.model.Game;
import com.company.syugai.model.User;
import com.company.syugai.model.UserGame;
import com.company.syugai.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserGameController extends AbstractController<UserGame>{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserGameController.class);
    private Dao<User, Integer> userDao;
    private Dao<UserGame,Integer> libraryDao;
    private Dao<Game, Integer> gameDao;

    public UserGameController(Service<UserGame> service, ObjectMapper objectMapper, Dao<User, Integer> userDao, Dao<UserGame, Integer> libraryDao, Dao<Game, Integer> gameDao){
        super(service, objectMapper, UserGame.class);
        this.userDao = userDao;
        this.libraryDao = libraryDao;
        this.gameDao = gameDao;
    }

    @Override
    public void getAll(Context context) {
        try{
            String password = context.basicAuthCredentials().getPassword();
            String username = context.basicAuthCredentials().getUsername();
            List<User> users = userDao.queryForAll();
            List<Game> games = new ArrayList<>();
            for(int i = 0; i < users.size(); i++){
                User user  = users.get(i);
                if(BCrypt.checkpw(password,user.getPassword()) && user.getLogin().equals(username)){
                    List<UserGame> gamesId = libraryDao.queryBuilder().where().eq("User_Id", user.getId()).query();
                    for(int j = 0; j < gamesId.size(); j++){
                        games.add(gamesId.get(j).getGame());
                    }
                }
            }
            context.result(getObjectMapper().writeValueAsString(games));
        }catch (Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    @Override
    public void getOne(Context context, int id) {
        try{
            String password = context.basicAuthCredentials().getPassword();
            String username = context.basicAuthCredentials().getUsername();
            List<User> users = userDao.queryForAll();
            Game game = null;
            for(int i = 0; i < users.size(); i++){
                User user  = users.get(i);
                if(BCrypt.checkpw(password, user.getPassword()) && user.getLogin().equals(username)){
                    List<UserGame> libraries = libraryDao.queryForAll();
                    game = findTheGame(user, libraries, id);
                }

            }
            if(game != null){
                context.result(getObjectMapper().writeValueAsString(game));
            } else{
                context.status(403).result("Unauthorized");
            }
        }catch (Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    public Game findTheGame(User user, List<UserGame> libraries, int gameId){
        Game game = null;
        for(int j = 0; j < libraries.size(); j++){
            if(user.getId() == libraries.get(j).getUser().getId() && gameId == libraries.get(j).getGame().getId()){
                game = libraries.get(j).getGame();
            }
        }
        return game;
    }

    @Override
    public void post(Context context) {
        try {
            UserGame library = getObjectMapper().readValue(context.body(), getClazz());
            getService().save(library);
            UserGame saved = getService().findById(library.getId());
            context.result(getObjectMapper().writeValueAsString(saved));
            context.status(201);
        }catch (Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }
}
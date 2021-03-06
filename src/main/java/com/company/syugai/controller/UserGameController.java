package com.company.syugai.controller;

import com.company.syugai.model.Game;
import com.company.syugai.model.User;
import com.company.syugai.model.UserGame;
import com.company.syugai.model.UserRole;
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
            List<Game> games = new ArrayList<>();
            User user = userDao.queryBuilder().where().eq(User.LOGIN, username).queryForFirst();
            if(BCrypt.checkpw(password,user.getPassword())){
                List<UserGame> gamesId = libraryDao.queryBuilder().where().eq("User_Id", user.getId()).query();
                for(int j = 0; j < gamesId.size(); j++){
                    games.add(gamesId.get(j).getGame());
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
        try {
            String password = context.basicAuthCredentials().getPassword();
            String username = context.basicAuthCredentials().getUsername();
            Game game = null;
            User user = userDao.queryBuilder().where().eq(User.LOGIN, username).queryForFirst();
            if(BCrypt.checkpw(password, user.getPassword()) || user.getRole() == UserRole.ADMIN) {
                UserGame library = libraryDao.queryBuilder().where().eq(UserGame.USER_ID, user.getId()).and().eq(UserGame.GAME_ID, id).queryForFirst();
                if(library != null) {
                    game = library.getGame();
                    context.result(getObjectMapper().writeValueAsString(game));
                }
                else{
                    context.status(404).result("Not found");
                }
            }
            else{
                context.status(403).result("Unauthorized");
            }
        }
        catch(Exception e){
                e.printStackTrace();
                context.status(500);
        }
    }

    @Override
    public void post(Context context) {
        try {
            String username = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            UserGame library = getObjectMapper().readValue(context.body(), getClazz());
            int id = context.pathParam("User_id", Integer.class).get();
            User user = userDao.queryBuilder().where().eq(User.LOGIN, username).queryForFirst();
            if((BCrypt.checkpw(password, user.getPassword()) && user.getId() == id) || (user.getRole() == UserRole.ADMIN)) {
                getService().save(library);
                UserGame saved = getService().findById(library.getId());
                context.result(getObjectMapper().writeValueAsString(saved));
                context.status(201);
            } else{
                context.status(403).result("Unauthorized");
            }
        }catch (Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }

    @Override
    public void delete(Context context, int id) {
        try {
            String username = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            User user = userDao.queryBuilder().where().eq(User.LOGIN, username).queryForFirst();
            UserGame library = getService().findById(id);
            if((BCrypt.checkpw(password, user.getPassword()) && library.getUser().equals(user)) || (BCrypt.checkpw(password, user.getPassword()) && user.getRole() == UserRole.ADMIN)) {
                    getService().delete(library);
                    context.status(201);
            } else{
                context.status(403).result("Unauthorized");
            }
        }catch (Exception e){
            e.printStackTrace();
            context.status(500);
        }
    }
}

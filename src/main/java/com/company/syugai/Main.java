package com.company.syugai;

import com.company.syugai.configuration.DatabaseConfiguration;
import com.company.syugai.configuration.JdbcDatabaseConfiguration;
import com.company.syugai.controller.Controller;
import com.company.syugai.controller.GameController;
import com.company.syugai.controller.UserController;
import com.company.syugai.controller.UserGameController;
import com.company.syugai.json.deserizlization.GameDeserialization;
import com.company.syugai.json.deserizlization.LibraryDeserialization;
import com.company.syugai.json.deserizlization.UserDeserialization;
import com.company.syugai.json.serialization.GameSerialization;
import com.company.syugai.json.serialization.UserSerialization;
import com.company.syugai.model.Game;
import com.company.syugai.model.UserRole;
import com.company.syugai.model.User;
import com.company.syugai.model.UserGame;
import com.company.syugai.service.GameService;
import com.company.syugai.service.Service;
import com.company.syugai.service.UserGameService;
import com.company.syugai.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import static com.company.syugai.model.UserRole.*;
import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.core.security.SecurityUtil.roles;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws SQLException {
        DatabaseConfiguration configuration = new JdbcDatabaseConfiguration("jdbc:sqlite:C:\\Users\\yugai\\OneDrive\\Документы\\BackendGameUserPassword.db");
        Dao<User, Integer> userDao = DaoManager.createDao(configuration.connectionSource(), User.class);
        Dao<Game, Integer> gameDao = DaoManager.createDao(configuration.connectionSource(), Game.class);
        Dao<UserGame, Integer> libraryDao = DaoManager.createDao(configuration.connectionSource(), UserGame.class);
        Service<User> userService = new UserService(userDao);
        Service<Game> gameService = new GameService(gameDao);
        Service<UserGame> libraryService = new UserGameService(libraryDao);
        SimpleModule simpleModule = new SimpleModule()
                .addSerializer(new UserSerialization())
                .addSerializer(new GameSerialization())
                .addDeserializer(User.class, new UserDeserialization())
                .addDeserializer(UserGame.class, new LibraryDeserialization(userDao, gameDao))
                .addDeserializer(Game.class, new GameDeserialization());
        ObjectMapper objectMapper = new ObjectMapper().registerModule(simpleModule);
        Controller<User> userController = new UserController(userService, objectMapper, userDao);
        Controller<Game> gameController = new GameController(gameService, objectMapper);
        Controller<UserGame> libraryController = new UserGameController(libraryService, objectMapper, userDao, libraryDao, gameDao);
        Javalin app = Javalin.create(javalinConfig -> {
            javalinConfig.prefer405over404 = true;
            javalinConfig.enableCorsForAllOrigins();
            javalinConfig.accessManager((handler, ctx, permittedRoles) -> {
                UserRole userRole = getUserRole(ctx, userDao);
                if (permittedRoles.contains(userRole)) {
                    handler.handle(ctx);
                } else {
                    LOGGER.error("Unpermitted role - " + String.valueOf(userRole));
                    ctx.status(403).result("Unauthorized");
                }
            });
        });

        app.routes(() -> {
            path("users", () -> {
                get(userController::getAll, roles(ADMIN));
                post(userController::post, roles(ADMIN, COMMON, ANONYMOUS));

                path(":id", () -> {
                    get(ctx -> userController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN, COMMON));
                    patch(ctx -> userController.patch(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN, COMMON));
                    delete(ctx -> userController.delete(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN, COMMON));
                });
            });
            path("games", () -> {
                get(gameController::getAll, roles(ADMIN, COMMON));
                post(gameController::post, roles(ADMIN));

                path(":id", () -> {
                    get(ctx -> gameController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN, COMMON));
                    patch(ctx -> gameController.patch(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN));
                    delete(ctx -> gameController.delete(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN));
                });
            });
            path("owngames", () -> {
                get(libraryController::getAll, roles(ADMIN, COMMON));
                post(libraryController::post, roles(ADMIN, COMMON));

                path(":id", () -> {
                    get(ctx -> libraryController.getOne(ctx, ctx.pathParam("id", Integer.class).get()), roles(ADMIN, COMMON));
                });
            });
        });

        app.start(7000);
    }

    public static UserRole getUserRole(Context context, Dao<User, Integer> dao){
        UserRole role = ANONYMOUS;
        if (context.basicAuthCredentialsExist()) {
            String username = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            try {
                List<User> users = dao.queryForAll();
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getLogin().equals(username) && BCrypt.checkpw(password, users.get(i).getPassword())) {
                        role = users.get(i).getRole();
                        LOGGER.info(String.valueOf(role));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            LOGGER.info(username);
            LOGGER.info(password);
        }
        return role;
    }
}
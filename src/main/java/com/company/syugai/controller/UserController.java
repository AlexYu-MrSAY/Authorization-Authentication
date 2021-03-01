package com.company.syugai.controller;

import com.company.syugai.model.User;
import com.company.syugai.model.UserRole;
import com.company.syugai.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class UserController extends AbstractController<User>{
    private Dao<User, Integer> userDao;
    public UserController(Service<User> service, ObjectMapper objectMapper, Dao<User, Integer> userDao){
        super(service, objectMapper, User.class);
        this.userDao = userDao;
    }

    @Override
    public void patch(Context context, int id) {
        try{
            String username = context.basicAuthCredentials().getUsername();
            String password = context.basicAuthCredentials().getPassword();
            User user = userDao.queryBuilder().where().eq(User.LOGIN, username).queryForFirst();
            if((BCrypt.checkpw(password, user.getPassword()) && user.getId() == id) || (BCrypt.checkpw(password, user.getPassword()) && user.getRole() == UserRole.ADMIN)) {
                User model = getObjectMapper().readValue(context.body(), getClazz());
                UserRole role = model.getRole();
                getService().update(model);
                model.setId(id);
                model.setRole(role);
                context.status(200);
            } else{
                context.status(403).result("Unauthorized");
            }
        }catch (JsonProcessingException | SQLException e){
            e.printStackTrace();
            context.status(400);
        }
    }
}

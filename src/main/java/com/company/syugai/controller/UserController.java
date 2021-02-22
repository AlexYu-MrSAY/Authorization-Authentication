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
import java.util.List;

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
            List<User> users = userDao.queryForAll();
            if(validUser(username, password, users, id)) {
                User model = getObjectMapper().readValue(context.body(), getClazz());
                model.setId(id);
                UserRole role = model.getRole();
                getService().update(model);
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

    public boolean validUser(String username, String password,List<User> users, int id){
        boolean valid = false;
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getId() == id && users.get(i).getLogin().equals(username) && BCrypt.checkpw(password, users.get(i).getPassword())){
                valid = true;
            } else if(users.get(i).getRole() == UserRole.ADMIN && users.get(i).getLogin().equals(username) && BCrypt.checkpw(password, users.get(i).getPassword())){
                valid = true;
            }
        }
        return valid;
    }
}

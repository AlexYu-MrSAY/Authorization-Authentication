package com.company.syugai.json.deserizlization;

import com.company.syugai.model.UserRole;
import com.company.syugai.model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDeserialization extends StdDeserializer<User> {
    public UserDeserialization(){
        super(User.class);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        int id = root.get(User.FIELD_ID).asInt();
        String first_name = root.get(User.FIRST_NAME).asText();
        String last_name = root.get(User.LAST_NAME).asText();
        String login = root.get(User.LOGIN).asText();
        String password = root.get(User.PASSWORD).asText();
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        String birthdayString = root.get(User.BIRTHDAY).asText();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthday = LocalDate.parse(birthdayString, f);
        String stringRole = root.get(User.ROLE).asText();
        stringRole = stringRole.toUpperCase();
        UserRole role = UserRole.valueOf(stringRole);
        return new User(id, first_name, last_name, login, hashed, birthday, role);
    }
}

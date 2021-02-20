package com.company.syugai.json.serialization;

import com.company.syugai.model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class UserSerialization extends StdSerializer<User> {
    public UserSerialization(){
        super(User.class);
    }

    public void serialize(User user, JsonGenerator j, SerializerProvider sp) throws IOException{
        j.writeStartObject();
        j.writeNumberField(User.FIELD_ID, user.getId());
        j.writeStringField(User.FIRST_NAME, user.getFirst_name());
        j.writeStringField(User.LAST_NAME, user.getLast_name());
        j.writeStringField(User.LOGIN, user.getLogin());
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        j.writeStringField(User.BIRTHDAY, user.getBirthday().format(f));
        j.writeStringField(User.ROLE, String.valueOf(user.getRole()));
        j.writeEndObject();
    }
}

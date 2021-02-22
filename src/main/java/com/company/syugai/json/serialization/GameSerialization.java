package com.company.syugai.json.serialization;

import com.company.syugai.model.Game;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class GameSerialization extends StdSerializer<Game> {
    public GameSerialization(){
        super(Game.class);
    }

    public void serialize(Game game, JsonGenerator j, SerializerProvider s) throws IOException {
        j.writeStartObject();
        j.writeNumberField(Game.ID, game.getId());
        j.writeStringField(Game.TITLE, game.getTitle());
        j.writeStringField(Game.DESCRIPTION, game.getDescription());
        j.writeNumberField(Game.PRICE, game.getPrice());
        j.writeEndObject();
    }
}

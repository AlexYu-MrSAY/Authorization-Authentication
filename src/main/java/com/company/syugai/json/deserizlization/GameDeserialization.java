package com.company.syugai.json.deserizlization;

import com.company.syugai.model.Game;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class GameDeserialization extends StdDeserializer<Game> {
    public GameDeserialization(){
        super(Game.class);
    }

    @Override
    public Game deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode root = p.getCodec().readTree(p);
        int id = root.get(Game.ID).asInt();
        String title = root.get(Game.TITLE).asText();
        String description = root.get(Game.DESCRIPTION).asText();
        int price = root.get(Game.PRICE).asInt();
        return new Game(id, title, description, price);
    }
}

package com.company.syugai.controller;

import com.company.syugai.model.Game;
import com.company.syugai.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameController extends AbstractController<Game> {
    public GameController(Service<Game> service, ObjectMapper objectMapper){
        super(service, objectMapper, Game.class);
    }
}

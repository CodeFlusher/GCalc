package me.codeflusher.gcalc.core.application;

import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.util.Identifier;

import java.util.HashMap;

public class Map {
    private final HashMap<Identifier, Model> actors;

    public Map() {
        this.actors = new HashMap<>();
    }

    public void addActor(Identifier identifier, Model model) {
        actors.put(identifier, model);
    }

    public Model getActor(Identifier identifier) {
        return actors.get(identifier);
    }

}

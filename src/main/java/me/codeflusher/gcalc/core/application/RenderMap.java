package me.codeflusher.gcalc.core.application;

import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.util.Identifier;
import me.codeflusher.gcalc.util.LogSystem;

import java.util.HashMap;
import java.util.Map;

public class RenderMap {
    private final Map<Integer, HashMap<Identifier, Model> > actors;

    public RenderMap() {
        this.actors = new HashMap<>();
    }

    public void addActor(Identifier identifier, Model model) {
        if (!actors.containsKey(model.getRenderType())){
            LogSystem.debugLog("Map debug", "Render Type not present!", model.getRenderType());
            actors.put(model.getRenderType(), new HashMap<>());
        }
        actors.get(model.getRenderType()).put(identifier, model);
//        actors.getOrDefault(model.getRenderType(), new HashMap<>()).put(identifier,model);
    }

    public Map<Identifier, Model> getActorMapByRenderType(int renderType){
        if (!actors.containsKey(renderType)){
            actors.put(renderType, new HashMap<>());
        }
        return actors.get(renderType);
//        return actors.getOrDefault(renderType, new HashMap<>());
    }
}

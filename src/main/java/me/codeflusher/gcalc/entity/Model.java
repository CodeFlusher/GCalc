package me.codeflusher.gcalc.entity;

public class Model {
    private final int id;
    private final int vertexCounter;

    public Model(int id, int vertexCounter) {
        this.id = id;
        this.vertexCounter = vertexCounter;
    }

    public int getId() {
        return id;
    }

    public int getVertexCounter() {
        return vertexCounter;
    }
}

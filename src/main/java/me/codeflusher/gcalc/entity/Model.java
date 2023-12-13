package me.codeflusher.gcalc.entity;

import org.joml.Vector3f;

public class Model {
    protected final int id;
    protected final int vertexCounter;
    protected Vector3f color = new Vector3f(0);
    protected int renderType;
    protected float opacity;
    protected boolean render;

    public Model(int id, int vertexCounter, int renderType, float opactiy) {
        this.id = id;
        this.vertexCounter = vertexCounter;
        this.renderType = renderType;
        this.opacity = opactiy;
    }

    public int getId() {
        return id;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public int getRenderType() {
        return renderType;
    }

    public void setRenderType(int renderType) {
        this.renderType = renderType;
    }

    public int getVertexCounter() {
        return vertexCounter;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}

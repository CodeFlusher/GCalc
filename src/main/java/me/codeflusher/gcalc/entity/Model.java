package me.codeflusher.gcalc.entity;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

public class Model {
    protected final int id;
    protected Vector3f color = new Vector3f(0);
    protected final int vertexCounter;
    protected int renderType;
    protected float opactiy;

    public Model(int id, int vertexCounter, int renderType, float opactiy) {
        this.id = id;
        this.vertexCounter = vertexCounter;
        this.renderType = renderType;
        this.opactiy = opactiy;
    }

    public int getId() {
        return id;
    }

    public Vector3f getColor() {
        return color;
    }

    public int getRenderType() {
        return renderType;
    }

    public int getVertexCounter() {
        return vertexCounter;
    }

    public float getOpactiy() {
        return opactiy;
    }

    public static Model createLine(float x, float y, float z, Vector3f color) {
        float[] verticies = {
                -x,-y,-z,
                x,y,z
        };

        ObjectLoader loader = new ObjectLoader();
        loader.cleanup();
        Model model = loader.loadModel(verticies, new int[]{1,2});
        model.color = color;
        model.renderType = GL46.GL_LINES;
        return model;
    }
}

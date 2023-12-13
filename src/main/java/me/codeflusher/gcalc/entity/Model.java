package me.codeflusher.gcalc.entity;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

public class Model {
    protected final int id;
    protected final int vertexCounter;
    protected Vector3f color = new Vector3f(0);
    protected int renderType;
    protected float opacity;

    public Model(int id, int vertexCounter, int renderType, float opactiy) {
        this.id = id;
        this.vertexCounter = vertexCounter;
        this.renderType = renderType;
        this.opacity = opactiy;
    }

    public static Model createLine(float x, float y, float z, Vector3f color) {
        float[] vertices = {
                -x, -y, -z,
                x, y, z
        };

        ObjectLoader loader = new ObjectLoader();
        loader.cleanup();
        Model model = loader.loadModel(vertices, new int[]{1, 2});
        model.color = color;
        model.renderType = GL46.GL_LINES;
        return model;
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

    public float getOpacity() {
        return opacity;
    }
}

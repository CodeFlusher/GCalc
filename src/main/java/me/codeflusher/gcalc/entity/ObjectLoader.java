package me.codeflusher.gcalc.entity;

import me.codeflusher.gcalc.util.Utils;
import org.lwjgl.opengl.GL46;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {
    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();

    public Model loadModel(float[] vertices, int[] indices) {
        cleanup();
        int id = createVAO();
        storeDataInAttributeList(0, 3, vertices);
        storeIndicesBuffer(indices);
        unbind();
        return new Model(id, vertices.length / 3, GL46.GL_TRIANGLES, 1f);
    }

    private int createVAO() {
        int id = GL46.glGenVertexArrays();
        vaos.add(id);
        GL46.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL46.glGenBuffers();
        vbos.add(vbo);
        GL46.glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDatgaInIntBuffer(indices);
        GL46.glBufferData(GL46.GL_ELEMENT_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attribNo, int vertexCount, float[] data) {
        int vbo = GL46.glGenBuffers();
        vbos.add(vbo);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDatgaInFloatBuffer(data);
        GL46.glBufferData(GL46.GL_ARRAY_BUFFER, buffer, GL46.GL_STATIC_DRAW);
        GL46.glVertexAttribPointer(attribNo, vertexCount, GL46.GL_FLOAT, false, 0, 0);
        GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL46.glBindVertexArray(0);
    }

    public void cleanup() {
        vaos.forEach(GL46::glDeleteVertexArrays);
        vbos.forEach(GL46::glDeleteBuffers);
    }
}

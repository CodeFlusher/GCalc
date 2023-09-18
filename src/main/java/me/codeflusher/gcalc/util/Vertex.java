package me.codeflusher.gcalc.util;

public class Vertex {
    public final float x;
    public final float y;
    public final float z;
    public final boolean existing;

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", existing=" + existing +
                '}';
    }

    public Vertex(float x, float y, float z, boolean existing) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.existing = existing;
    }

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.existing =true;
    }

    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }


    public float getZ() {
        return z;
    }

    public boolean isExisting() {
        return existing;
    }

}

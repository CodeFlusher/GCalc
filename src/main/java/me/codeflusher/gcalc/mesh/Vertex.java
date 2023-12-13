package me.codeflusher.gcalc.mesh;

public class Vertex {
    public final float x;
    public final float y;
    public final float z;
    public final boolean existing;

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
        this.existing = true;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", existing=" + existing +
                '}';
    }

    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }

}

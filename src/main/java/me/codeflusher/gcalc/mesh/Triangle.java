package me.codeflusher.gcalc.mesh;

import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;

import java.util.ArrayList;

public class Triangle {
    int vert1ID;
    int vert2ID;
    int vert3ID;

    public Triangle(int vert1ID, int vert2ID, int vert3ID) {
        this.vert1ID = vert1ID;
        this.vert2ID = vert2ID;
        this.vert3ID = vert3ID;
    }

    public static ArrayList<Triangle> createTriangleMesh(ArrayList<Vertex> vertices, boolean isStatic) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        Config config = ConfigManager.getConfig();
        Integer resolution = isStatic ? config.getStaticMeshResolution() : config.getDynamicMeshResolution();
        for (int x = 0; x < resolution - 1; x++) {
            for (int y = 0; y < resolution - 1; y++) {
                int[] ids = {resolution * x + y, resolution * (x + 1) + y, resolution * x + (y + 1), resolution * (x + 1) + (y + 1)};
                Vertex vert1 = vertices.get(ids[0]);
                Vertex vert2 = vertices.get(ids[1]);
                Vertex vert3 = vertices.get(ids[1]);
                Vertex vert4 = vertices.get(ids[2]);
                Vertex vert5 = vertices.get(ids[2]);
                Vertex vert6 = vertices.get(ids[3]);
                if (vert1.existing && vert2.existing && vert4.existing) {
                    triangles.add(new Triangle(ids[0], ids[2], ids[1]));
                }
                if (vert3.existing && vert5.existing && vert6.existing) {
                    triangles.add(new Triangle(ids[1], ids[2], ids[3]));
                }
            }
        }
        return triangles;
    }

    public int getVert1ID() {
        return vert1ID;
    }

    public int getVert2ID() {
        return vert2ID;
    }

    public int getVert3ID() {
        return vert3ID;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vert1ID=" + vert1ID +
                ", vert2ID=" + vert2ID +
                ", vert3ID=" + vert3ID +
                '}';
    }

}

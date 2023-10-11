package me.codeflusher.gcalc.mesh;

import me.codeflusher.gcalc.util.LogSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VertexSolver {

    private static ArrayList<VertexRow> vertexArrayList;

    public static Vertex createVertex(VertexPredictionRunnable predicate, Integer x, Integer y, VertexAttributeCompute attributeCompute) throws Exception {
        return new Vertex(attributeCompute.getVertexPosition(x), attributeCompute.getVertexPosition(y), predicate.run(attributeCompute.modifyX(x), attributeCompute.modifyY(y)));
    }

    public static Vertex[] createTriangle(VertexPredictionRunnable predicate, Integer x, Integer y, VertexAttributeCompute attributeCompute, Boolean isOdd) {
        Vertex[] vertices = new Vertex[3];
        int odd = isOdd ? 1 : 0;
        for (int i = 0; i < 3; i++) {
            try {
                vertices[i] = createVertex(predicate, x + (i + odd) % 2, y + (i + odd) / 2, attributeCompute);
            } catch (Exception e) {
                vertices[i] = new Vertex(attributeCompute.getVertexPosition(x), attributeCompute.getVertexPosition(y), 0, false);
            }
        }
        return vertices;
    }

    public static VertexRow createRow(VertexPredictionRunnable predicate, Integer xPos, VertexAttributeCompute attributeCompute) {
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int y = 0; y < attributeCompute.getResolution(); y++) {
            Vertex[] vertices1 = createTriangle(predicate, xPos, y, attributeCompute, false);
            Vertex[] vertices2 = createTriangle(predicate, xPos, y, attributeCompute, true);
            vertices.addAll(List.of(vertices1));
            vertices.addAll(List.of(vertices2));
        }
        return new VertexRow(vertices, xPos);
    }

    public static ArrayList<Vertex> getVertexMesh(VertexPredictionRunnable predicate, boolean isStatic) {

        ArrayList<Future<?>> futures = new ArrayList<>();
        VertexAttributeCompute attributeCompute = new VertexAttributeCompute(isStatic);
        int resolution = attributeCompute.getResolution();
        //LogSystem.debugLog("Mesh Compute", attributeCompute);
        vertexArrayList = new ArrayList<>();
        vertexArrayList.ensureCapacity(resolution);
        try (var executor = Executors.newFixedThreadPool(resolution)) {
            for (int x = 0; x < resolution; x++) {
                int finalX = x;
                futures.add(executor.submit(() -> createRow(predicate, finalX, attributeCompute)));
            }
        } catch (Exception e) {
            LogSystem.exception("Mesh Compute", e);
        }
        boolean isDone = false;
        // LogSystem.debugLog("Mesh Compute", futures.size());
        while (!isDone) {
            isDone = futures.stream().allMatch(Future::isDone);
        }
        futures.forEach(future -> {
            try {
                VertexRow row = (VertexRow) future.get();
                vertexArrayList.add(row.id, row);
            } catch (Exception e) {
                LogSystem.exception("Mesh Compute", e);
            }
        });

//        LogSystem.debugLog("Mesh Compute", vertexArrayList.size());
//        LogSystem.debugLog("Mesh Compute",futures.stream().allMatch(Future::isDone));
        ArrayList<Vertex> vertices = new ArrayList<>();
        vertexArrayList.forEach(vertexRow -> vertices.addAll(vertexRow.vertexArrayList()));
        return vertices;
    }

    public interface VertexPredictionRunnable {
        Float run(float x, float y) throws Exception;
    }

    public record VertexRow(ArrayList<Vertex> vertexArrayList, Integer id) {
    }

}

package me.codeflusher.gcalc.mesh;

import me.codeflusher.gcalc.util.LogSystem;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class VertexSolver {

    private static ArrayList<VertexRow> vertexArrayList;

    private static Float a;

    public static Vertex createVertex(Expression predicate, Integer x, Integer y, VertexAttributeCompute attributeCompute) throws Exception {
        var eq = predicate.setVariable("y", attributeCompute.modifyY(y)).setVariable("x", attributeCompute.modifyX(x)).setVariable("a", a).evaluate();
        return new Vertex(attributeCompute.getVertexPosition(x), attributeCompute.getVertexPosition(y), (float) eq);
    }

    public static Vertex[] createTriangle(Expression predicate, Integer x, Integer y, VertexAttributeCompute attributeCompute, Boolean isOdd) {
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

    public static VertexRow createRow(Expression predicate, Integer xPos, VertexAttributeCompute attributeCompute) {
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int y = 0; y < attributeCompute.getResolution(); y++) {
            Vertex[] vertices1 = createTriangle(predicate, xPos, y, attributeCompute, false);
            Vertex[] vertices2 = createTriangle(predicate, xPos, y, attributeCompute, true);
            vertices.addAll(List.of(vertices1));
            vertices.addAll(List.of(vertices2));
        }
        return new VertexRow(vertices, xPos);
    }

    public static ArrayList<Vertex> getVertexMesh(ExpressionBuilder predicate, boolean isStatic, Float aState) {
        a = aState;
        VertexAttributeCompute attributeCompute = new VertexAttributeCompute(isStatic);
        int resolution = attributeCompute.getResolution();
        //LogSystem.debugLog("Mesh Compute", attributeCompute);
        vertexArrayList = new ArrayList<>();
        vertexArrayList.ensureCapacity(resolution);
        for (int x = 0; x < resolution; x++) {
            int finalX = x;
            Expression expression;
            try {
                expression = predicate.build();
                vertexArrayList.add(createRow(expression, finalX, attributeCompute));
            } catch (Exception e) {
                LogSystem.exception("Expression", e);
            }
        }
//        boolean isDone = false;
//        // LogSystem.debugLog("Mesh Compute", futures.size());
//        while (!isDone) {
//            isDone = futures.stream().allMatch(Future::isDone);
//        }
//        futures.forEach(future -> {
//            try {
//                VertexRow row = (VertexRow) future.get();
//                vertexArrayList.add(row.id, row);
//            } catch (Exception e) {
//                LogSystem.exception("Mesh Compute", e);
//            }
//        });
        ArrayList<Vertex> vertices = new ArrayList<>();
        vertexArrayList.forEach(vertexRow -> vertices.addAll(vertexRow.vertexArrayList()));
        return vertices;
    }


    public record VertexRow(ArrayList<Vertex> vertexArrayList, Integer id) {
    }

}

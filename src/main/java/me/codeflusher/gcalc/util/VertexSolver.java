package me.codeflusher.gcalc.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VertexSolver {

    public interface VertexPredictionRunnable{
        Float run(float x,float y) throws Exception;
    }

    public record VertexRow(ArrayList<Vertex> vertexArrayList, Integer id){}

    private static ArrayList<VertexRow> vertexArrayList;

    public static Vertex createVertex(VertexPredictionRunnable predicate, Integer x, Integer y, float scaleModifier, Integer resolutionX, Integer resolutionY,float offset) throws Exception{
        return new Vertex(
                x*((float) Constants.MODEL_SIZE /resolutionX),
                y*((float) Constants.MODEL_SIZE /resolutionY),
                predicate.run((x+offset)*scaleModifier*((float) Constants.RESOLUTION_REFERENCE*((float) resolutionX /resolutionY) /resolutionX),
                        (y+offset)*scaleModifier*((float) Constants.RESOLUTION_REFERENCE*((float) resolutionY /resolutionX) /resolutionY)));
    }

    public static Vertex[] createTriangle(VertexPredictionRunnable predicate, Integer x, Integer y, float scaleModifier, Boolean isOdd, Integer resolutionX, Integer resolutionY, float offset){
        Vertex[] vertices = new Vertex[3];
        int odd = isOdd?1:0;
        for(int i = 0; i<3; i++){
            try {
                vertices[i] = createVertex(predicate, x+(i+odd)%2,y+(i+odd)/2, scaleModifier,resolutionX,resolutionY, offset);
            }catch (Exception e){
                vertices[i] = new Vertex(x+i%2,(y + (float) (i + odd) / 2),0,false);
            }
        }
        return vertices;
    }

    public static VertexRow createRow(VertexPredictionRunnable predicate, Integer xPos, Integer resolutionX, Integer resolutionY, float scaleModifier,float offset){
        ArrayList<Vertex> vertices = new ArrayList<>();
        for (int y = -1*(resolutionY/2); y<(resolutionY/2)-1; y++){
            Vertex[] vertices1 = createTriangle(predicate, xPos , y,scaleModifier, false,resolutionX,resolutionY, offset);
            Vertex[] vertices2 = createTriangle(predicate, xPos, y,scaleModifier, true,resolutionX,resolutionY, offset);
            vertices.addAll(List.of(vertices1));
            vertices.addAll(List.of(vertices2));
        }
        return new VertexRow(vertices, xPos+(resolutionX/2));
    }

    public static ArrayList<Vertex> getVertexMesh(VertexPredictionRunnable predicate, Integer resolutionX, Integer resolutionY, float scaleModifier, float offset){

        ArrayList<Future<?>> futures = new ArrayList<>();
        try(var executor = Executors.newFixedThreadPool(resolutionX)) {
            vertexArrayList = new ArrayList<>(resolutionX-1);
            for (int x = -1*(resolutionX/2); x<(resolutionX/2); x++){
                int finalX = x;
                futures.add(executor.submit(() -> createRow(predicate, finalX, resolutionX, resolutionY, scaleModifier, offset)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        boolean isDone = false;
        while (!isDone){
            isDone = futures.stream().allMatch(Future::isDone);
        }
        futures.forEach(future -> {
            try {
                VertexRow row = (VertexRow) future.get();
                vertexArrayList.add(row.id, row);
            } catch (Exception e){
                LogSystem.log("Error", e);
            }
        });

//        LogSystem.debugLog("Mesh Compute", vertexArrayList.size());
//        LogSystem.debugLog("Mesh Compute",futures.stream().allMatch(Future::isDone));
        ArrayList<Vertex> vertices = new ArrayList<>();
        vertexArrayList.forEach(vertexRow -> vertices.addAll(vertexRow.vertexArrayList()));
        return vertices;
    }

}

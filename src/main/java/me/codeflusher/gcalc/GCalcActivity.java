package me.codeflusher.gcalc;

import me.codeflusher.gcalc.core.*;
import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.entity.ObjectLoader;
import me.codeflusher.gcalc.user.Camera;
import me.codeflusher.gcalc.util.*;
import org.joml.Math;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallbackI;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;

import javax.swing.*;
import java.util.ArrayList;

public class GCalcActivity implements ILogic {

    private StringBuffer inputStringBuffer;
    private boolean isAwaitingForInput;
    private Tristate isMovingGraph;
    private static final Float CAMERA_MOVEMENT_SPEED = 0.05f;
    private int updateCounter;
    private long totalTimeCounted;
    private int direction = 0;
    private float color = 0.0f;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private Model model;
    private Camera camera;
    Vector3f cameraInc;
    private final WindowManager window;

    public GCalcActivity(){
        this.renderer = new RenderManager();
        this.window = GCalcCore.getWindow();
        this.loader = new ObjectLoader();
        this.camera = new Camera();
        this.inputStringBuffer = new StringBuffer();
        isAwaitingForInput = false;
        isMovingGraph = Tristate.RUN;
        //camera.setPosition(new Vector3f(20,20,20));
        cameraInc = new Vector3f(0,0,0);
    }
    @Override
    public void init() throws Exception {
        LogSystem.debugLog("Initalizing Activity", "init");
        GLFW.glfwSetCharCallback(window.getWindow(), (window, codepoint) -> {
            inputStringBuffer.append(Character.toChars(codepoint));
        });
        updateCounter = 0;
        totalTimeCounted = 0;
        renderer.initializeRendering();
    }

    //(float) (10*Math.cos(x)+10*Math.sin(y))
    public void updateModel(Integer resolutionX, Integer resolutionY, float scaleValue, float offset){
       // LogSystem.log("Mesh compute", "Starting mesh computation");
        long startTime = System.nanoTime();
        ArrayList<Vertex> vertices = VertexSolver.getVertexMesh((x, y) -> Math.sin(x)+Math.sin(y), resolutionX, resolutionY, scaleValue, offset);
        ArrayList<Triangle> triangles = Triangle.createTriangleMesh(vertices, resolutionX, resolutionY);

        ArrayList<Float> vertFloats = new ArrayList<>(){
            {
                for (Vertex vert:
                     vertices) {
                    add(vert.x);
                    add(vert.z);
                    add(vert.y);
                }
            }
        };
        ArrayList<Integer> tris = new ArrayList<>(){
            {
                for (Triangle vert:
                     triangles) {
                    add(vert.getVert1ID());
                    add(vert.getVert2ID());
                    add(vert.getVert3ID());
                }
            }
        };
        loader.cleanup();
        this.model = loader.loadModel(Utils.toFloatArray(vertFloats), Utils.toIntArray(tris));
        long endTime = System.nanoTime() - startTime;
        //LogSystem.log("Mesh Compute", "Computed mesh in: " + endTime);
        totalTimeCounted +=endTime;
        updateCounter++;
        if (updateCounter==360){
            totalTimeCounted/=updateCounter;
            LogSystem.log("Mesh Compute", "Computed 360 meshed in average: " + totalTimeCounted + " nanoseconds or " + ((float)totalTimeCounted)/EngineManager.NANOSECOND + " seconds");
            totalTimeCounted = 0;
            updateCounter = 0;
        }
    }

    @Override
    public void input() {
       cameraInc.set(0,0,0);
       if (window.isKeyPressed(GLFW.GLFW_KEY_ENTER)){
           if(isAwaitingForInput){
                LogSystem.log("Parsed input", inputStringBuffer.toString());
           }
           inputStringBuffer = new StringBuffer();
           isAwaitingForInput = !isAwaitingForInput;

       }

        if (isAwaitingForInput){
            return;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)){
            cameraInc.z = -1;
        }if (window.isKeyPressed(GLFW.GLFW_KEY_S)){
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)){
            cameraInc.x = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)){
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)){
            isMovingGraph = isMovingGraph.getInversed();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
           cameraInc.y = -1;
        }
    }

    @Override
    public void update(MouseInput input) {
        Integer resolution = 100;
        if(isMovingGraph == Tristate.RUN)
            updateModel(resolution, resolution, 0.05f, (float) GCalcCore.getEngine().getCurrentFrame() / 15);
        else if(isMovingGraph == Tristate.AWAIT){
            updateModel(resolution*5, resolution*5, 0.05f, 0);
            isMovingGraph = Tristate.STOP;
        }

        camera.movePosition(cameraInc.x * CAMERA_MOVEMENT_SPEED, cameraInc.y*CAMERA_MOVEMENT_SPEED, cameraInc.z*CAMERA_MOVEMENT_SPEED);
        if (input.isRightButtonPressed()){
            Vector2f rotationVector = input.getDisplayVector();
            camera.moveRotation(rotationVector.x * Constants.MOUSE_SENSITIVITY, rotationVector.y * Constants.MOUSE_SENSITIVITY, 0f);
        }
    }

    @Override
    public void render() {
        if (window.isResizeable()){
            GL46.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResizeable(true);
        }
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(model, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }
}

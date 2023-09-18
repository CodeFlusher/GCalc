package me.codeflusher.gcalc;

import me.codeflusher.gcalc.core.ILogic;
import me.codeflusher.gcalc.core.MouseInput;
import me.codeflusher.gcalc.core.RenderManager;
import me.codeflusher.gcalc.core.WindowManager;
import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.entity.ObjectLoader;
import me.codeflusher.gcalc.user.Camera;
import me.codeflusher.gcalc.util.*;
import org.joml.Math;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class GCalcActivity implements ILogic {

    private static final Float CAMERA_MOVEMENT_SPEED = 0.05f;

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
        //camera.setPosition(new Vector3f(20,20,20));
        cameraInc = new Vector3f(0,0,0);
    }
    @Override
    public void init() throws Exception {
        LogSystem.debugLog("Initalizing Activity", "init");
        updateModel(500, 500, 0.01f);
//        try {
//            Thread thread = new Thread(() -> updateModel(500, 500, 0.01f));
//            thread.start();
//        }catch (Exception e){
//            LogSystem.log("Error", e);
//        }
//        this.model = loader.loadModel(new float[]{0,0,0,0,0,0,0,0,0}, new int[]{0,1,2});
        renderer.initializeRendering();
    }

    //(float) (10*Math.cos(x)+10*Math.sin(y))
    public void updateModel(Integer resolutionX, Integer resolutionY, float scaleValue){

        LogSystem.log("Mesh compute", "Starting mesh computation");
        ArrayList<Vertex> vertices = VertexSolver.getVertexMesh((x, y) -> x*x-y*y, resolutionX, resolutionY, scaleValue);

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
//        LogSystem.debugLog("Mesh compute", vertices);
//        LogSystem.debugLog("Mesh compute", triangles);
//        LogSystem.debugLog("Mesh compute", vertFloats);
//        LogSystem.debugLog("Mesh compute", tris);
        loader.cleanup();
        this.model = loader.loadModel(Utils.toFloatArray(vertFloats), Utils.toIntArray(tris));
    }

    @Override
    public void input() {
       cameraInc.set(0,0,0);
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
            cameraInc.y = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
            cameraInc.y = -1;
        }
    }

    @Override
    public void update(MouseInput input) {
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

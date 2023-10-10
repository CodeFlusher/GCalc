package me.codeflusher.gcalc;

import lwjgui.event.ActionEvent;
import lwjgui.event.EventHandler;
import lwjgui.event.MouseEvent;
import lwjgui.geometry.Insets;
import lwjgui.paint.Color;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Slider;
import lwjgui.scene.control.TextField;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.Pane;
import lwjgui.scene.layout.VBox;
import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.core.EngineManager;
import me.codeflusher.gcalc.core.RenderManager;
import me.codeflusher.gcalc.core.GAppWindowManager;
import me.codeflusher.gcalc.core.application.AppScene;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.core.application.Map;
import me.codeflusher.gcalc.core.application.MouseInput;
import me.codeflusher.gcalc.entity.ObjectLoader;
import me.codeflusher.gcalc.mesh.Triangle;
import me.codeflusher.gcalc.mesh.Vertex;
import me.codeflusher.gcalc.mesh.VertexSolver;
import me.codeflusher.gcalc.user.Camera;
import me.codeflusher.gcalc.util.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class GCalcActivity implements IApplication {

    private static final Float CAMERA_MOVEMENT_SPEED = 0.05f;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final AppScene scene;
    private final GAppWindowManager window;
    private Tristate isMovingGraph;
    private int updateCounter;
    private long totalTimeCounted;
    private final float color = 0.0f;
    private final Vector3f cameraInc;
    private MouseInput mouseInput;
    private boolean isInput;

    public GCalcActivity() {
        this.renderer = new RenderManager();
        this.window = GCalcCore.getWindowManager();
        this.loader = new ObjectLoader();
        scene = new AppScene(new Map(), new Camera());
        isMovingGraph = Tristate.AWAIT;
        //camera.setPosition(new Vector3f(20,20,20));
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init() throws Exception {
        LogSystem.debugLog("Initalizing Activity", "init");
        updateCounter = 0;
        totalTimeCounted = 0;
        renderer.initializeRendering();
        scene.getMap().addActor(Constants.MODEL_IDENTIFIER, loader.loadModel(new float[]{1, 1, 1, 1, 1, 1},
                new int[]{1, 1, 1, 1, 1, 1}));
    }

    //(float) (10*Math.cos(x)+10*Math.sin(y))
    public void updateModel(Integer resolutionX, Integer resolutionY, float scaleValue, float offset) {
        // LogSystem.log("Mesh compute", "Starting mesh computation");
        long startTime = System.nanoTime();
        ArrayList<Vertex> vertices = VertexSolver.getVertexMesh((x, y) -> (float) Math.pow(Math.abs(x), y), resolutionX, resolutionY, scaleValue, offset);
        ArrayList<Triangle> triangles = Triangle.createTriangleMesh(vertices, resolutionX, resolutionY);

        ArrayList<Float> vertFloats = new ArrayList<>() {
            {
                for (Vertex vert :
                        vertices) {
                    add(vert.x);
                    add(vert.z);
                    add(vert.y);
                }
            }
        };
        ArrayList<Integer> tris = new ArrayList<>() {
            {
                for (Triangle vert :
                        triangles) {
                    add(vert.getVert1ID());
                    add(vert.getVert2ID());
                    add(vert.getVert3ID());
                }
            }
        };
        loader.cleanup();
        scene.getMap().addActor(Constants.MODEL_IDENTIFIER, loader.loadModel(Utils.toFloatArray(vertFloats), Utils.toIntArray(tris)));
        long endTime = System.nanoTime() - startTime;
        //LogSystem.log("Mesh Compute", "Computed mesh in: " + endTime);
        totalTimeCounted += endTime;
        updateCounter++;
        if (updateCounter == 360) {
            totalTimeCounted /= updateCounter;
            LogSystem.log("Mesh Compute", "Computed 360 meshed in average: " + totalTimeCounted + " nanoseconds or " + ((float) totalTimeCounted) / EngineManager.NANOSECOND + " seconds");
            totalTimeCounted = 0;
            updateCounter = 0;
        }
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraInc.z = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraInc.x = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            cameraInc.y = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            isMovingGraph = isMovingGraph.getInversed();
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        }
    }

    public void setMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }

    @Override
    public void update() {
        Integer resolution = 100;
        if (isMovingGraph == Tristate.RUN)
            updateModel(resolution, resolution, 0.05f, (float) GCalcCore.getEngine().getCurrentFrame() / 15);
        else if (isMovingGraph == Tristate.AWAIT) {
            updateModel(resolution * 5, resolution * 5, 0.05f, 0);
            isMovingGraph = Tristate.STOP;
        }

        scene.getCamera().movePosition(cameraInc.x * CAMERA_MOVEMENT_SPEED, cameraInc.y * CAMERA_MOVEMENT_SPEED, cameraInc.z * CAMERA_MOVEMENT_SPEED);
        if (isInput){
            Vector2f rotationVector = mouseInput.getDisplayVector();
            scene.getCamera().moveRotation(rotationVector.x * Constants.MOUSE_SENSITIVITY, rotationVector.y * Constants.MOUSE_SENSITIVITY, 0f);
        }

    }

    @Override
    public void render() {
        if (window.isResizeable()) {
            GL46.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResizeable(true);
        }
        window.setClearColor(color, color, color, 0.0f);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }

    @Override
    public AppScene getScene() {
        return scene;
    }

    @Override
    public Pane createUI() {

        VBox root = new VBox();

        OpenGLPane glPane = new OpenGLPane();
        glPane.setBackgroundLegacy(new Color(0, 0, 0, 0));
        glPane.setRendererCallback(renderer);
        glPane.setOnMousePressed(event -> {
            isInput = true;
        });

        glPane.setOnMouseReleased(event -> {
            isInput = false;
        });

        Config config = ConfigManager.getConfig();

        HBox row1 = new HBox();
        HBox row2 = new HBox();
        HBox row3 = new HBox();

        EventHandler<ActionEvent> eventHandler = event -> {

            this.isMovingGraph = this.isMovingGraph.getInversed();
        };

        Button toggleDynamicGraph = new Button("Toggle dynamic graph");
        toggleDynamicGraph.setPrefSize((int) (config.getResolutionX() * 0.05), (int) (config.getResolutionX() * 0.05));
        toggleDynamicGraph.setOnAction(eventHandler);
        toggleDynamicGraph.setText("Toggle Dynamic Graph");


        TextField functionTextField = new TextField();
        functionTextField.setPrefSize((int) (config.getResolutionX() * 0.05), (int) (config.getResolutionX() * 0.05));

        Slider slider = new Slider();

        int insetValue = 15;
        Insets insets = new Insets(insetValue);

        row1.getChildren().addAll(toggleDynamicGraph);
        row1.setPadding(insets);

        row2.getChildren().addAll(functionTextField);
        row2.setPadding(insets);

        row3.getChildren().addAll(slider);

        glPane.setPrefSize(config.getResolutionX(), Math.floor((float) config.getResolutionY() * 0.75));

        root.getChildren().add(glPane);
        root.getChildren().addAll( row1, row2, row3);

        root.setMinSize(config.getResolutionX(), config.getResolutionY());

        return root;
    }
}

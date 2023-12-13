package me.codeflusher.gcalc;

import lwjgui.font.FontStyle;
import lwjgui.geometry.Insets;
import lwjgui.geometry.Pos;
import lwjgui.paint.Color;
import lwjgui.scene.control.Button;
import lwjgui.scene.control.Label;
import lwjgui.scene.control.Slider;
import lwjgui.scene.control.TextField;
import lwjgui.scene.layout.HBox;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.Pane;
import lwjgui.scene.layout.VBox;
import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.config.ParamRange;
import me.codeflusher.gcalc.core.EngineManager;
import me.codeflusher.gcalc.core.GAppWindowManager;
import me.codeflusher.gcalc.core.RenderManager;
import me.codeflusher.gcalc.core.application.AppScene;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.core.application.MouseInput;
import me.codeflusher.gcalc.core.application.RenderMap;
import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.entity.ObjectLoader;
import me.codeflusher.gcalc.mesh.Triangle;
import me.codeflusher.gcalc.mesh.Vertex;
import me.codeflusher.gcalc.mesh.VertexSolver;
import me.codeflusher.gcalc.user.Camera;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.LogSystem;
import me.codeflusher.gcalc.util.Tristate;
import me.codeflusher.gcalc.util.Utils;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class GCalcActivity implements IApplication {

    private static Float CAMERA_MOVEMENT_SPEED = 0.05f;
    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final AppScene scene;
    private final GAppWindowManager window;
    private final Vector3f cameraInc;
    private Tristate isMovingGraph;
    private int updateCounter;
    private long totalTimeCounted;
    private MouseInput mouseInput;
    private boolean isInput;
    private float aState;
    private ExpressionBuilder expressionBuilder;
    private Label averageDynamicMeshProcessionTime;
    private Label flightSpeedLabel;
    private Label staticMeshProcessionTime;
    private Label frameRate;
    private Label isExpressionFailedToBuild;

    public GCalcActivity() {
        this.renderer = new RenderManager();
        this.window = GCalcCore.getWindowManager();
        this.loader = new ObjectLoader();
        Config config = ConfigManager.getConfig();
        aState = config.getASliderState();
        scene = new AppScene(new RenderMap(), new Camera());

        isMovingGraph = Tristate.AWAIT;
        scene.getCamera().setPosition(new Vector3f(5, 5, 5));
        cameraInc = new Vector3f(0, 0, 0);
        this.expressionBuilder = new ExpressionBuilder(config.getLatestPrompt()).variables("x", "y", "a");
    }

    @Override
    public void init() throws Exception {
        LogSystem.debugLog("Initializing Activity", "init");
        updateCounter = 0;
        totalTimeCounted = 0;
        renderer.initializeRendering();

        scene.getMap().addActor(Constants.LINE_X_IDENTIFIER, Model.createLine((float) (Constants.MODEL_SIZE / Math.sqrt(2)), 0, 0, new Vector3f(1, 0, 0)));
        scene.getMap().addActor(Constants.LINE_Y_IDENTIFIER, Model.createLine(0, (float) (Constants.MODEL_SIZE / Math.sqrt(2)), 0, new Vector3f(0, 1, 0)));
        scene.getMap().addActor(Constants.LINE_Z_IDENTIFIER, Model.createLine(0, 0, (float) (Constants.MODEL_SIZE / Math.sqrt(2)), new Vector3f(0, 0, 1)));
        scene.getMap().addActor(Constants.MODEL_IDENTIFIER, loader.loadModel(new float[]{1, 1, 1, 1, 1, 1},
                new int[]{1, 1, 1, 1, 1, 1}));
    }

    public void updateModel() {
        long startTime = System.nanoTime();
        boolean isStatic = isMovingGraph != Tristate.RUN;

        ArrayList<Vertex> vertices = VertexSolver.getVertexMesh(expressionBuilder, isStatic, aState);
        if (vertices.isEmpty()) {
            LogSystem.exception("Mesh Compute", new RuntimeException("Failed to create vertex mesh"));
            return;
        }
        ArrayList<Triangle> triangles = Triangle.createTriangleMesh(vertices, isStatic);

        ArrayList<Float> vertFloats = new ArrayList<Float>() {
            {
                for (Vertex vert :
                        vertices) {
                    add(vert.x);
                    add(vert.z);
                    add(vert.y);
                }
            }
        };
        ArrayList<Integer> tris = new ArrayList<Integer>() {
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
        if (isStatic) {
            this.setStaticMeshProcessionTime(endTime);
        } else {
            totalTimeCounted += endTime;
            updateCounter++;
        }
        if (updateCounter == 360) {
            totalTimeCounted /= updateCounter;
            LogSystem.log("Mesh Compute", "Computed 360 meshed in average: " + totalTimeCounted + " nanoseconds or " + ((float) totalTimeCounted) / EngineManager.NANOSECOND + " seconds for each graph mesh.");
            this.setAverageDynamicMeshProcessionTime(totalTimeCounted);
            totalTimeCounted = 0;
            updateCounter = 0;
        }
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (!isInput)
            return;
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
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            cameraInc.y = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) {
            cameraInc.y = -1;
        }
    }

    public void setMouseInput(MouseInput mouseInput) {
        this.mouseInput = mouseInput;
    }

    @Override
    public void update() {
        if (isMovingGraph == Tristate.RUN) {
            updateModel();
        } else if (isMovingGraph == Tristate.AWAIT) {
            updateModel();
            isMovingGraph = Tristate.STOP;

        }

        scene.getCamera().movePosition(cameraInc.x * CAMERA_MOVEMENT_SPEED, cameraInc.y * CAMERA_MOVEMENT_SPEED, cameraInc.z * CAMERA_MOVEMENT_SPEED);
        if (isInput) {
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
        float color = 0.0f;
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
        Config config = ConfigManager.getConfig();

        VBox root = new VBox();

        OpenGLPane glPane = new OpenGLPane();
        glPane.setBackgroundLegacy(new Color(0, 0, 0, 0));
        glPane.setRendererCallback(renderer);
        glPane.setOnMousePressed(event -> isInput = true);

        glPane.setPrefSize(config.getResolutionX(), Math.floor((float) config.getResolutionY() * (0.66)));

        glPane.setOnMouseReleased(event -> isInput = false);

        HBox row0 = new HBox();
        HBox row1 = new HBox();
        HBox row2 = new HBox();
        HBox row3 = new HBox();

        int insetValue = (int) (config.getResolutionY() * 0.033);
        Insets insets = new Insets(insetValue, 0, 0, 0);

        this.staticMeshProcessionTime = new Label("Static mesh procession time is undefined");
        this.averageDynamicMeshProcessionTime = new Label("Average dynamic mesh procession time is undefined");
        this.frameRate = new Label("Framerate undefined");
        this.isExpressionFailedToBuild = new Label();
        this.flightSpeedLabel = new Label("Current flight speed: " + CAMERA_MOVEMENT_SPEED);

        staticMeshProcessionTime.setFontSize(28);
        averageDynamicMeshProcessionTime.setFontSize(28);
        frameRate.setFontSize(28);
        flightSpeedLabel.setFontSize(28);
        isExpressionFailedToBuild.setFontSize(32);
        isExpressionFailedToBuild.setTextFill(Color.RED);
        isExpressionFailedToBuild.setFontStyle(FontStyle.BOLD);

        glPane.setAlignment(Pos.TOP_LEFT);
        glPane.setPadding(new Insets(16));
        VBox glPaneRow = new VBox();
        glPaneRow.getChildren().addAll(frameRate, staticMeshProcessionTime, averageDynamicMeshProcessionTime, flightSpeedLabel, isExpressionFailedToBuild);
        glPane.getChildren().add(glPaneRow);
        glPane.setOnMouseScrolled(event -> {
            LogSystem.debugLog("Scroll event debug", event.y);
            CAMERA_MOVEMENT_SPEED = Utils.clampFloat((float) (CAMERA_MOVEMENT_SPEED + event.y / 100), Constants.MIN_FLIGHT_SPEED, Constants.MAX_FLIGHT_SPEED);
            this.flightSpeedLabel.setText("Current flight speed: " + CAMERA_MOVEMENT_SPEED);
        });

        TextField staticMeshResolutionField = new TextField();
        Label staticMeshResolutionLabel = new Label("Static mesh resolution");
        staticMeshResolutionLabel.setPrefSize(config.getResolutionX() * 0.25, config.getResolutionY() * 0.05);
        staticMeshResolutionField.setPrefSize(config.getResolutionX() * 0.25, config.getResolutionY() * 0.05);
        TextField dynamicMeshResolutionField = new TextField();
        Label dynamicMeshResolutionLabel = new Label("Dynamic mesh resolution");
        dynamicMeshResolutionLabel.setPrefSize(config.getResolutionX() * 0.25, config.getResolutionY() * 0.05);
        dynamicMeshResolutionField.setPrefSize(config.getResolutionX() * 0.25, config.getResolutionY() * 0.05);

        row0.getChildren().addAll(staticMeshResolutionLabel, staticMeshResolutionField, dynamicMeshResolutionLabel, dynamicMeshResolutionField);
        row0.setPadding(insets);

        Button toggleDynamicGraph = new Button("Toggle dynamic graph");
        toggleDynamicGraph.setPrefSize((int) (config.getResolutionX() * 0.33), (int) (config.getResolutionY() * 0.05));

        Button updateGraph = new Button("Update graph");
        updateGraph.setPrefSize((int) (config.getResolutionX() * 0.33), (int) (config.getResolutionY() * 0.05));

        TextField xRangeField = new TextField();
        Label xRangeLabel = new Label("Maximum X range");
        xRangeField.setPrefSize(config.getResolutionX() * 0.15, config.getResolutionY() * 0.05);
        xRangeLabel.setPrefSize(config.getResolutionX() * 0.15, config.getResolutionY() * 0.05);

        row1.getChildren().addAll(toggleDynamicGraph, updateGraph, xRangeLabel, xRangeField);
        row1.setPadding(insets);

        Label graphEquationLabel = new Label("Graph equation: f(x,y) = z; f(x,y) = ");
        graphEquationLabel.setPrefSize((int) (config.getResolutionX() * 0.33), (int) (config.getResolutionY() * 0.05));

        TextField graphEquation = new TextField();
        graphEquation.setPrefSize((int) (config.getResolutionX() * 0.33), (int) (config.getResolutionY() * 0.05));

        TextField yRangeField = new TextField();
        Label yRangeLabel = new Label("Maximum Y range");
        yRangeField.setPrefSize(config.getResolutionX() * 0.15, config.getResolutionY() * 0.05);
        yRangeLabel.setPrefSize(config.getResolutionX() * 0.15, config.getResolutionY() * 0.05);

        row2.getChildren().addAll(graphEquationLabel, graphEquation, yRangeLabel, yRangeField);
        row2.setPadding(insets);

        Label aParamSliderLabel = new Label("Slider of 'a' param");
        aParamSliderLabel.setPrefSize((int) (config.getResolutionX() * 0.33), (int) (config.getResolutionY() * 0.05));

        Slider aParamSlider = new Slider(-1 * config.getRangeA().getMax(), config.getRangeA().getMax(), config.getASliderState());
        aParamSlider.setPrefSize((int) (config.getResolutionX() * 0.33), (int) (config.getResolutionY() * 0.05));

        TextField aRangeField = new TextField();
        Label aRangeLabel = new Label("Maximum 'a' param range");

        aRangeField.setPrefSize(config.getResolutionX() * 0.15, config.getResolutionY() * 0.05);
        aRangeLabel.setPrefSize(config.getResolutionX() * 0.15, config.getResolutionY() * 0.05);

        row3.getChildren().addAll(aParamSliderLabel, aParamSlider, aRangeLabel, aRangeField);
        row3.setPadding(insets);

        root.getChildren().add(glPane);
        root.getChildren().addAll(row0, row1, row2, row3);

        root.setMinSize(config.getResolutionX(), config.getResolutionY());
        toggleDynamicGraph.setOnAction(event -> {
            this.isMovingGraph = this.isMovingGraph.getInverted();
            LogSystem.log("GCalc", "Toggle dynamic/static graph: " + isMovingGraph);
        });
        updateGraph.setOnAction(event -> {
            expressionBuilder = new ExpressionBuilder(graphEquation.getText()).variables("x", "y", "a");
            try {
                expressionBuilder.build();
                this.isExpressionFailedToBuild.setText("");
            } catch (Exception e) {
                this.isExpressionFailedToBuild.setText("Failed to parse expression.");
                LogSystem.exception("Expression Build", e);
            }
            Config newConfig = new Config(config.getAntialiasingSamples(), config.getVSync(),
                    graphEquation.getText(),
                    (int) (aParamSlider.getValue()),
                    new ParamRange(Utils.parseInt(aRangeField.getText()), false),
                    new ParamRange(Utils.parseInt(xRangeField.getText()), false),
                    new ParamRange(Utils.parseInt(yRangeField.getText()), false),
                    config.getResolutionX(),
                    config.getResolutionY(),
                    Integer.parseInt(String.valueOf(Utils.limitSizeOfModel(Utils.parseInt(staticMeshResolutionField.getText())))),
                    Integer.parseInt(String.valueOf(Utils.limitSizeOfModel(Utils.parseInt(dynamicMeshResolutionField.getText())))),
                    config.getDebug());
            try {
                ConfigManager.writeConfig(newConfig);
            } catch (Exception e) {
                LogSystem.exception("Config IO", e);
            }
            ConfigManager.loadConfigFromDisk();
            updateModel();
        });

        aParamSlider.setOnValueChangedEvent(event -> aState = (float) aParamSlider.getValue());

        xRangeField.setText(String.valueOf(config.getRangeX().getMax()));
        yRangeField.setText(String.valueOf(config.getRangeY().getMax()));
        aRangeField.setText(String.valueOf(config.getRangeA().getMax()));
        staticMeshResolutionField.setText(String.valueOf(config.getStaticMeshResolution()));
        dynamicMeshResolutionField.setText(String.valueOf(config.getDynamicMeshResolution()));

        graphEquation.setText(config.getLatestPrompt());

        return root;
    }

    public void setFramerate(Integer frames) {
        frameRate.setText("FPS: " + frames);
    }

    public void setAverageDynamicMeshProcessionTime(Long time) {
        this.averageDynamicMeshProcessionTime.setText("Average dynamic mesh processing time: " + ((double) time) / EngineManager.NANOSECOND + " seconds.");
    }

    public void setStaticMeshProcessionTime(Long time) {
        this.staticMeshProcessionTime.setText("Static mesh processing time: " + ((double) time) / EngineManager.NANOSECOND + " seconds.");
    }
}

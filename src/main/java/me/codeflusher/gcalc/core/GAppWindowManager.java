package me.codeflusher.gcalc.core;

import lwjgui.LWJGUI;
import lwjgui.LWJGUIUtil;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.LogSystem;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

public class GAppWindowManager {
    public static final Float FOV = (float) Math.toRadians(60);
    public static final Float Z_NEAR = 0.01f;
    public static final Float Z_FAR = 1000f;
    private final String title;
    private final Matrix4f projectionMatrix;
    private long window;
    private Window lwjguiWindow;
    private boolean resizeable;

    public GAppWindowManager(String title) {
        this.title = title;
        projectionMatrix = new Matrix4f();
    }

    public void initializeLWJGUIWindow() {

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize the GLFW");
        }

        window = LWJGUIUtil.createOpenGLCoreWindow(Constants.APP_NAME, 1, 1, true, true);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, ConfigManager.getConfig().getAntialiasingSamples());

        LogSystem.log("Window instance", window);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        this.lwjguiWindow = LWJGUI.initialize(window);

        lwjguiWindow.setResizible(true);
        Config config = ConfigManager.getConfig();

        this.show();



        lwjguiWindow.setScene(new Scene(GCalcCore.getApplicationInstance().createUI(), config.getResolutionX(), config.getResolutionY()));
        LogSystem.log("Window", "Window manager finished starting the window");
    }

    public void update() {
        int height = getHeight();
        int width = getWidth();
        Config config = ConfigManager.getConfig();
        if (config.getResolutionX() != width || config.getResolutionY() != height) {
            LogSystem.debugLog("TestHeight", height + " " + config.getResolutionY());
            LogSystem.debugLog("TestHeight", width + " " + config.getResolutionX());
            try {
                ConfigManager.writeConfig(new Config(config.getAntialiasingSamples(),config.getVSync(), config.getLatestPrompt(), config.getASliderState(), config.getRangeA(), config.getRangeX(), config.getRangeY(), width, height, config.getStaticMeshResolution(), config.getDynamicMeshResolution(), config.getDebug()));
                LogSystem.log("Config IO", "Successfully update the config");
                ConfigManager.loadConfigFromDisk();
                config = ConfigManager.getConfig();
                lwjguiWindow.setScene(new Scene(GCalcCore.getApplicationInstance().createUI(), config.getResolutionX(), config.getResolutionY()));
            } catch (Exception e) {
                LogSystem.log("Window", "Failed to update config.");
                LogSystem.exception("Window", e);
            }
        }

        LWJGUI.render();
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL46.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public int getWidth() {
        return lwjguiWindow.getContext().getWidth();
    }

    public int getHeight() {
        return lwjguiWindow.getContext().getHeight();
    }

    public long getWindow() {
        return window;
    }

    public boolean isResizeable() {
        return resizeable;
    }

    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) getWidth() / getHeight();
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix) {
        float aspectRatio = (float) getWidth() / getHeight();
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public void show() {
        lwjguiWindow.show();
    }

    public Window getLwjguiWindow() {
        return lwjguiWindow;
    }
}

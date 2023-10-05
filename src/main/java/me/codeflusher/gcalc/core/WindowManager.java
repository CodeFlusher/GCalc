package me.codeflusher.gcalc.core;

import lwjgui.LWJGUI;
import lwjgui.LWJGUIUtil;
import lwjgui.scene.Context;
import lwjgui.scene.Node;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.StackPane;
import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.util.LogSystem;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {
    public static final Float FOV = (float) Math.toRadians(60);
    public static final Float Z_NEAR = 0.01f;
    public static final Float Z_FAR = 1000f;
    private final String title;
    private long window;
    private boolean resizeable;
    private final Matrix4f projectionMatrix;
    private Window lwjguiWindow;

    public WindowManager(String title) {
        this.title = title;
        projectionMatrix = new Matrix4f();
    }

    public void initializeLWJGUIWindow(){

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()){
            throw new IllegalStateException("Failed to initialize the GLFW");
        }

        Config config = ConfigManager.getConfig();

        window = LWJGUIUtil.createOpenGLCoreWindow(title, config.getResolutionX(), config.getResolutionY(), true, true, config.getVSync());

        LogSystem.log("Window instance", window );
        if (window == MemoryUtil.NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }
        lwjguiWindow = LWJGUI.initialize(window);

        GCalcCore.getApplicationInstance().createUI(lwjguiWindow.getScene());

        lwjguiWindow.show();

        LogSystem.log("Window", "Finalized to initialize the window");
    }
    public void update(){
//        GLFW.glfwSwapBuffers(window);
//        GLFW.glfwPollEvents();
        int height = lwjguiWindow.getContext().getHeight();
        int width = lwjguiWindow.getContext().getWidth();
        Config config = ConfigManager.getConfig();
        if (config.getResolutionX() != width || config.getResolutionY() != height){
            LogSystem.debugLog("TestHeight", height + " " + config.getResolutionY());
            LogSystem.debugLog("TestHeight", width + " " + config.getResolutionX());
            try {
                ConfigManager.writeConfig(new Config(config.getVSync(), config.getLatestPrompt(), config.getASliderState(), config.getRangeX(), config.getRangeY(), config.getRangeZ(), width, height,config.getDebug()));
                lwjguiWindow.getScene().setRoot(new StackPane());
                ConfigManager.loadConfigFromDisk();
                GCalcCore.getApplicationInstance().createUI(lwjguiWindow.getScene());
            } catch (Exception e){
                LogSystem.log("Window", "Failed to update config.");
            }
        }
        LWJGUI.render();
    }

    public void cleanup(){

        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r,float g,float b,float a){
        GL46.glClearColor(r,g,b,a);
    }

    public boolean isKeyPressed(int keycode){
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose(){
        return GLFW.glfwWindowShouldClose(window);
    }

    public void setResizeable(boolean resizeable) {
        this.resizeable = resizeable;
    }

    public String getTitle() {
        return title;
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

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public void setTitle(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) getWidth() / getHeight();
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
    public Matrix4f updateProjectionMatrix(Matrix4f matrix){
        float aspectRatio = (float) getWidth() / getHeight();
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

}

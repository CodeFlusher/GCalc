package me.codeflusher.gcalc.core;

import lwjgui.LWJGUI;
import lwjgui.LWJGUIApplication;
import lwjgui.LWJGUIUtil;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import lwjgui.scene.control.Label;
import lwjgui.scene.layout.OpenGLPane;
import lwjgui.scene.layout.StackPane;
import me.codeflusher.gcalc.GCalcActivity;
import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.util.Config;
import me.codeflusher.gcalc.util.ConfigManager;
import me.codeflusher.gcalc.util.LogSystem;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {
    public static final Float FOV = (float) Math.toRadians(60);
    public static final Float Z_NEAR = 0.01f;
    public static final Float Z_FAR = 1000f;
    private final String title;
    private int width;
    private int height;
    private long window;
    private boolean resizeable;
    private boolean vSync;
    private final Matrix4f projectionMatrix;

    private static OpenGLPane glPane;

    private Window lwjguiWindow;

    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void initializeLWJGUIWindow(){

        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()){
            throw new IllegalStateException("Failed to initialize the GLFW");
        }

        Config config = ConfigManager.getConfig();

        window = LWJGUIUtil.createOpenGLCoreWindow(title, width, height, true, true, config.getVSync());

        LogSystem.log("Window instance", window );
        if (window == MemoryUtil.NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }
        lwjguiWindow = LWJGUI.initialize(window);

        GCalcCore.getApplicationInstance().createUI(lwjguiWindow.getScene());

        lwjguiWindow.show();

        LogSystem.log("Window", "Finalized to initialize the window");
    }

    public void initializeWindow(){
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()){
            throw new IllegalStateException("Failed to initialize the window");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 8);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 6);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximazed = false;
        if (width==0 || height == 0){
            height = 100;
            width = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximazed = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        LogSystem.log("Window instance", window );
        if (window == MemoryUtil.NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height)->{
            this.width = width;
            this.height = height;
            this.setResizeable(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods)->{
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE){
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        if (maximazed)
            GLFW.glfwMaximizeWindow(window);
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window,(vidMode.width() -width) /2, (vidMode.height() - height) /2);
        }

        GLFW.glfwMakeContextCurrent(window);

        if (isvSync())
            GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);
        GL.createCapabilities();
        GL46.glClearColor(0.0f,0.0f,0.0f,0.0f);
        GL46.glEnable(GL46.GL_DEPTH_TEST);
        GL46.glEnable(GL46.GL_STENCIL_TEST);
        GL46.glEnable(GL46.GL_MULTISAMPLE);
       // GL46.glEnable(GL46.GL_CULL_FACE);
        //GL46.glCullFace(GL46.GL_BACK);
    }

    public void update(){
//        GLFW.glfwSwapBuffers(window);
//        GLFW.glfwPollEvents();
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
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public boolean isResizeable() {
        return resizeable;
    }

    public boolean isvSync() {
        return vSync;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public void setTitle(String title){
        GLFW.glfwSetWindowTitle(window, title);
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height){
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

}

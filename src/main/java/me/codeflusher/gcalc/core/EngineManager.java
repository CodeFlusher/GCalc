package me.codeflusher.gcalc.core;

import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.core.application.MouseInput;
import me.codeflusher.gcalc.util.LogSystem;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECOND = 1000000000;
    private static final float FRAMERATE = 1000;
    private static final float frametime = 1 / FRAMERATE;
    private static int fps;
    private GAppWindowManager windowManager;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private IApplication logic;
    private int frames = 0;

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

    public void init() {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        windowManager = GCalcCore.getWindowManager();

        logic = GCalcCore.getApplicationInstance();
        mouseInput = new MouseInput();
        windowManager.initializeLWJGUIWindow();
        logic.setMouseInput(mouseInput);
        try {
            logic.init();
        } catch (Exception e) {
            LogSystem.exception("Engine Thread", e);
        }
        mouseInput.init();
    }

    public void startEngine() {
        this.init();
        this.runWindow();
    }

    public void runWindow() {
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;
        LogSystem.log("Engine loop", "Running the engine!");
        while (!windowManager.windowShouldClose()) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;
            input();

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            //input

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    //windowManager.setTitle(Constants.APP_NAME + " | FPS: " + frames);
                    logic.setFramerate(frames);

                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render) {
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void input() {
        mouseInput.input();
        logic.input();
    }

    private void render() {
        logic.render();
        windowManager.update();
    }

    public void update() {
        logic.update();
        windowManager.update();
    }

    private void cleanup() {
        logic.cleanup();
        LogSystem.log("Engine Loop", "Cleaning up after engine execution");
        errorCallback.free();
    }


}
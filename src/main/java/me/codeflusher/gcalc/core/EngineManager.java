package me.codeflusher.gcalc.core;

import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.core.application.MouseInput;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.LogSystem;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {
    public static final long NANOSECOND = 1000000000;
    private static final float FRAMERATE = 1000;
    private static int fps;
    private static float frametime = 1/ FRAMERATE;
    private boolean isRunning;
    private WindowManager window;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;
    private IApplication logic;
    private int frames = 0;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = GCalcCore.getWindow();
        logic = GCalcCore.getApplicationInstance();
        mouseInput = new MouseInput();
        window.initializeLWJGUIWindow();
        logic.init();
        mouseInput.init();
    }

    public void start() throws Exception {
        this.init();
        if (isRunning) {
            return;
        }
        this.run();
    }

    private void run() {
        this.isRunning = true;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;
        while (isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;
            input();

            unprocessedTime += passedTime/(double) NANOSECOND;
            frameCounter += passedTime;

            //input

            while (unprocessedTime>frametime){
                render = true;
                unprocessedTime -= frametime;

                //LogSystem.debugLog("Engine loop", ((Double)unprocessedTime).toString() + " " + frametime);

                if (window.windowShouldClose())
                    stop();

                if (frameCounter>=NANOSECOND) {
                    setFps(frames);
                    window.setTitle(Constants.APP_NAME + " | FPS: "+ frames);

                    frames = 0;
                    frameCounter = 0;
                }
            }
            if (render){
                update();
                render();
                frames++;
            }
        }
        cleanup();
    }
    private void stop() {
        if (!isRunning){
            return;
        }
        LogSystem.log("Engine Loop", "Shutdown!");
        isRunning = false;
    }

    private void input() {
        mouseInput.input();
        logic.input();
    }

    public int getCurrentFrame() {
        return frames;
    }

    private void render() {
        logic.render();
        window.update();
    }

    private void update(){
        logic.update(mouseInput);
    }

    private void cleanup(){
        logic.cleanup();
        LogSystem.log("Engine Loop", "Cleaning up after engine execution");
        window.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }


}
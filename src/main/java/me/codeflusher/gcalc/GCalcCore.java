package me.codeflusher.gcalc;

import lwjgui.LWJGUIApplication;
import lwjgui.Task;
import lwjgui.scene.Scene;
import lwjgui.scene.Window;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.core.EngineManager;
import me.codeflusher.gcalc.core.GAppWindowManager;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.LogSystem;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

public class GCalcCore {

    private static GAppWindowManager windowManager;
    private static IApplication applicationInstance;
    private static EngineManager engine;
    private static Window lwjguiWindow;

    public static void main(String[] args) {
        ConfigManager.firstLoadConfigFromDisk();
        LogSystem.getInstance(ConfigManager.getConfig().getDebug());
        LogSystem.debugLog("LWJGL Version", Version.getVersion());
        windowManager = new GAppWindowManager(Constants.APP_NAME);
        applicationInstance = new GCalcActivity();
        if (!GLFW.glfwInit()) {
            LogSystem.exception("Init", new IllegalStateException("Failed to initialize the GLFW"));
            return;
        }

        engine = new EngineManager();

        try {
            engine.startEngine();
        }catch (Exception e){
            LogSystem.exception("Init", e);
        }

    }

    public static GAppWindowManager getWindowManager() {
        return windowManager;
    }

    public static EngineManager getEngine() {
        return engine;
    }

    public static IApplication getApplicationInstance() {
        return applicationInstance;
    }

    public static void setLwjguiWindow(Window lwjguiWindow) {
        GCalcCore.lwjguiWindow = lwjguiWindow;
    }

    public static Window getLwjguiWindow() {
        return lwjguiWindow;
    }
}
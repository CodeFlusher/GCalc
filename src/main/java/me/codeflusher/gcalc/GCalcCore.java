package me.codeflusher.gcalc;

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
        } catch (Exception e) {
            LogSystem.exception("Init", e);
        }

    }

    public static GAppWindowManager getWindowManager() {
        return windowManager;
    }

    public static IApplication getApplicationInstance() {
        return applicationInstance;
    }

}
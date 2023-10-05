package me.codeflusher.gcalc;

import me.codeflusher.gcalc.core.EngineManager;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.core.WindowManager;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.LogSystem;
import org.lwjgl.Version;

public class GCalcCore {

    private static WindowManager window;
    private static IApplication applicationInstance;
    private static EngineManager engine;

    public static void main(String[] args) {
        ConfigManager.firstLoadConfigFromDisk();
        LogSystem.getInstance(ConfigManager.getConfig().getDebug());
        LogSystem.debugLog("LWJGL Version", Version.getVersion());
        window = new WindowManager(Constants.APP_NAME);

        applicationInstance = new GCalcActivity();

        engine = new EngineManager();

        try {
            engine.start();
        }catch (Exception e){
            e.printStackTrace();
        }

        window.cleanup();
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static EngineManager getEngine() {
        return engine;
    }

    public static IApplication getApplicationInstance() {
        return applicationInstance;
    }
}
package me.codeflusher.gcalc;

import me.codeflusher.gcalc.core.EngineManager;
import me.codeflusher.gcalc.core.IApplication;
import me.codeflusher.gcalc.core.WindowManager;
import me.codeflusher.gcalc.util.ConfigManager;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.LogSystem;
import org.lwjgl.Version;

public class GCalcCore {

    private static WindowManager window;
    private static IApplication applicationInstance;
    private static EngineManager engine;

    public static void main(String[] args) {
        LogSystem.getInstance(true);
        LogSystem.debugLog("LWJGL Version", Version.getVersion());
        ConfigManager.loadConfigFromDisk();
        window = new WindowManager(Constants.APP_NAME, 1600, 900, false);

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
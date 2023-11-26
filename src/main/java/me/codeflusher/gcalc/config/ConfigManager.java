package me.codeflusher.gcalc.config;

import com.google.gson.Gson;
import me.codeflusher.gcalc.util.LogSystem;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigManager {
    public static final String CONFIG_PATH = "config/config.json";
    private static Config config;

    private static void defaultConfig() {
        config = new Config(4,true, "sin(x+y)*a", 2, new ParamRange(5, false), new ParamRange(5, false), new ParamRange(5, false), 800, 600, 512, 64, false);
    }

    public static void loadConfigFromDisk() {
        File configFile = new File(CONFIG_PATH);

        LogSystem.log("Config IO", "Loading condig from: " + configFile.getAbsoluteFile());
        if (!configFile.exists()) {
            defaultConfig();
            try {
                writeConfig(getConfig());
            } catch (Exception e) {
                LogSystem.log("Config IO", "Failed to create new config!" + "\n" + Arrays.toString(e.getStackTrace()));
            }
            return;
        }

        Gson gson = new Gson();
        StringBuilder jsonConfigString = new StringBuilder();
        try {
            Files.readAllLines(Paths.get(CONFIG_PATH)).forEach(jsonConfigString::append);
        } catch (Exception e) {
            LogSystem.log("Config IO", "Failed to read the config!" + "\n" + Arrays.toString(e.getStackTrace()));
            defaultConfig();
            return;
        }
        config = gson.fromJson(jsonConfigString.toString(), Config.class);
        if (config == null) {
            defaultConfig();
            try {
                writeConfig(getConfig());
            } catch (Exception e) {
                LogSystem.exception("Config IO", e);
            }
        }
    }

    public static void firstLoadConfigFromDisk() {
        File configFile = new File(CONFIG_PATH);

        if (!configFile.exists()) {
            defaultConfig();
            try {
                firstWriteConfig(getConfig());
            } catch (Exception ignored) {

            }
            return;
        }

        Gson gson = new Gson();
        StringBuilder jsonConfigString = new StringBuilder();
        try {
            Files.readAllLines(Paths.get(CONFIG_PATH)).forEach(jsonConfigString::append);
        } catch (Exception e) {
            defaultConfig();
            return;
        }
        config = gson.fromJson(jsonConfigString.toString(), Config.class);
        if (config == null) {
            defaultConfig();
            try {
                firstWriteConfig(getConfig());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeConfig(Config config) throws Exception {
        File file = new File(CONFIG_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        Gson gson = new Gson();
        LogSystem.debugLog("Config IO", gson.toJson(config));
        writer.write(gson.toJson(config));
        writer.close();
    }

    public static void firstWriteConfig(Config config) throws Exception {
        File file = new File(CONFIG_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        Gson gson = new Gson();
        writer.write(gson.toJson(config));

        writer.close();
    }

    public static Config getConfig() {
        return config;
    }
}

package me.codeflusher.gcalc.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ConfigManager {
    private static Config config;
    public static final String CONFIG_PATH = "config/config.json";
    private static void defaultConfig(){
        config = new Config(false);
    }
    public static void loadConfigFromDisk(){
        File configFile = new File(CONFIG_PATH);

        LogSystem.log("Config IO", "Loading condig from: " + configFile.getAbsoluteFile());
        if(!configFile.exists()){
            defaultConfig();
            try {
                writeConfig(getConfig());
            }catch (Exception e){
                LogSystem.log("Config IO","Failed to create new config!" +"\n"+ Arrays.toString(e.getStackTrace()));
            }
            return;
        }

        Gson gson = new Gson();
        String jsonConfigString;
        try {
            jsonConfigString = Files.readString(Path.of(CONFIG_PATH));
        }catch (Exception e){
            LogSystem.log("Config IO","Failed to read the config!" +"\n"+ Arrays.toString(e.getStackTrace()));
            defaultConfig();
            return;
        }
        config = gson.fromJson(jsonConfigString, Config.class);
        if (config == null){
            defaultConfig();
            try {
                writeConfig(getConfig());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeConfig(Config config) throws Exception{
        File file = new File(CONFIG_PATH);
        if (!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        Gson gson = new Gson();
        LogSystem.log("Config IO", gson.toJson(config));
        writer.write(gson.toJson(config));
        writer.close();
    }
    public static Config getConfig() {
        return config;
    }
}

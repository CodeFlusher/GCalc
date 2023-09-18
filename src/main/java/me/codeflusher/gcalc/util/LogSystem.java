package me.codeflusher.gcalc.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.logging.Logger;

public class LogSystem {
    private static class Logger {
        public static void info(Object o){
            System.out.println(o);
        }
    }

    interface LogRunnable{
        void run(String namespace, Object message);
    }
    private static LogRunnable debugRunnable;
    private static LogRunnable logRunnable;
    private static LogSystem instance;
    private LogSystem(){

    }
    public static void getInstance(boolean debugAllowed){
        if (instance != null){
            return;
        }
        logRunnable = (namespace, message) -> Logger.info("["+ Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis())) + " | "+namespace+"]: "+message.toString());
        if (debugAllowed){
            debugRunnable = logRunnable;
        }else {
            debugRunnable = (namespace, message) -> {

            };
        }
        instance = new LogSystem();
    }

    public static void log(String namespace, Object object){
        logRunnable.run(namespace, object);
    }
    public static void debugLog(String namespace, Object object){
        debugRunnable.run(namespace, object);
    }

}

package me.codeflusher.gcalc.util;

import java.sql.Timestamp;
import java.time.Instant;

public class LogSystem {
    private static LogRunnable debugRunnable;
    private static LogRunnable logRunnable;
    private static LogSystem instance;
    private LogSystem() {

    }

    public static void getInstance(boolean debugAllowed) {
        if (instance != null) {
            return;
        }
        logRunnable = (namespace, message) -> Logger.info("[" + Timestamp.from(Instant.ofEpochMilli(System.currentTimeMillis())) + " | " + namespace + "]: " + message.toString());
        if (debugAllowed) {
            debugRunnable = logRunnable;
        } else {
            debugRunnable = (namespace, message) -> {

            };
        }
        instance = new LogSystem();
    }

    public static void log(String namespace, Object object) {
        logRunnable.run("INFO | " + namespace, object);
    }
    public static void exception(String namespace, Exception object) {
        StackTraceElement[] elements = object.getStackTrace();
        errLog(namespace, object.getMessage());
        for(StackTraceElement element : elements){
            errLog(namespace, element);
        }
    }

    public static void debugLog(String namespace, Object object) {
        debugRunnable.run("DEBUG | " + namespace, object);
    }
    public static void errLog(String namespace, Object object) {
        debugRunnable.run("ERROR | " + namespace, object);
    }
    public static void warnLog(String namespace, Object object) {
        debugRunnable.run("WARN | " + namespace, object);
    }

    interface LogRunnable {
        void run(String namespace, Object message);
    }

    private static class Logger {
        public static void info(Object o) {
            System.out.println(o);
        }
    }

}

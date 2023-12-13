package me.codeflusher.gcalc.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String APP_NAME = "GCalc Pre-release";
    public static final Integer MODEL_SIZE = 25;
    public static final Integer ARROW_TIP_SIZE = 1;
    public static final Identifier MODEL_IDENTIFIER = new Identifier("graph_model");
    public static final Identifier LINE_X_IDENTIFIER = new Identifier("line_x");
    public static final Identifier LINE_Y_IDENTIFIER = new Identifier("line_y");
    public static final Identifier LINE_Z_IDENTIFIER = new Identifier("line_z");
    public static float MOUSE_SENSITIVITY = 0.2f;
    public static float MAX_FLIGHT_SPEED = 0.5f;
    public static float MIN_FLIGHT_SPEED = 0.01f;
    public static Map<Character, Integer> NUM_MAP = new HashMap<Character, Integer>() {
        {
            put('0', 0);
            put('1', 1);
            put('2', 2);
            put('3', 3);
            put('4', 4);
            put('5', 5);
            put('6', 6);
            put('7', 7);
            put('8', 8);
            put('9', 9);
        }
    };
}

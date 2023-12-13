package me.codeflusher.gcalc.util;

import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.config.ParamRange;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static FloatBuffer storeDatgaInFloatBuffer(float[] data) {
        FloatBuffer fb = MemoryUtil.memAllocFloat(data.length);
        fb.put(data).flip();
        return fb;
    }

    public static IntBuffer storeDatgaInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static int parseInt(String s) {
        StringBuilder builder = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Constants.NUM_MAP.getOrDefault(c, -1) != -1) {
                builder.append(c);
            }
        }
        return Integer.parseInt(builder.toString());
    }

    public static float clampFloat(float value, float minimum, float maximum) {
        if (value < minimum) return minimum;
        return Math.min(value, maximum);
    }

    public static int limitSizeOfModel(int inputSize) {
        if (inputSize < 16) {
            return 16;
        }
        return Math.min(inputSize, 1536);
    }

    public static float[] toFloatArray(List<Float> floatList) {
        float[] floatArray = new float[floatList.size()];
        int i = 0;

        for (Float f : floatList) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }

    public static float[] combineArrays(float[] firstArray, float[] secondArray) {

        int fal = firstArray.length;
        int sal = secondArray.length;
        float[] result = new float[fal + sal];
        System.arraycopy(firstArray, 0, result, 0, fal);
        System.arraycopy(secondArray, 0, result, fal, sal);
        return result;
    }

    public static int[] toIntArray(List<Integer> integers) {
        int[] intArray = new int[integers.size()];
        int i = 0;

        for (Integer integer : integers) {
            intArray[i++] = integer;
        }
        return intArray;
    }

    public static String loadResources(String filename) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(filename);
             Scanner scanner = new Scanner(in, String.valueOf(StandardCharsets.UTF_8))) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
}

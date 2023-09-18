package me.codeflusher.gcalc.util;

import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {
    public static FloatBuffer storeDatgaInFloatBuffer(float[] data){
        FloatBuffer fb = MemoryUtil.memAllocFloat(data.length);
        fb.put(data).flip();
        return fb;
    }
    public static IntBuffer storeDatgaInIntBuffer(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static float[] toFloatArray(List<Float> floatList){
        float[] floatArray = new float[floatList.size()];
        int i = 0;

        for (Float f : floatList) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }
    public static int[] toIntArray(List<Integer> integers){
        int[] intArray = new int[integers.size()];
        int i = 0;

        for (Integer integer : integers) {
            intArray[i++] = integer.intValue();
        }
        return intArray;
    }

    public static String loadResources(String filename) throws Exception{
        String result;
        try(InputStream in = Utils.class.getResourceAsStream(filename);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())){
            result = scanner.useDelimiter("\\A").next();
            }
        return  result;
    }
}

package me.codeflusher.gcalc.entity;

import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.Utils;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

public class Models {

    //im lazy to do the maths
    public static float[] getArrowTipVertices(float x, float y, float z) {
        if (x != 0) {
            return new float[]{
                    x, y, z,
                    (float) (x - Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (y + Constants.ARROW_TIP_SIZE * 1.414 / 2), z,
                    x, y, z,
                    (float) (x - Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (y - Constants.ARROW_TIP_SIZE * 1.414 / 2), z,
                    x, y, z,
                    (float) (x - Constants.ARROW_TIP_SIZE * 1.414 / 2), y, (float) (z + Constants.ARROW_TIP_SIZE * 1.414 / 2),
                    x, y, z,
                    (float) (x - Constants.ARROW_TIP_SIZE * 1.414 / 2), y, (float) (z - Constants.ARROW_TIP_SIZE * 1.414 / 2),
            };
        } else if (y != 0) {
            return new float[]{
                    x, y, z,
                    (float) (x + Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (y - Constants.ARROW_TIP_SIZE * 1.414 / 2), z,
                    x, y, z,
                    (float) (x - Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (y - Constants.ARROW_TIP_SIZE * 1.414 / 2), z,
                    x, y, z,
                    x, (float) (y - Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (z + Constants.ARROW_TIP_SIZE * 1.414 / 2),
                    x, y, z,
                    x, (float) (y - Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (z - Constants.ARROW_TIP_SIZE * 1.414 / 2),
            };
        } else {
            return new float[]{
                    x, y, z,
                    x, (float) (y - Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (z - Constants.ARROW_TIP_SIZE * 1.414 / 2),
                    x, y, z,
                    x, (float) (y + Constants.ARROW_TIP_SIZE * 1.414 / 2), (float) (z - Constants.ARROW_TIP_SIZE * 1.414 / 2),
                    x, y, z,
                    (float) (x - Constants.ARROW_TIP_SIZE * 1.414 / 2), y, (float) (z - Constants.ARROW_TIP_SIZE * 1.414 / 2),
                    x, y, z,
                    (float) (x + Constants.ARROW_TIP_SIZE * 1.414 / 2), y, (float) (z - Constants.ARROW_TIP_SIZE * 1.414 / 2),
            };
        }
    }

    public static Model createArrow(float x, float y, float z, Vector3f color) {
        float[] vertices = {
                -x, -y, -z,
                x, y, z,
        };

        int[] indices = {
                1, 2,
                3, 4,
                5, 6,
                7, 8,
                9, 10,


        };

        vertices = Utils.combineArrays(vertices, getArrowTipVertices(x, y, z));

        ObjectLoader loader = new ObjectLoader();
        loader.cleanup();
        Model model = loader.loadModel(vertices, indices);
        model.setColor(color);
        model.setRenderType(GL46.GL_LINES);
        return model;
    }
}

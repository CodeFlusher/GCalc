package me.codeflusher.gcalc.entity;

import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.config.ParamRange;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.Direction;
import me.codeflusher.gcalc.util.LogSystem;
import me.codeflusher.gcalc.util.Utils;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;

import java.util.ArrayList;

public class Models {

    //im lazy to do the maths
    public static float[] getArrowTipVertices(float x, float y, float z) {
        if (x != 0) {
            return new float[]{
                    x, y, z,
                    x - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y + Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z,
                    x, y, z,
                    x - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z,
                    x, y, z,
                    x - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y, z + Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
                    x, y, z,
                    x - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y, z - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
            };
        } else if (y != 0) {
            return new float[]{
                    x, y, z,
                    x + Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z,
                    x, y, z,
                    x - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z,
                    x, y, z,
                    x, y - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z + Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
                    x, y, z,
                    x, y - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
            };
        } else {
            return new float[]{
                    x, y, z,
                    x, y - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
                    x, y, z,
                    x, y + Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, z - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
                    x, y, z,
                    x - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y, z - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
                    x, y, z,
                    x + Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2, y, z - Constants.ARROW_TIP_SIZE * Constants.APPROXIMATE_ROOT_OF_2 / 2,
            };
        }
    }

    public static ArrayList<Float> createLine(Direction dir, Integer lenght, float x, float y, float z) {
        return new ArrayList<Float>() {
            {
                add((float) (lenght * dir.getxMult()) + x);
                add((float) (lenght * dir.getyMult()) + y);
                add((float) (lenght * dir.getzMult()) + z);
                add((float) (-1 * lenght * dir.getxMult()) + x);
                add((float) (-1 * lenght * dir.getyMult()) + y);
                add((float) (-1 * lenght * dir.getzMult()) + z);
            }
        };
    }

    public static Model createGrid(Direction dir) {
        ArrayList<Float> vertices = new ArrayList<>();

        Config config = ConfigManager.getConfig();
        ParamRange range;
        if (dir == Direction.Z)
            range = new ParamRange(5, false);
        else
            range = dir == Direction.X ? config.getRangeX() : config.getRangeY();

        float step = (float) Constants.MODEL_SIZE / (2 * range.getMax());

        switch (dir) {
            case X:
                for (int i = -1*range.getMax(); i <= range.getMax(); i++) {
                    vertices.addAll(createLine(dir, (int) (Constants.MODEL_SIZE / Constants.APPROXIMATE_ROOT_OF_3), 0, 0, i * step));
                    vertices.addAll(createLine(Direction.Y, (int) (Constants.MODEL_SIZE /  Constants.APPROXIMATE_ROOT_OF_3), 0, i * step, 0));
                }
            case Y:
                for (int i = -1*range.getMax(); i <= range.getMax(); i++) {
                    vertices.addAll(createLine(dir, (int) (Constants.MODEL_SIZE /  Constants.APPROXIMATE_ROOT_OF_3), 0, 0, i * step));
                    vertices.addAll(createLine(Direction.Y, (int) (Constants.MODEL_SIZE /  Constants.APPROXIMATE_ROOT_OF_3), i * step, 0, 0));
                }
            case Z:
                for (int i = -1*range.getMax(); i <= range.getMax(); i++) {
                    vertices.addAll(createLine(dir, (int) (Constants.MODEL_SIZE /  Constants.APPROXIMATE_ROOT_OF_3), 0, i * step, 0));
                    vertices.addAll(createLine(Direction.Y, (int) (Constants.MODEL_SIZE /  Constants.APPROXIMATE_ROOT_OF_3), i * step, 0, 0));
                }
        }


        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 1; i <= vertices.size() / 2; i++) {
            indices.add(i);
        }

        ObjectLoader loader = new ObjectLoader();

        LogSystem.debugLog("Grid maker", "Grid verticies lenght: ", vertices.size(), "Indicies lenght: ", indices.size());

        Model model = loader.loadModel(Utils.toFloatArray(vertices), Utils.toIntArray(indices));
        model.setColor(new Vector3f(0.2f, 0.2f, 0.2f));
        model.setOpacity(0.25f);
        model.setRenderType(GL46.GL_LINES);
        return model;
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

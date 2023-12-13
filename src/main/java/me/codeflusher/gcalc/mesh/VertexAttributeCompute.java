package me.codeflusher.gcalc.mesh;

import me.codeflusher.gcalc.config.Config;
import me.codeflusher.gcalc.config.ConfigManager;
import me.codeflusher.gcalc.config.ParamRange;
import me.codeflusher.gcalc.util.Constants;

public class VertexAttributeCompute {
    private final ParamRange rangeOnX;
    private final ParamRange rangeOnY;
    private final float scaleX;
    private final float vertexDensity;
    private final float scaleY;
    private final int resolution;
    private final boolean isStatic;

    public VertexAttributeCompute(boolean isStatic) {
        Config config = ConfigManager.getConfig();
        this.rangeOnX = config.getRangeX();
        this.rangeOnY = config.getRangeY();
        this.isStatic = isStatic;
        this.resolution = (isStatic ? config.getStaticMeshResolution() : config.getDynamicMeshResolution());
        this.vertexDensity = ((float) Constants.MODEL_SIZE / resolution);
        this.scaleX = (float) (resolution / rangeOnX.getMax());
        this.scaleY = (float) (resolution / rangeOnY.getMax());
    }

    public float getVertexPosition(Integer i) {
        return (i - (float) resolution / 2) * vertexDensity;
    }

    public float modifyX(Integer x) {
        return (x - (float) resolution / 2) / scaleX;
    }

    public float modifyY(Integer y) {
        return (y - (float) resolution / 2) / scaleY;
    }
    public int getResolution() {
        return resolution;
    }

    public boolean isStatic() {
        return isStatic;
    }

    @Override
    public String toString() {
        return "VertexAttributeCompute{" +
                "rangeOnX=" + rangeOnX +
                ", rangeOnY=" + rangeOnY +
                ", scaleX=" + scaleX +
                ", vertexDensity=" + vertexDensity +
                ", scaleY=" + scaleY +
                ", resolution=" + resolution +
                ", isStatic=" + isStatic +
                '}';
    }
}

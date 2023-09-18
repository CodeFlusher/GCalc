package me.codeflusher.gcalc.util;

import me.codeflusher.gcalc.user.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    public static Matrix4f getViewMatrix(Camera camera){
        Vector3f position = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.identity();
        matrix4f.rotate((float)Math.toRadians(rotation.x), new Vector3f(1,0,0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0,1,0))
                .rotate((float)Math.toRadians(rotation.z), new Vector3f(0,0,1));
        matrix4f.translate(-position.x, -position.y, -position.z);
        return matrix4f;
    }
}

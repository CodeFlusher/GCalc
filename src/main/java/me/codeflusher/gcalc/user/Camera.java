package me.codeflusher.gcalc.user;

import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.Utils;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;

    public Camera() {
        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
    }

    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void movePosition(float x, float y, float z) {
        if (z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * z;
        }
        if (x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        position.y += y;
        position.x = Utils.clampFloat(position.x, -1 * Constants.MODEL_SIZE, Constants.MODEL_SIZE);
        position.y = Utils.clampFloat(position.y, -1 * Constants.MODEL_SIZE, Constants.MODEL_SIZE);
        position.z = Utils.clampFloat(position.z, -1 * Constants.MODEL_SIZE, Constants.MODEL_SIZE);
    }

    public void setPosition(float x, float y, float z) {
        position.x = Utils.clampFloat(x, -1 * Constants.MODEL_SIZE, Constants.MODEL_SIZE);
        position.y = Utils.clampFloat(y, -1 * Constants.MODEL_SIZE, Constants.MODEL_SIZE);
        position.z = Utils.clampFloat(z, -1 * Constants.MODEL_SIZE, Constants.MODEL_SIZE);
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void moveRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}

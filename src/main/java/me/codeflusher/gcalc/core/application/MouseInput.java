package me.codeflusher.gcalc.core.application;

import me.codeflusher.gcalc.GCalcCore;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2f previousPos;
    private final Vector2f currentPos;
    private final Vector2f displayVector;


    public MouseInput() {
        this.previousPos = new Vector2f(-1, -1);
        this.currentPos = new Vector2f(0, 0);
        this.displayVector = new Vector2f();
    }

    public void init() {
        long windowHandle = GCalcCore.getWindowManager().getWindow();
        GLFW.glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            currentPos.x = (float) xpos;
            currentPos.y = (float) ypos;
        });
    }

    public void input() {
        displayVector.x = 0;
        displayVector.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0) {
            double x = currentPos.x - previousPos.x;
            double y = currentPos.y - previousPos.y;

            boolean rotateX = x != 0;
            boolean rotateY = y != 0;
            if (rotateX) {
                displayVector.y = (float) x;
            }
            if (rotateY) {
                displayVector.x = (float) y;
            }

        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
    }

    public Vector2f getDisplayVector() {
        return displayVector;
    }

    @Override
    public String toString() {
        return "MouseInput{" +
                "previousPos=" + previousPos +
                ", currentPos=" + currentPos +
                ", displayVector=" + displayVector +
                '}';
    }
}



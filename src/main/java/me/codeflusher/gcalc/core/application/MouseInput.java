package me.codeflusher.gcalc.core.application;

import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.core.GAppWindowManager;
import me.codeflusher.gcalc.util.LogSystem;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseInput {
    private final Vector2f previousPos;
    private final Vector2f currentPos;
    private final Vector2f displayVector;

    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

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
//
//        GLFW.glfwSetCursorEnterCallback(windowHandle, (window, entered) -> {
//            inWindow = entered;
//        });
//        GLFW.glfwSetMouseButtonCallback(windowHandle, (window, button, action, mods) -> {
//            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
//            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
//        });
    }

    public void update(float xpos, float ypos){
        currentPos.x = xpos;
        currentPos.y = ypos;
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
//        LogSystem.debugLog("Mouse Input", this);
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
//        currentPos.x = 0;
//        currentPos.y = 0;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    public Vector2f getPreviousPos() {
        return previousPos;
    }

    public Vector2f getCurrentPos() {
        return currentPos;
    }

    public Vector2f getDisplayVector() {
        //LogSystem.debugLog("Get display vector", displayVector);
        return displayVector;
    }

    public boolean isInWindow() {
        return inWindow;
    }

    @Override
    public String toString() {
        return "MouseInput{" +
                "previousPos=" + previousPos +
                ", currentPos=" + currentPos +
                ", displayVector=" + displayVector +
                ", inWindow=" + inWindow +
                ", leftButtonPressed=" + leftButtonPressed +
                ", rightButtonPressed=" + rightButtonPressed +
                '}';
    }
}



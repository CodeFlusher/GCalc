package me.codeflusher.gcalc.core;

import com.sun.tools.javac.Main;
import me.codeflusher.gcalc.GCalcCore;
import org.joml.Vector2d;
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
        this.previousPos = new Vector2f(-1,-1);
        this.currentPos = new Vector2f(0,0);
        this.displayVector = new Vector2f();
    }

    public void init() {
        GLFW.glfwSetCursorPosCallback(GCalcCore.getWindow().getWindow(), (window, xpos, ypos) -> {
            currentPos.x = (float)xpos;
            currentPos.y = (float)ypos;
        });

        GLFW.glfwSetCursorEnterCallback(GCalcCore.getWindow().getWindow(), (window, entered) -> {
            inWindow = entered;
        });
        GLFW.glfwSetMouseButtonCallback(GCalcCore.getWindow().getWindow(), (window, button, action, mods) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }
    public void input(){
        displayVector.x = 0;
        displayVector.y =0;
        if (previousPos.x >0 && previousPos.y >0 && inWindow){
            double x = currentPos.x - previousPos.x;
            double y = currentPos.y - previousPos.y;

            boolean rotateX = x!=0;
            boolean rotateY = y!=0;
            if (rotateX){
                displayVector.y = (float)x;
            }
            if (rotateY){
                displayVector.x = (float)y;
            }

        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
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
        return displayVector;
    }

    public boolean isInWindow() {
        return inWindow;
    }
}



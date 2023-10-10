package me.codeflusher.gcalc.core.application;

import lwjgui.scene.Scene;
import lwjgui.scene.layout.Pane;

public interface IApplication {
    void init() throws Exception;

    void input();

    void update();
    void setMouseInput(MouseInput input);

    void render();

    void cleanup();

    AppScene getScene();

    Pane createUI();
}

package me.codeflusher.gcalc.core;

import lwjgui.scene.Scene;

public interface IApplication {
    void init() throws Exception;
    void input();
    void update(MouseInput input);
    void render();
    void cleanup();
    AppScene getScene();
    void createUI(Scene scene);
}

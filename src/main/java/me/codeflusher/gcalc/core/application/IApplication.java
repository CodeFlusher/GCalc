package me.codeflusher.gcalc.core.application;

import lwjgui.scene.layout.Pane;


/**
 * Interface that represents basic app that can be launched on top of the engine
 *
 * @author CodeFlusher
 * @version 1.0
 **/
public interface IApplication {
    void init() throws Exception;

    void input();

    void update();

    void setMouseInput(MouseInput input);

    void setFramerate(Integer framerate);

    void render();

    void cleanup();

    AppScene getScene();

    Pane createUI();
}

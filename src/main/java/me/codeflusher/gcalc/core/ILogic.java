package me.codeflusher.gcalc.core;

public interface ILogic {
    void init() throws Exception;
    void input();
    void update(MouseInput input);
    void render();
    void cleanup();
}

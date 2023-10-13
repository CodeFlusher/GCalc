package me.codeflusher.gcalc.core;

import lwjgui.scene.Context;
import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.core.application.AppScene;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.util.Constants;
import me.codeflusher.gcalc.util.Transformation;
import me.codeflusher.gcalc.util.Utils;
import org.lwjgl.opengl.GL46;

public class RenderManager implements IGRender {
    private final GAppWindowManager window;
    private ShaderManager shader;
    private IApplication application;

    public RenderManager() {
        window = GCalcCore.getWindowManager();
    }

    @Override
    public void initializeRendering() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResources("/shader/vertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shader/fragment.fsh"));
        shader.link();
        shader.creteUniform("projectionMatrix");
        shader.creteUniform("viewMatrix");


        this.application = GCalcCore.getApplicationInstance();
    }

    public void drawAxes() {

    }

    public void clear() {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup();
    }

    @Override
    public void render(Context context) {
        clear();
        //shader.bind();

        AppScene scene = application.getScene();
        Model model = scene.getMap().getActor(Constants.MODEL_IDENTIFIER);

        shader.bind();
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(scene.getCamera()));
        drawAxes();

        GL46.glDisable(GL46.GL_CULL_FACE);
        GL46.glDisable(GL46.GL_SCISSOR_TEST);
        GL46.glDisable(GL46.GL_STENCIL_TEST);
        GL46.glDisable(GL46.GL_DEPTH_TEST);
        GL46.glDisable(GL46.GL_FRAMEBUFFER);

        GL46.glBindVertexArray(model.getId());
        GL46.glEnableVertexAttribArray(0);
        GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, model.getVertexCounter());
        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);
        shader.unbind();
    }
}

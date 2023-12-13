package me.codeflusher.gcalc.core;

import lwjgui.scene.Context;
import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.core.application.AppScene;
import me.codeflusher.gcalc.core.application.IApplication;
import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.util.Transformation;
import me.codeflusher.gcalc.util.Utils;
import org.lwjgl.opengl.GL46;

import java.util.Collection;

import static org.lwjgl.opengl.GL46.*;


public class RenderManager implements IGRender {
    private final GAppWindowManager window;
    private ShaderManager shader;
    private ShaderManager lineShader;
    private IApplication application;

    public RenderManager() {
        window = GCalcCore.getWindowManager();
    }

    @Override
    public void initializeRendering() throws Exception {
        lineShader = new ShaderManager();
        lineShader.createFragmentShader(Utils.loadResources("/shader/line_fragment.fsh"));
        lineShader.createVertexShader(Utils.loadResources("/shader/line_vertex.vsh"));
        lineShader.link();
        lineShader.creteUniform("projectionMatrix");
        lineShader.creteUniform("viewMatrix");
        lineShader.creteUniform("color");
        lineShader.creteUniform("opacity");
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResources("/shader/vertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shader/fragment.fsh"));
        shader.link();
        shader.creteUniform("projectionMatrix");
        shader.creteUniform("opacity");
        shader.creteUniform("viewMatrix");

        this.application = GCalcCore.getApplicationInstance();
        assert application != null;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shader.cleanup();
    }

    @Override
    public void render(Context context) {
        clear();

        glEnable(GL46.GL_DEPTH_TEST);
        glEnable(GL46.GL_MULTISAMPLE);
        glEnable(GL46.GL_STENCIL_TEST);

        AppScene scene = application.getScene();

        shader.bind();
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(scene.getCamera()));

        Collection<Model> triangleModels = scene.getMap().getActorMapByRenderType(GL46.GL_TRIANGLES).values();

        for (Model model : triangleModels) {

            glBindVertexArray(model.getId());
            glEnableVertexAttribArray(0);

            shader.setUniform("opacity", model.getOpacity());

            glDrawArrays(model.getRenderType(), 0, model.getVertexCounter());

            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        }

        shader.unbind();

        //line render calls

        lineShader.bind();
        lineShader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        lineShader.setUniform("viewMatrix", Transformation.getViewMatrix(scene.getCamera()));
        lineShader.setUniform("color", Transformation.getViewMatrix(scene.getCamera()));

        Collection<Model> graphLines = scene.getMap().getActorMapByRenderType(GL46.GL_LINES).values();

        for (Model graphLine : graphLines) {

            glBindVertexArray(graphLine.getId());
            glEnableVertexAttribArray(0);
            glLineWidth(10f);

            lineShader.setUniform("color", graphLine.getColor());
            lineShader.setUniform("opacity", graphLine.getOpacity());

            glDrawArrays(graphLine.getRenderType(), 0, graphLine.getVertexCounter());

            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        }
        lineShader.unbind();

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}

package me.codeflusher.gcalc.core;

import me.codeflusher.gcalc.GCalcCore;
import me.codeflusher.gcalc.entity.Model;
import me.codeflusher.gcalc.user.Camera;
import me.codeflusher.gcalc.util.Transformation;
import me.codeflusher.gcalc.util.Utils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GLUtil;

public class RenderManager {
    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager(){
        window = GCalcCore.getWindow();
    }

    public void initializeRendering() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResources("/shader/vertex.vsh"));
        shader.createFragmentShader(Utils.loadResources("/shader/fragment.fsh"));
        shader.link();
        shader.creteUniform("projectionMatrix");
        shader.creteUniform("viewMatrix");
    }

    public void render(Model model, Camera camera){
        clear();
        shader.bind();

        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        drawAxes();
        GL46.glBindVertexArray(model.getId());
        GL46.glEnableVertexAttribArray(0);
        GL46.glDrawArrays(GL46.GL_TRIANGLES, 0, model.getVertexCounter());
        GL46.glDisableVertexAttribArray(0);
        GL46.glBindVertexArray(0);
        shader.unbind();

    }

    public void drawAxes(){
    
    }
    public void clear(){
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup(){
        shader.cleanup();
    }
}

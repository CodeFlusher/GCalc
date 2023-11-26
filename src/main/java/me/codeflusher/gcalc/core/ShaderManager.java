package me.codeflusher.gcalc.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager {

    private final int programID;
    private final Map<String, Integer> uniforms;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderManager() throws Exception {
        this.programID = GL46.glCreateProgram();
        if (this.programID == 0) {
            throw new Exception("Failed to create shader");
        }
        uniforms = new HashMap<>();
    }

    public void creteUniform(String uniformName) throws Exception {
        int uniformLocation = GL46.glGetUniformLocation(programID, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Failed to find uniform");
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniform, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL46.glUniformMatrix4fv(uniforms.get(uniform), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value) {
        GL46.glUniform1i(uniforms.get(uniformName), value);
    }
    public void setUniform(String uniformName, float value) {
        GL46.glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value){
        GL46.glUniform3fv(uniforms.get(uniformName),new float[]{value.x, value.y, value.z});
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = createShader(shaderCode, GL46.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = createShader(shaderCode, GL46.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderID = GL46.glCreateShader(shaderType);
        if (shaderID == 0) {
            throw new Exception("Failed to create shader");
        }
        GL46.glShaderSource(shaderID, shaderCode);
        GL46.glCompileShader(shaderID);

        if (GL46.glGetShaderi(shaderID, GL46.GL_COMPILE_STATUS) == 0) {
            throw new Exception("Failed to compile shader code. Shader Type:" + shaderType + "\n Info: " + GL46.glGetShaderInfoLog(shaderID, 1024));
        }

        GL46.glAttachShader(programID, shaderID);
        return shaderID;
    }

    public void link() throws Exception {
        GL46.glLinkProgram(programID);
        if (GL46.glGetProgrami(programID, GL46.GL_LINK_STATUS) == 0) {
            throw new Exception("Failed to link shader code. " + "\n Info: " + GL46.glGetProgramInfoLog(programID, 1024));
        }

        if (vertexShaderID != 0) {
            GL46.glDetachShader(programID, vertexShaderID);
        }
        if (fragmentShaderID != 0) {
            GL46.glDetachShader(programID, fragmentShaderID);
        }
        GL46.glValidateProgram(programID);
        if (GL46.glGetProgrami(programID, GL46.GL_VALIDATE_STATUS) == 0) {
            throw new Exception("Failed to validate shader code. " + "\n Info: " + GL46.glGetProgramInfoLog(programID, 1024));
        }
    }

    public void bind() {
        GL46.glUseProgram(programID);
    }

    public void unbind() {
        GL46.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programID != 0) {
            GL46.glDeleteProgram(programID);
        }
    }
}

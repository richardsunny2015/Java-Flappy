package com.rlanchez.flappy.graphics;

import com.rlanchez.flappy.math.Matrix4f;
import com.rlanchez.flappy.math.Vector3f;
import com.rlanchez.flappy.utils.ShaderUtils;
import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

public class Shader {
    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<>();

    public Shader(String vertPath, String fragPath) {
        // Should really check to see if this ID is -1
        ID = ShaderUtils.load(vertPath, fragPath);
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        int result = glGetUniformLocation(ID, name);
        if (result == -1) {
            System.err.println(String.format("Could not find uniform variable: %s!", name));
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    public void setUniform1i(String name, int value) {
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector) {
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix) {
        glUniformMatrix4fv(getUniform(name), false, matrix.tFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
    }

    public void disable() {
        glUseProgram(0);
    }
}

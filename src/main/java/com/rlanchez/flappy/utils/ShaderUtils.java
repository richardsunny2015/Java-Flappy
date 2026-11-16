package com.rlanchez.flappy.utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtils {
    private ShaderUtils() {
    }

    public static int load(String vertPath, String fragPath) {
        String vert = FileUtils.loadAsString(vertPath);
        String frag = FileUtils.loadAsString(fragPath);
        return create(vert, frag);
    }

    public static int create(String vert, String frag) {
        int program = glCreateProgram();
        // create vertex and fragment shaders
        int vertId = glCreateShader(GL_VERTEX_SHADER);
        int fragId = glCreateShader(GL_FRAGMENT_SHADER);
        // set sources of the shaders
        glShaderSource(vertId, vert);
        glShaderSource(fragId, frag);

        // compile and check if shaders compiled correctly
        glCompileShader(vertId);
        if (glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile vertex shader");
            System.err.println(glGetShaderInfoLog(vertId));
            return -1;
        }
        glCompileShader(fragId);
        if (glGetShaderi(vertId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile fragment shader");
            System.err.println(glGetShaderInfoLog(fragId));
            return -1;
        }

        // attach shaders to program
        glAttachShader(program, vertId);
        glAttachShader(program, fragId);

        glLinkProgram(program);
        glValidateProgram(program);

        // delete shaders after attaching them to program
        glDeleteShader(vertId);
        glDeleteShader(fragId);

        return program;
    }
}

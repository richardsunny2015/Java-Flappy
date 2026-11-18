package com.rlanchez.flappy;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.rlanchez.flappy.graphics.Shader;
import com.rlanchez.flappy.input.KeyboardInput;
import com.rlanchez.flappy.level.Level;
import com.rlanchez.flappy.math.Matrix4f;

public class Main implements Runnable {
    private int width = 1280;
    private int height = 720;
    private String title = "Flappy";
    private boolean running = true;
    private long window;
    private Thread thread;
    private Level level;

    public void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            return;
        }
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            return;
        }
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        glfwSetKeyCallback(window, new KeyboardInput());
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);

        GL.createCapabilities();
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        Shader.loadAll();
        Shader.BACKGROUND.enable();

        Matrix4f prMatrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * -9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader.BACKGROUND.setUniformMat4f("pr_matrix", prMatrix);
        Shader.BACKGROUND.disable();
        // Texture loadall
        level = new Level();
    }

    public void run() {
        init();
        while (running) {
            update();
            render();
            if (glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }
    private void update() {
        glfwPollEvents();
        if (KeyboardInput.keys[GLFW_KEY_SPACE]) {
            System.out.println("FLAP!");
        }
    }
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        glfwSwapBuffers(window);
    }
    public static void main(String[] args) {
        new Main().start();
    }
}

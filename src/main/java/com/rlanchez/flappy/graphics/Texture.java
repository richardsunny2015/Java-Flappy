package com.rlanchez.flappy.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.rlanchez.flappy.utils.BufferUtils;

public class Texture {
    private int width, height, texture;

    public Texture(String filePath) {
        texture = load(filePath);
    }

    private int load(String filePath) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(filePath));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            // pixels represent ARGB as 8 4-bit decimals
            // we need to convert them to RGBA(right to left) for opengl
            // the following code extracts the 2 decimals for each rgba value
            // using & and right shift

            // leftmost 2 digits shifted to the right 6 times
            int a = (pixels[i] & 0xff000000) >> (6 * 4);
            // second pair of digits
            int r = (pixels[i] & 0xff0000) >> (4 * 4);
            // third pair
            int g = (pixels[i] & 0xff00) >> (2 * 4);
            // last pair
            int b = pixels[i] & 0xff;

            // use left shift and bit-or to put it back together as ABGR (RGBA backwards)
            data[i] = a << 24 | g << 16 | b << 8 | r;
        }
        int tex = glGenTextures();
        // select texture
        glBindTexture(GL_TEXTURE_2D, tex);
        // upscales pixels sharply by selecting nearest
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));

        //unselect texture
        glBindTexture(GL_TEXTURE_2D, 0);

        return tex;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getID() {
        return texture;
    }
    // need getters and maybe setters for width and height
}

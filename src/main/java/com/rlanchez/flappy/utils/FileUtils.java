package com.rlanchez.flappy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
    public static String loadAsString(String file) {
        // use string builder to avoid reallocating memory at every appended line
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String buffer = "";
            while ((buffer = reader.readLine()) != null) {
                result.append(buffer + "\n");
            }
            reader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result.toString();
    }
}

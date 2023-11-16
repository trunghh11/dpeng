package org.openjfx.dpeng.database.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TopicDict {
    public static Map<String, String> animalDict = new HashMap<String, String>();
    public static Map<String, String> appearanceDict = new HashMap<String, String>();
    public static Map<String, String> artDict = new HashMap<String, String>();
    public static Map<String, String> colorshapeDict = new HashMap<String, String>();
    public static Map<String, String> fooddrinksDict = new HashMap<String, String>();
    public static Map<String, String> healthDict = new HashMap<String, String>();
    public static Map<String, String> hobbyDict = new HashMap<String, String>();
    public static Map<String, String> homeDict = new HashMap<String, String>();
    public static Map<String, String> scienceDict = new HashMap<String, String>();
    
    public static Map<String, String> readDict(String path) {
        Map<String, String> dict = new HashMap<String, String>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("=", 2);
                dict.put(words[0].trim(), words[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dict;
    }

    public static void loadAllDict() {
        animalDict = readDict("src/main/resources/org/openjfx/dpeng/topics/animal.txt");
        appearanceDict = readDict("src/main/resources/org/openjfx/dpeng/topics/appearance.txt");
        artDict = readDict("src/main/resources/org/openjfx/dpeng/topics/art.txt");
        colorshapeDict = readDict("src/main/resources/org/openjfx/dpeng/topics/colorshape.txt");
        fooddrinksDict = readDict("src/main/resources/org/openjfx/dpeng/topics/fooddrinks.txt");
        healthDict = readDict("src/main/resources/org/openjfx/dpeng/topics/health.txt");
        hobbyDict = readDict("src/main/resources/org/openjfx/dpeng/topics/hobby.txt");
        homeDict = readDict("src/main/resources/org/openjfx/dpeng/topics/home.txt");
        scienceDict = readDict("src/main/resources/org/openjfx/dpeng/topics/science.txt");
    }
}

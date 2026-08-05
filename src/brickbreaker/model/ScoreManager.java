package brickbreaker.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {
    
    private static final String FILE_PATH = "data/scores.dat";
    private static final int MAX_SCORES = 10;
    
    @SuppressWarnings("unchecked")
    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return scores;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            scores = (List<ScoreEntry>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error al cargar puntajes: " + e.getMessage());
        }
        
        return scores;
    }
    
    public static void addScore(String name, int score, int level) {
        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(name, score, level));
        Collections.sort(scores);
        
        if (scores.size() > MAX_SCORES) {
            scores = scores.subList(0, MAX_SCORES);
        }
        
        saveScores(scores);
    }
    
    private static void saveScores(List<ScoreEntry> scores) {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            System.err.println("Error al guardar puntajes: " + e.getMessage());
        }
    }
}

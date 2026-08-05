package brickbreaker.model;

import java.io.Serializable;

public class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private int score;
    private int level;
    
    public ScoreEntry(String name, int score, int level) {
        this.name = name;
        this.score = score;
        this.level = level;
    }
    
    public String getName() { return name; }
    public int getScore() { return score; }
    public int getLevel() { return level; }
    
    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(other.score, this.score);
    }
}

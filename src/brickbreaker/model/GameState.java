package brickbreaker.model;

public class GameState {
    
    public enum State {
        PLAYING,
        PAUSED,
        GAME_OVER,
        LEVEL_COMPLETE,
        VICTORY,
        WAITING_TO_START
    }
    
    public static final int MAX_LEVEL = 5;
    
    private State currentState;
    private int score;
    private int lives;
    private int level;
    private int combo;
    private int maxCombo;
    
    public GameState() {
        resetGame();
    }
    
    public void resetGame() {
        score = 0;
        lives = 3;
        level = 1;
        combo = 0;
        maxCombo = 0;
        currentState = State.WAITING_TO_START;
    }
    
    public void addScore(int points) {
        int multiplier = getComboMultiplier();
        score += points * multiplier;
    }
    
    public int getComboMultiplier() {
        if (combo >= 20) return 4;
        if (combo >= 10) return 3;
        if (combo >= 5) return 2;
        return 1;
    }
    
    public void incrementCombo() {
        combo++;
        if (combo > maxCombo) {
            maxCombo = combo;
        }
    }
    
    public void resetCombo() {
        combo = 0;
    }
    
    public void loseLife() {
        lives--;
        resetCombo();
        if (lives <= 0) {
            currentState = State.GAME_OVER;
        } else {
            currentState = State.WAITING_TO_START;
        }
    }
    
    public void nextLevel() {
        level++;
        if (level > MAX_LEVEL) {
            currentState = State.VICTORY;
        } else {
            currentState = State.WAITING_TO_START;
        }
    }
    
    public State getCurrentState() { return currentState; }
    public void setCurrentState(State state) { this.currentState = state; }
    
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    public int getCombo() { return combo; }
    public int getMaxCombo() { return maxCombo; }
}

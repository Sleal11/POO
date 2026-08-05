package brickbreaker.view;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public static final String MENU_SCREEN = "MENU";
    public static final String GAME_SCREEN = "GAME";
    public static final String SCORES_SCREEN = "SCORES";
    public static final String GAME_OVER_SCREEN = "GAME_OVER";
    public static final String VICTORY_SCREEN = "VICTORY";

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private MainMenuPanel menuPanel;
    private GamePanel gamePanel;
    private ScoreBoardPanel scoreBoardPanel;
    private GameOverPanel gameOverPanel;
    private VictoryPanel victoryPanel;

    public MainWindow() {
        setTitle("🧱 Brick Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        menuPanel = new MainMenuPanel(this);
        gamePanel = new GamePanel(this);
        scoreBoardPanel = new ScoreBoardPanel(this);
        gameOverPanel = new GameOverPanel(this);
        victoryPanel = new VictoryPanel(this);

        cardPanel.add(menuPanel, MENU_SCREEN);
        cardPanel.add(gamePanel, GAME_SCREEN);
        cardPanel.add(scoreBoardPanel, SCORES_SCREEN);
        cardPanel.add(gameOverPanel, GAME_OVER_SCREEN);
        cardPanel.add(victoryPanel, VICTORY_SCREEN);

        add(cardPanel);
        pack();
        setLocationRelativeTo(null);
    }

    public void showScreen(String screenName) {
        cardLayout.show(cardPanel, screenName);

        if (screenName.equals(GAME_SCREEN)) {
            gamePanel.requestFocusInWindow();
        }
    }

    public void startNewGame() {
        gamePanel.startNewGame();
        showScreen(GAME_SCREEN);
    }

    public void showGameOver(int finalScore, int level) {
        gameOverPanel.setFinalScore(finalScore, level);
        showScreen(GAME_OVER_SCREEN);
    }

    public void showVictory(int finalScore, int maxCombo) {
        victoryPanel.setFinalScore(finalScore, maxCombo);
        showScreen(VICTORY_SCREEN);
    }

    public void showScoreBoard() {
        scoreBoardPanel.refreshScores();
        showScreen(SCORES_SCREEN);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}

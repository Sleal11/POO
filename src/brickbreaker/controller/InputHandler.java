package brickbreaker.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import brickbreaker.model.GameState;
import brickbreaker.view.GamePanel;

public class InputHandler extends KeyAdapter {
    
    private GameController gameController;
    private GamePanel gamePanel;
    
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    
    public InputHandler(GameController gameController, GamePanel gamePanel) {
        this.gameController = gameController;
        this.gamePanel = gamePanel;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        GameState gameState = gameController.getGameState();
        
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        
        if (key == KeyEvent.VK_ESCAPE) {
            togglePause();
        }
        
        if (key == KeyEvent.VK_P) {
            togglePause();
        }
        
        if (key == KeyEvent.VK_SPACE) {
            if (gameState != null && gameState.getCurrentState() == GameState.State.WAITING_TO_START) {
                gameController.launchBall();
            }
        }
    }
    
    private void togglePause() {
        GameState gameState = gameController.getGameState();
        if (gameState != null) {
            if (gameState.getCurrentState() == GameState.State.PLAYING) {
                gameController.pauseGame();
                if (gamePanel != null) {
                    gamePanel.showPauseOverlay(true);
                }
            } else if (gameState.getCurrentState() == GameState.State.PAUSED) {
                if (gamePanel != null) {
                    gamePanel.showPauseOverlay(false);
                }
                gameController.resumeGame();
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
    }
    
    public void updatePaddle() {
        if (gameController.getPaddle() != null) {
            if (leftPressed) {
                gameController.getPaddle().moveLeft();
            }
            if (rightPressed) {
                gameController.getPaddle().moveRight();
            }
        }
    }
}

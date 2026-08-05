package brickbreaker.controller;

import javax.swing.Timer;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import brickbreaker.model.*;
import brickbreaker.view.GamePanel;
import brickbreaker.view.MainWindow;

public class GameController implements ActionListener {
    
    private GamePanel gamePanel;
    private Timer timer;
    private final int DELAY = 16;
    
    private Ball ball;
    private Paddle paddle;
    private BrickLayout brickLayout;
    private GameState gameState;
    private ParticleSystem particleSystem;
    
    public GameController(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.gameState = new GameState();
        this.particleSystem = new ParticleSystem();
        timer = new Timer(DELAY, this);
    }
    
    public void initGame() {
        gameState.resetGame();
        particleSystem.clear();
        initLevel();
        timer.start();
    }
    
    private void initLevel() {
        paddle = new Paddle(MainWindow.WINDOW_WIDTH / 2 - 50, MainWindow.WINDOW_HEIGHT - 60, 0, MainWindow.WINDOW_WIDTH);
        ball = new Ball(MainWindow.WINDOW_WIDTH / 2, MainWindow.WINDOW_HEIGHT - 80);
        ball.setDx(0);
        ball.setDy(0);
        ball.clearTrail();
        
        brickLayout = new BrickLayout(gameState.getLevel());
        gameState.setCurrentState(GameState.State.WAITING_TO_START); 
        
        if (gamePanel != null) {
            gamePanel.triggerLevelTransition();
        }
    }
    
    private void resetBallAndPaddle() {
        paddle.setPosition(MainWindow.WINDOW_WIDTH / 2 - paddle.getWidth() / 2, MainWindow.WINDOW_HEIGHT - 60);
        ball.setPosition(MainWindow.WINDOW_WIDTH / 2, MainWindow.WINDOW_HEIGHT - 80);
        ball.setDx(0);
        ball.setDy(0);
        ball.clearTrail();
        
        gameState.setCurrentState(GameState.State.WAITING_TO_START);
    }
    
    public void launchBall() {
        if (gameState.getCurrentState() == GameState.State.WAITING_TO_START) {
            int speed = 3 + gameState.getLevel();
            ball.setDx(Math.random() < 0.5 ? -speed : speed);
            ball.setDy(-speed - 1);
            gameState.setCurrentState(GameState.State.PLAYING);
        }
    }
    
    public void pauseGame() {
        timer.stop();
        if (gameState.getCurrentState() == GameState.State.PLAYING) {
            gameState.setCurrentState(GameState.State.PAUSED);
        }
    }
    
    public void resumeGame() {
        if (gameState.getCurrentState() == GameState.State.PAUSED || gameState.getCurrentState() == GameState.State.PLAYING) {
            gameState.setCurrentState(GameState.State.PLAYING);
            timer.start();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        particleSystem.update();

        if (gameState.getCurrentState() == GameState.State.WAITING_TO_START) {
            gamePanel.getInputHandler().updatePaddle();
            ball.setPosition(paddle.getX() + paddle.getWidth() / 2, paddle.getY() - ball.getRadius() - 2);
        } else if (gameState.getCurrentState() == GameState.State.PLAYING) {
            gamePanel.getInputHandler().updatePaddle();
            ball.move();
            
            checkCollisions();
        }
        gamePanel.repaint();
    }
    
    private void checkCollisions() {
        int r = ball.getRadius();
        if (ball.getX() - r < 0) {
            ball.setDx(Math.abs(ball.getDx()));
            ball.setPosition(r, ball.getY());
        }
        if (ball.getX() + r > MainWindow.WINDOW_WIDTH - 15) {
            ball.setDx(-Math.abs(ball.getDx()));
            ball.setPosition(MainWindow.WINDOW_WIDTH - 15 - r, ball.getY());
        }
        if (ball.getY() - r < 0) {
            ball.setDy(Math.abs(ball.getDy()));
            ball.setPosition(ball.getX(), r);
        }
        
        if (ball.getY() + r > MainWindow.WINDOW_HEIGHT - 35) {
            gameState.loseLife();
            if (gameState.getCurrentState() == GameState.State.GAME_OVER) {
                timer.stop();
                gamePanel.getMainWindow().showGameOver(gameState.getScore(), gameState.getLevel());
            } else {
                resetBallAndPaddle();
            }
            return;
        }
        
        Rectangle ballRect = ball.getBounds();
        Rectangle paddleRect = paddle.getBounds();
        
        if (ballRect.intersects(paddleRect)) {
            if (ball.getDy() > 0) {
                ball.setDy(-Math.abs(ball.getDy()));
                
                int paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                int hitPoint = ball.getX() - paddleCenter;
                
                ball.setDx(hitPoint / 7); 
                
                if (ball.getDx() == 0) {
                    ball.setDx(Math.random() < 0.5 ? 2 : -2);
                }
            }
        }
        
        boolean hitBrick = false;
        for (Brick brick : brickLayout.getBricks()) {
            if (brick.isActive() && ballRect.intersects(brick.getBounds())) {
                
                int ballTop = ballRect.y;
                int ballBottom = ballRect.y + ballRect.height;
                int brickTop = brick.getBounds().y;
                int brickBottom = brick.getBounds().y + brick.getBounds().height;

                boolean hitTopOrBottom = (ballBottom - ball.getDy() <= brickTop) || (ballTop - ball.getDy() >= brickBottom);
                
                if (hitTopOrBottom) {
                    ball.reverseY();
                } else {
                    ball.reverseX();
                }
                
                brick.hit();
                
                double particleX = brick.getX() + brick.getWidth() / 2.0;
                double particleY = brick.getY() + brick.getHeight() / 2.0;
                if (!brick.isActive()) {
                    particleSystem.emit(particleX, particleY, brick.getOriginalColor(), 18);
                    gameState.incrementCombo();
                    gameState.addScore(brick.getPoints());
                } else {
                    particleSystem.emitSparks(particleX, particleY, brick.getColor(), 6);
                }
                
                hitBrick = true;
                break;
            }
        }
        
        if (hitBrick && brickLayout.allDestroyed()) {
            gameState.nextLevel();
            if (gameState.getCurrentState() == GameState.State.VICTORY) {
                timer.stop();
                gamePanel.getMainWindow().showVictory(gameState.getScore(), gameState.getMaxCombo());
            } else {
                initLevel();
            }
        }
    }
    
    public Ball getBall() { return ball; }
    public Paddle getPaddle() { return paddle; }
    public BrickLayout getBrickLayout() { return brickLayout; }
    public GameState getGameState() { return gameState; }
    public ParticleSystem getParticleSystem() { return particleSystem; }
}

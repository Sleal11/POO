package brickbreaker.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import brickbreaker.controller.GameController;
import brickbreaker.controller.InputHandler;
import brickbreaker.model.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

    private MainWindow mainWindow;
    private GameController gameController;
    private InputHandler inputHandler;

    private List<float[]> stars;
    private Random random = new Random();
    private int bgTick = 0;

    private static final String[] LEVEL_NAMES = {
        "GRID CLÁSICO", "DIAMANTE", "FORTALEZA", "ZIGZAG", "NEON SUPREMO"
    };

    private int showLevelNameTimer = 0;

    private boolean showPauseMenu = false;
    private Rectangle pauseResumeBtn = new Rectangle(0, 0, 0, 0);
    private Rectangle pauseRestartBtn = new Rectangle(0, 0, 0, 0);
    private Rectangle pauseMenuBtn = new Rectangle(0, 0, 0, 0);
    private int pauseHoverIndex = -1;

    public GamePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT));
        setBackground(new Color(10, 10, 25));
        setFocusable(true);

        gameController = new GameController(this);
        inputHandler = new InputHandler(gameController, this);
        addKeyListener(inputHandler);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (showPauseMenu) {
                    handlePauseClick(e.getX(), e.getY());
                }
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (showPauseMenu) {
                    updatePauseHover(e.getX(), e.getY());
                }
            }
        });

        initStars();
    }

    private void initStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            stars.add(new float[]{
                random.nextFloat() * MainWindow.WINDOW_WIDTH,
                random.nextFloat() * MainWindow.WINDOW_HEIGHT,
                1 + random.nextFloat() * 2f,
                0.1f + random.nextFloat() * 0.4f,
                0.3f + random.nextFloat() * 0.7f
            });
        }
    }

    public void startNewGame() {
        showPauseMenu = false;
        showLevelNameTimer = 120;
        gameController.initGame();
        requestFocusInWindow();
    }

    public void showPauseOverlay(boolean show) {
        this.showPauseMenu = show;
        repaint();
    }

    public boolean isPauseMenuVisible() {
        return showPauseMenu;
    }

    public void triggerLevelTransition() {
        showLevelNameTimer = 120;
    }

    private void handlePauseClick(int mx, int my) {
        if (pauseResumeBtn.contains(mx, my)) {
            showPauseMenu = false;
            gameController.resumeGame();
            requestFocusInWindow();
        } else if (pauseRestartBtn.contains(mx, my)) {
            showPauseMenu = false;
            startNewGame();
        } else if (pauseMenuBtn.contains(mx, my)) {
            showPauseMenu = false;
            mainWindow.showScreen(MainWindow.MENU_SCREEN);
        }
    }

    private void updatePauseHover(int mx, int my) {
        int prev = pauseHoverIndex;
        if (pauseResumeBtn.contains(mx, my)) pauseHoverIndex = 0;
        else if (pauseRestartBtn.contains(mx, my)) pauseHoverIndex = 1;
        else if (pauseMenuBtn.contains(mx, my)) pauseHoverIndex = 2;
        else pauseHoverIndex = -1;
        if (prev != pauseHoverIndex) repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        bgTick++;

        drawBackground(g2d);
        drawStars(g2d);

        if (gameController.getGameState() != null && gameController.getPaddle() != null) {
            drawBricks(g2d);
            drawParticles(g2d);
            drawBallTrail(g2d);
            drawBall(g2d);
            drawPaddle(g2d);
            drawHUD(g2d);
            drawStateMessages(g2d);
            drawLevelName(g2d);
        }

        if (showPauseMenu) {
            drawPauseOverlay(g2d);
        }

        if (showLevelNameTimer > 0) showLevelNameTimer--;

        for (float[] star : stars) {
            star[1] += star[3];
            if (star[1] > MainWindow.WINDOW_HEIGHT) {
                star[1] = -3;
                star[0] = random.nextFloat() * MainWindow.WINDOW_WIDTH;
            }
            star[4] = 0.3f + 0.7f * (float)Math.abs(Math.sin(bgTick * 0.015 + star[0] * 0.1));
        }
    }

    private void drawBackground(Graphics2D g2d) {
        int level = 1;
        if (gameController.getGameState() != null) {
            level = gameController.getGameState().getLevel();
        }

        Color top, bottom;
        switch (level) {
            case 1: top = new Color(8, 8, 25);   bottom = new Color(15, 20, 45); break;
            case 2: top = new Color(15, 5, 25);   bottom = new Color(25, 10, 50); break;
            case 3: top = new Color(20, 8, 8);    bottom = new Color(40, 15, 20); break;
            case 4: top = new Color(5, 15, 20);   bottom = new Color(10, 25, 40); break;
            default: top = new Color(10, 5, 20);  bottom = new Color(20, 10, 40); break;
        }

        GradientPaint gradient = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawStars(Graphics2D g2d) {
        for (float[] star : stars) {
            int alpha = (int)(star[4] * 180);
            g2d.setColor(new Color(200, 220, 255, Math.min(255, alpha)));
            int size = (int) star[2];
            g2d.fillOval((int) star[0], (int) star[1], size, size);
        }
    }

    private void drawHUD(Graphics2D g2d) {
        GameState state = gameController.getGameState();
        Font hudFont = new Font("Segoe UI", Font.BOLD, 17);
        g2d.setFont(hudFont);

        String scoreText = "SCORE: " + state.getScore();
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.drawString(scoreText, 21, 29);
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreText, 20, 28);

        String levelText = "NIVEL " + state.getLevel();
        FontMetrics fm = g2d.getFontMetrics();
        int levelX = (MainWindow.WINDOW_WIDTH - fm.stringWidth(levelText)) / 2;
        g2d.setColor(new Color(255, 220, 60));
        g2d.drawString(levelText, levelX, 28);

        if (state.getCombo() >= 5) {
            int multiplier = state.getComboMultiplier();
            String comboText = "COMBO x" + multiplier;
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
            fm = g2d.getFontMetrics();
            int comboX = (MainWindow.WINDOW_WIDTH - fm.stringWidth(comboText)) / 2;

            float pulse = 0.5f + 0.5f * (float) Math.sin(bgTick * 0.15);
            Color comboColor;
            if (multiplier >= 4) comboColor = new Color(255, 60, 255);
            else if (multiplier >= 3) comboColor = new Color(255, 120, 60);
            else comboColor = new Color(100, 255, 150);

            g2d.setColor(new Color(comboColor.getRed(), comboColor.getGreen(), comboColor.getBlue(), (int)(pulse * 80)));
            g2d.drawString(comboText, comboX - 1, 51);
            g2d.drawString(comboText, comboX + 1, 51);
            g2d.setColor(comboColor);
            g2d.drawString(comboText, comboX, 50);
        }

        g2d.setFont(hudFont);
        String livesLabel = "VIDAS:";
        int livesLabelX = MainWindow.WINDOW_WIDTH - 150;
        g2d.setColor(Color.WHITE);
        g2d.drawString(livesLabel, livesLabelX, 28);

        for (int i = 0; i < state.getLives(); i++) {
            int cx = livesLabelX + 75 + (i * 25);
            int cy = 15;
            g2d.setColor(new Color(255, 60, 80, 60));
            g2d.fillOval(cx - 3, cy - 3, 22, 22);
            g2d.setColor(new Color(255, 60, 80));
            g2d.fillOval(cx, cy, 16, 16);
            g2d.setColor(new Color(255, 200, 200, 120));
            g2d.fillOval(cx + 3, cy + 2, 6, 6);
        }

        BrickLayout layout = gameController.getBrickLayout();
        if (layout != null) {
            int total = layout.getBricks().size();
            long remaining = layout.getBricks().stream().filter(Brick::isActive).count();
            float progress = 1f - ((float) remaining / total);

            int barX = 20;
            int barY = MainWindow.WINDOW_HEIGHT - 20;
            int barW = MainWindow.WINDOW_WIDTH - 55;
            int barH = 5;

            g2d.setColor(new Color(40, 40, 70));
            g2d.fillRoundRect(barX, barY, barW, barH, 4, 4);

            Color barColor = new Color(60, 200, 120);
            g2d.setColor(barColor);
            g2d.fillRoundRect(barX, barY, (int)(barW * progress), barH, 4, 4);

            if (progress > 0) {
                int glowX = barX + (int)(barW * progress) - 3;
                g2d.setColor(new Color(120, 255, 180, 150));
                g2d.fillOval(glowX, barY - 2, 8, barH + 4);
            }
        }
    }

    private void drawStateMessages(Graphics2D g2d) {
        GameState state = gameController.getGameState();

        if (state.getCurrentState() == GameState.State.WAITING_TO_START) {
            float alpha = 0.5f + 0.5f * (float) Math.sin(bgTick * 0.08);
            g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
            drawCenteredText(g2d, "Presiona ESPACIO para lanzar", new Font("Segoe UI", Font.BOLD, 22), 40);
        }
    }

    private void drawCenteredText(Graphics2D g2d, String text, Font font, int yOffset) {
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = (getHeight() / 2) + yOffset;
        g2d.drawString(text, x, y);
    }

    private void drawLevelName(Graphics2D g2d) {
        if (showLevelNameTimer > 0) {
            GameState state = gameController.getGameState();
            int level = state.getLevel();
            String name = (level <= LEVEL_NAMES.length) ? LEVEL_NAMES[level - 1] : "NIVEL " + level;

            float alpha;
            if (showLevelNameTimer > 90) {
                alpha = (120 - showLevelNameTimer) / 30f;
            } else if (showLevelNameTimer < 30) {
                alpha = showLevelNameTimer / 30f;
            } else {
                alpha = 1f;
            }
            alpha = Math.max(0, Math.min(1, alpha));

            Font font = new Font("Segoe UI", Font.BOLD, 40);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();

            String fullText = "NIVEL " + level + " — " + name;
            int tx = (getWidth() - fm.stringWidth(fullText)) / 2;
            int ty = getHeight() / 2 - 50;

            g2d.setColor(new Color(255, 220, 60, (int)(alpha * 60)));
            g2d.drawString(fullText, tx - 2, ty);
            g2d.drawString(fullText, tx + 2, ty);

            g2d.setColor(new Color(255, 220, 60, (int)(alpha * 255)));
            g2d.drawString(fullText, tx, ty);
        }
    }

    private void drawPaddle(Graphics2D g2d) {
        Paddle paddle = gameController.getPaddle();
        int px = paddle.getX();
        int py = paddle.getY();
        int pw = paddle.getWidth();
        int ph = paddle.getHeight();

        g2d.setColor(new Color(100, 150, 255, 40));
        g2d.fillRoundRect(px - 4, py - 2, pw + 8, ph + 8, 14, 14);

        GradientPaint paddleGrad = new GradientPaint(
            px, py, new Color(140, 170, 255),
            px, py + ph, new Color(80, 100, 200)
        );
        g2d.setPaint(paddleGrad);
        g2d.fillRoundRect(px, py, pw, ph, 12, 12);

        g2d.setColor(new Color(180, 200, 255));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(px, py, pw, ph, 12, 12);

        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.fillRoundRect(px + 5, py + 2, pw - 10, 4, 4, 4);

        g2d.setStroke(new BasicStroke(1f));
    }

    private void drawBallTrail(Graphics2D g2d) {
        Ball ball = gameController.getBall();
        List<int[]> trail = ball.getTrail();
        int r = ball.getRadius();

        for (int i = 0; i < trail.size(); i++) {
            int[] pos = trail.get(i);
            float opacity = 1f - (float)(i + 1) / trail.size();
            int alpha = (int)(opacity * 100);
            float scale = 1f - (float) i / trail.size() * 0.6f;
            int trailR = (int)(r * scale);

            g2d.setColor(new Color(150, 180, 255, alpha));
            g2d.fillOval(pos[0] - trailR, pos[1] - trailR, trailR * 2, trailR * 2);
        }
    }

    private void drawBall(Graphics2D g2d) {
        Ball ball = gameController.getBall();
        int r = ball.getRadius();
        int x = ball.getX() - r;
        int y = ball.getY() - r;

        g2d.setColor(new Color(150, 180, 255, 50));
        g2d.fillOval(x - 5, y - 5, r * 2 + 10, r * 2 + 10);
        g2d.setColor(new Color(200, 220, 255, 100));
        g2d.fillOval(x - 2, y - 2, r * 2 + 4, r * 2 + 4);

        GradientPaint ballGrad = new GradientPaint(
            x, y, new Color(240, 245, 255),
            x + r * 2, y + r * 2, new Color(180, 200, 255)
        );
        g2d.setPaint(ballGrad);
        g2d.fillOval(x, y, r * 2, r * 2);

        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(x + 3, y + 2, 5, 5);
    }

    private void drawBricks(Graphics2D g2d) {
        BrickLayout layout = gameController.getBrickLayout();
        if (layout == null) return;

        List<Brick> bricks = layout.getBricks();
        for (Brick brick : bricks) {
            if (brick.isActive()) {
                int bx = brick.getX();
                int by = brick.getY();
                int bw = brick.getWidth();
                int bh = brick.getHeight();
                Color color = brick.getColor();

                GradientPaint brickGrad = new GradientPaint(
                    bx, by, color,
                    bx, by + bh, color.darker()
                );
                g2d.setPaint(brickGrad);
                g2d.fillRoundRect(bx, by, bw, bh, 6, 6);

                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillRect(bx + 2, by + 1, bw - 4, 5);

                g2d.setColor(new Color(
                    Math.min(255, color.getRed() + 30),
                    Math.min(255, color.getGreen() + 30),
                    Math.min(255, color.getBlue() + 30),
                    100
                ));
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(bx, by, bw, bh, 6, 6);

                if (brick.getHp() > 1) {
                    g2d.setColor(new Color(255, 255, 255, 150));
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    FontMetrics fm = g2d.getFontMetrics();
                    String hpText = String.valueOf(brick.getHp());
                    int textX = bx + (bw - fm.stringWidth(hpText)) / 2;
                    int textY = by + (bh + fm.getAscent() - fm.getDescent()) / 2;
                    g2d.drawString(hpText, textX, textY);
                }

                if (brick.getHp() < brick.getMaxHp()) {
                    g2d.setColor(new Color(0, 0, 0, 60));
                    g2d.setStroke(new BasicStroke(1.5f));
                    int cx = bx + bw / 2;
                    int cy = by + bh / 2;
                    g2d.drawLine(cx - 8, cy - 4, cx + 3, cy + 2);
                    g2d.drawLine(cx + 3, cy + 2, cx - 2, cy + 7);
                    g2d.drawLine(cx + 3, cy + 2, cx + 10, cy - 1);
                    g2d.setStroke(new BasicStroke(1f));
                }
            }
        }
    }

    private void drawParticles(Graphics2D g2d) {
        ParticleSystem ps = gameController.getParticleSystem();
        if (ps == null) return;

        Composite original = g2d.getComposite();
        for (Particle p : ps.getParticles()) {
            float opacity = p.getOpacity();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.setColor(p.getColor());
            int size = (int) p.getSize();
            g2d.fillOval((int) p.getX() - size / 2, (int) p.getY() - size / 2, size, size);

            if (size > 2) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * 0.3f));
                g2d.fillOval((int) p.getX() - size, (int) p.getY() - size, size * 2, size * 2);
            }
        }
        g2d.setComposite(original);
    }

    private void drawPauseOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        int panelW = 340;
        int panelH = 320;
        int panelX = (getWidth() - panelW) / 2;
        int panelY = (getHeight() - panelH) / 2;

        g2d.setColor(new Color(20, 20, 50, 200));
        g2d.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2d.setColor(new Color(100, 140, 255, 80));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        g2d.setFont(new Font("Segoe UI", Font.BOLD, 42));
        g2d.setColor(new Color(255, 220, 60));
        FontMetrics fm = g2d.getFontMetrics();
        String pauseTitle = "PAUSA";
        int titleX = (getWidth() - fm.stringWidth(pauseTitle)) / 2;
        g2d.drawString(pauseTitle, titleX, panelY + 60);

        g2d.setColor(new Color(100, 140, 255, 100));
        g2d.fillRect(panelX + 40, panelY + 80, panelW - 80, 2);

        int btnW = 240;
        int btnH = 48;
        int btnX = (getWidth() - btnW) / 2;
        int btnStartY = panelY + 110;
        int btnGap = 60;

        String[] labels = {"▶  CONTINUAR", "🔄  REINICIAR", "🏠  MENÚ PRINCIPAL"};
        Color[] accentColors = {new Color(60, 200, 120), new Color(255, 200, 60), new Color(255, 80, 80)};
        Rectangle[] btnRects = {pauseResumeBtn, pauseRestartBtn, pauseMenuBtn};

        for (int i = 0; i < 3; i++) {
            int by = btnStartY + i * btnGap;
            btnRects[i].setBounds(btnX, by, btnW, btnH);

            boolean hovered = (pauseHoverIndex == i);
            Color bg = hovered ? new Color(60, 60, 120) : new Color(35, 35, 75);

            g2d.setColor(bg);
            g2d.fillRoundRect(btnX, by, btnW, btnH, 14, 14);

            g2d.setColor(accentColors[i]);
            g2d.setStroke(new BasicStroke(hovered ? 2.5f : 1.5f));
            g2d.drawRoundRect(btnX, by, btnW, btnH, 14, 14);

            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.setColor(Color.WHITE);
            FontMetrics fm2 = g2d.getFontMetrics();
            int textX = btnX + (btnW - fm2.stringWidth(labels[i])) / 2;
            int textY = by + (btnH + fm2.getAscent() - fm2.getDescent()) / 2;
            g2d.drawString(labels[i], textX, textY);
        }

        g2d.setStroke(new BasicStroke(1f));

        g2d.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        g2d.setColor(new Color(150, 150, 200));
        String hint = "Presiona P o ESC para continuar";
        FontMetrics fm3 = g2d.getFontMetrics();
        g2d.drawString(hint, (getWidth() - fm3.stringWidth(hint)) / 2, panelY + panelH - 15);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }
}

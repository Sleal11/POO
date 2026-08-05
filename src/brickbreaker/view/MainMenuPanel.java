package brickbreaker.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel {

    private MainWindow mainWindow;

    private static final Color TITLE_COLOR = new Color(255, 220, 60);
    private static final Color SUBTITLE_COLOR = new Color(180, 200, 255);
    private static final Color BUTTON_COLOR = new Color(40, 40, 90);
    private static final Color BUTTON_HOVER_COLOR = new Color(70, 70, 160);
    private static final Color BUTTON_TEXT_COLOR = new Color(255, 255, 255);

    private Timer animTimer;
    private float titleGlow = 0f;
    private boolean titleGlowUp = true;
    private int starTick = 0;

    private List<float[]> stars;
    private Random random = new Random();

    private List<JButton> buttons = new ArrayList<>();
    private float buttonAlpha = 0f;

    public MainMenuPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT));
        setBackground(new Color(10, 10, 30));
        setLayout(new GridBagLayout());

        initStars();
        initComponents();
        startAnimation();
    }

    private void initStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            stars.add(new float[] {
                    random.nextFloat() * MainWindow.WINDOW_WIDTH,
                    random.nextFloat() * MainWindow.WINDOW_HEIGHT,
                    1 + random.nextFloat() * 2.5f,
                    0.2f + random.nextFloat() * 0.8f,
                    0.3f + random.nextFloat() * 0.7f
            });
        }
    }

    private void startAnimation() {
        animTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (titleGlowUp) {
                    titleGlow += 0.03f;
                    if (titleGlow >= 1f) {
                        titleGlow = 1f;
                        titleGlowUp = false;
                    }
                } else {
                    titleGlow -= 0.03f;
                    if (titleGlow <= 0.2f) {
                        titleGlow = 0.2f;
                        titleGlowUp = true;
                    }
                }

                starTick++;
                for (float[] star : stars) {
                    star[1] += star[3];
                    if (star[1] > MainWindow.WINDOW_HEIGHT) {
                        star[1] = -5;
                        star[0] = random.nextFloat() * MainWindow.WINDOW_WIDTH;
                    }
                    star[4] = 0.3f + 0.7f * (float) Math.abs(Math.sin(starTick * 0.02 + star[0]));
                }

                if (buttonAlpha < 1f) {
                    buttonAlpha += 0.05f;
                    if (buttonAlpha > 1f)
                        buttonAlpha = 1f;
                }

                repaint();
            }
        });
        animTimer.start();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel titleSpacer = new JPanel();
        titleSpacer.setOpaque(false);
        titleSpacer.setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, 140));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(titleSpacer, gbc);

        JButton playButton = createStyledButton("⚡  JUGAR", new Color(60, 200, 120));
        playButton.addActionListener(e -> mainWindow.startNewGame());
        gbc.gridy = 1;
        gbc.insets = new Insets(8, 140, 8, 140);
        add(playButton, gbc);
        buttons.add(playButton);

        JButton scoresButton = createStyledButton("🏆  PUNTAJES", new Color(255, 200, 60));
        scoresButton.addActionListener(e -> mainWindow.showScoreBoard());
        gbc.gridy = 2;
        add(scoresButton, gbc);
        buttons.add(scoresButton);

        JButton exitButton = createStyledButton("✖  SALIR", new Color(255, 80, 80));
        exitButton.addActionListener(e -> System.exit(0));
        gbc.gridy = 3;
        add(exitButton, gbc);
        buttons.add(exitButton);
    }

    private JButton createStyledButton(String text, Color accentColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);

                g2.setColor(accentColor);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);

                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() / 2 - 2, 16, 16);

                g2.dispose();

                g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(getFont());
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textX = (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), textX, textY);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(320, 55));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(8, 8, 25),
                0, getHeight(), new Color(20, 15, 50));
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawStars(g2d);
        drawDecorativeBricks(g2d);
        drawAnimatedTitle(g2d);
    }

    private void drawStars(Graphics2D g2d) {
        for (float[] star : stars) {
            float brightness = star[4];
            int alpha = (int) (brightness * 200);
            g2d.setColor(new Color(200, 220, 255, Math.min(255, alpha)));
            int size = (int) star[2];
            g2d.fillOval((int) star[0], (int) star[1], size, size);

            if (size > 2) {
                g2d.setColor(new Color(180, 200, 255, alpha / 4));
                g2d.fillOval((int) star[0] - 2, (int) star[1] - 2, size + 4, size + 4);
            }
        }
    }

    private void drawAnimatedTitle(Graphics2D g2d) {
        String title = "BRICK BREAKER";
        Font titleFont = new Font("Segoe UI", Font.BOLD, 56);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (getWidth() - fm.stringWidth(title)) / 2;
        int titleY = 90;

        int glowAlpha = (int) (titleGlow * 60);
        g2d.setColor(new Color(255, 180, 0, glowAlpha));
        for (int i = 3; i >= 1; i--) {
            g2d.setFont(titleFont);
            g2d.drawString(title, titleX - i, titleY);
            g2d.drawString(title, titleX + i, titleY);
            g2d.drawString(title, titleX, titleY - i);
            g2d.drawString(title, titleX, titleY + i);
        }

        g2d.setColor(TITLE_COLOR);
        g2d.drawString(title, titleX, titleY);

        int shimmerX = titleX + (int) ((fm.stringWidth(title) + 60) * ((starTick % 120) / 120.0)) - 30;
        g2d.setColor(new Color(255, 255, 255, (int) (titleGlow * 120)));
        g2d.fillRect(shimmerX, titleY - fm.getAscent(), 3, fm.getHeight());

        String subtitle = "¡Destruye todos los ladrillos!";
        g2d.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        g2d.setColor(SUBTITLE_COLOR);
        FontMetrics fm2 = g2d.getFontMetrics();
        int subX = (getWidth() - fm2.stringWidth(subtitle)) / 2;
        g2d.drawString(subtitle, subX, titleY + 35);
    }

    private void drawDecorativeBricks(Graphics2D g2d) {
        Color[] brickColors = {
                new Color(255, 60, 90, 25),
                new Color(255, 140, 50, 25),
                new Color(255, 230, 60, 25),
                new Color(60, 220, 120, 25),
                new Color(60, 150, 255, 25)
        };

        int brickWidth = 70;
        int brickHeight = 25;
        int padding = 4;

        for (int row = 0; row < 3; row++) {
            float offset = (float) Math.sin(starTick * 0.015 + row * 0.5) * 8;
            for (int col = 0; col < 12; col++) {
                int x = col * (brickWidth + padding) + 10 + (int) offset;
                int y = row * (brickHeight + padding) + 15;
                g2d.setColor(brickColors[row % brickColors.length]);
                g2d.fillRoundRect(x, y, brickWidth, brickHeight, 8, 8);
            }
        }

        for (int row = 0; row < 2; row++) {
            float offset = (float) Math.sin(starTick * 0.015 + row * 0.5 + Math.PI) * 8;
            for (int col = 0; col < 12; col++) {
                int x = col * (brickWidth + padding) + 10 + (int) offset;
                int y = getHeight() - (row + 1) * (brickHeight + padding) - 10;
                g2d.setColor(brickColors[(row + 3) % brickColors.length]);
                g2d.fillRoundRect(x, y, brickWidth, brickHeight, 8, 8);
            }
        }
    }
}

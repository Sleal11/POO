package brickbreaker.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import brickbreaker.model.ScoreManager;

@SuppressWarnings("serial")
public class VictoryPanel extends JPanel {

    private MainWindow mainWindow;
    private int finalScore = 0;
    private int maxCombo = 0;

    private JLabel scoreValueLabel;
    private JTextField nameField;
    private JButton saveButton;

    private Timer animTimer;
    private int tick = 0;
    private List<float[]> fireworks;
    private Random random = new Random();

    private static final Color TITLE_COLOR = new Color(255, 220, 60);
    private static final Color TEXT_COLOR = new Color(200, 220, 255);

    public VictoryPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT));
        setBackground(new Color(10, 10, 30));
        setLayout(new GridBagLayout());
        fireworks = new ArrayList<>();

        initComponents();
        startAnimation();
    }

    private void startAnimation() {
        animTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick++;
                if (tick % 15 == 0) {
                    emitFirework();
                }
                for (int i = fireworks.size() - 1; i >= 0; i--) {
                    float[] p = fireworks.get(i);
                    p[0] += p[2];
                    p[1] += p[3];
                    p[3] += 0.1f;
                    p[7] -= 1;
                    p[8] *= 0.97f;
                    if (p[7] <= 0 || p[8] < 0.5f) {
                        fireworks.remove(i);
                    }
                }
                repaint();
            }
        });
        animTimer.start();
    }

    private void emitFirework() {
        float cx = 100 + random.nextFloat() * (MainWindow.WINDOW_WIDTH - 200);
        float cy = 50 + random.nextFloat() * 200;

        float[][] colors = {
            {255, 220, 60},
            {255, 180, 0},
            {255, 100, 50},
            {100, 255, 200},
            {200, 150, 255}
        };
        float[] color = colors[random.nextInt(colors.length)];

        for (int i = 0; i < 20; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            float speed = 1 + random.nextFloat() * 4;
            fireworks.add(new float[]{
                cx, cy,
                (float)(Math.cos(angle) * speed),
                (float)(Math.sin(angle) * speed) - 1,
                color[0], color[1], color[2],
                25 + random.nextInt(20),
                3 + random.nextFloat() * 4
            });
        }
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel titleSpacer = new JPanel();
        titleSpacer.setOpaque(false);
        titleSpacer.setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, 100));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(titleSpacer, gbc);

        scoreValueLabel = new JLabel("0", SwingConstants.CENTER);
        scoreValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        scoreValueLabel.setForeground(TITLE_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 50, 10, 50);
        add(scoreValueLabel, gbc);

        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        savePanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Tu nombre:");
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        nameField = new JTextField(10);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        nameField.setBackground(new Color(30, 30, 60));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(Color.WHITE);
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 140, 255), 2),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        saveButton = new JButton("Guardar");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setBackground(new Color(60, 200, 120));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveScore());

        savePanel.add(nameLabel);
        savePanel.add(nameField);
        savePanel.add(saveButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 50, 20, 50);
        add(savePanel, gbc);

        JButton menuButton = createStyledButton("🏠  MENÚ PRINCIPAL");
        menuButton.addActionListener(e -> mainWindow.showScreen(MainWindow.MENU_SCREEN));
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 140, 8, 140);
        add(menuButton, gbc);

        JButton retryButton = createStyledButton("🔄  JUGAR DE NUEVO");
        retryButton.addActionListener(e -> mainWindow.startNewGame());
        gbc.gridy = 4;
        add(retryButton, gbc);
    }

    private void saveScore() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = "Campeón";
        }

        ScoreManager.addScore(name, finalScore, 5);

        nameField.setText("");
        nameField.setEnabled(false);
        saveButton.setEnabled(false);
        saveButton.setText("¡Guardado!");

        Timer timer = new Timer(1000, e -> mainWindow.showScoreBoard());
        timer.setRepeats(false);
        timer.start();
    }

    public void setFinalScore(int score, int maxCombo) {
        this.finalScore = score;
        this.maxCombo = maxCombo;
        scoreValueLabel.setText(String.valueOf(score));

        nameField.setEnabled(true);
        saveButton.setEnabled(true);
        saveButton.setText("Guardar");
        nameField.setText("");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(50, 50, 100));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 50));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(80, 80, 160));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(50, 50, 100));
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
            0, 0, new Color(10, 20, 40),
            0, getHeight(), new Color(5, 5, 20)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (float[] p : fireworks) {
            float opacity = Math.max(0, p[7] / 40f);
            int alpha = (int)(opacity * 255);
            g2d.setColor(new Color((int)p[4], (int)p[5], (int)p[6], Math.min(255, alpha)));
            int size = (int) p[8];
            g2d.fillOval((int) p[0] - size / 2, (int) p[1] - size / 2, size, size);
            g2d.setColor(new Color((int)p[4], (int)p[5], (int)p[6], Math.min(255, alpha / 3)));
            g2d.fillOval((int) p[0] - size, (int) p[1] - size, size * 2, size * 2);
        }

        String title = "¡VICTORIA!";
        float glowPulse = 0.5f + 0.5f * (float)Math.sin(tick * 0.05);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 52);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(title)) / 2;
        int ty = 70;

        g2d.setColor(new Color(255, 220, 60, (int)(glowPulse * 80)));
        g2d.drawString(title, tx - 2, ty);
        g2d.drawString(title, tx + 2, ty);
        g2d.drawString(title, tx, ty - 2);
        g2d.drawString(title, tx, ty + 2);

        g2d.setColor(TITLE_COLOR);
        g2d.drawString(title, tx, ty);

        String sub = "¡Has completado los 5 niveles!";
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        g2d.setColor(TEXT_COLOR);
        FontMetrics fm2 = g2d.getFontMetrics();
        g2d.drawString(sub, (getWidth() - fm2.stringWidth(sub)) / 2, ty + 30);

        if (maxCombo > 0) {
            String comboText = "Máximo Combo: " + maxCombo + "x";
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2d.setColor(new Color(255, 180, 60));
            FontMetrics fm3 = g2d.getFontMetrics();
            g2d.drawString(comboText, (getWidth() - fm3.stringWidth(comboText)) / 2, ty + 55);
        }
    }
}

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
public class GameOverPanel extends JPanel {

    private MainWindow mainWindow;
    private int finalScore = 0;
    private int finalLevel = 1;
    
    private JLabel scoreValueLabel;
    private JTextField nameField;
    private JButton saveButton;

    private Timer animTimer;
    private int tick = 0;
    private List<float[]> fallingParticles;
    private Random random = new Random();

    private static final Color TITLE_COLOR = new Color(255, 60, 80);
    private static final Color SCORE_COLOR = new Color(255, 220, 60);
    private static final Color TEXT_COLOR = new Color(200, 210, 240);

    public GameOverPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT));
        setBackground(new Color(15, 10, 20));
        setLayout(new GridBagLayout());
        fallingParticles = new ArrayList<>();

        initComponents();
        startAnimation();
    }

    private void startAnimation() {
        animTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick++;
                if (tick % 3 == 0) {
                    fallingParticles.add(new float[]{
                        random.nextFloat() * MainWindow.WINDOW_WIDTH,
                        -10,
                        1 + random.nextFloat() * 3,
                        180 + random.nextInt(75), random.nextInt(40), random.nextInt(40),
                        150 + random.nextInt(100),
                        2 + random.nextFloat() * 4
                    });
                }
                for (int i = fallingParticles.size() - 1; i >= 0; i--) {
                    float[] p = fallingParticles.get(i);
                    p[1] += p[2];
                    p[6] -= 1;
                    if (p[1] > MainWindow.WINDOW_HEIGHT || p[6] <= 0) {
                        fallingParticles.remove(i);
                    }
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
        titleSpacer.setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, 110));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(titleSpacer, gbc);

        scoreValueLabel = new JLabel("0", SwingConstants.CENTER);
        scoreValueLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        scoreValueLabel.setForeground(SCORE_COLOR);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 50, 15, 50);
        add(scoreValueLabel, gbc);
        
        JPanel savePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
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
            BorderFactory.createLineBorder(new Color(255, 80, 100), 2),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        
        saveButton = new JButton("Guardar");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setBackground(new Color(255, 80, 100));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveScore());
        
        savePanel.add(nameLabel);
        savePanel.add(nameField);
        savePanel.add(saveButton);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 50, 25, 50);
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
            name = "Anónimo";
        }
        
        ScoreManager.addScore(name, finalScore, finalLevel);
        
        nameField.setText("");
        nameField.setEnabled(false);
        saveButton.setEnabled(false);
        saveButton.setText("¡Guardado!");
        
        Timer timer = new Timer(1000, e -> mainWindow.showScoreBoard());
        timer.setRepeats(false);
        timer.start();
    }

    public void setFinalScore(int score, int level) {
        this.finalScore = score;
        this.finalLevel = level;
        scoreValueLabel.setText(String.valueOf(score));
        
        nameField.setEnabled(true);
        saveButton.setEnabled(true);
        saveButton.setText("Guardar");
        nameField.setText("");
        nameField.requestFocus();
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
            0, 0, new Color(35, 8, 12),
            0, getHeight(), new Color(12, 8, 25)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (float[] p : fallingParticles) {
            int alpha = Math.min(255, (int) p[6]);
            g2d.setColor(new Color((int)p[3], (int)p[4], (int)p[5], alpha));
            int size = (int) p[7];
            g2d.fillOval((int) p[0], (int) p[1], size, size);
        }

        drawGlitchTitle(g2d);

        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2d.setColor(TEXT_COLOR);
        String levelText = "Alcanzaste el Nivel " + finalLevel;
        FontMetrics fm2 = g2d.getFontMetrics();
        g2d.drawString(levelText, (getWidth() - fm2.stringWidth(levelText)) / 2, 100);
    }

    private void drawGlitchTitle(Graphics2D g2d) {
        String title = "GAME OVER";
        Font titleFont = new Font("Segoe UI", Font.BOLD, 52);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int tx = (getWidth() - fm.stringWidth(title)) / 2;
        int ty = 75;

        float glitchOffset = (float) Math.sin(tick * 0.2) * 3;
        boolean glitchFlash = (tick % 60 < 3);

        if (!glitchFlash) {
            g2d.setColor(new Color(255, 0, 0, 80));
            g2d.drawString(title, tx + (int) glitchOffset, ty);

            g2d.setColor(new Color(0, 200, 255, 60));
            g2d.drawString(title, tx - (int) glitchOffset, ty);
        }

        g2d.setColor(TITLE_COLOR);
        g2d.drawString(title, tx, ty);
    }
}

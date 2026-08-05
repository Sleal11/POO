package brickbreaker.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import brickbreaker.model.ScoreEntry;
import brickbreaker.model.ScoreManager;

@SuppressWarnings("serial")
public class ScoreBoardPanel extends JPanel {

    private MainWindow mainWindow;
    private JTable scoreTable;
    private DefaultTableModel tableModel;

    private static final Color TITLE_COLOR = new Color(255, 220, 60);

    public ScoreBoardPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setPreferredSize(new Dimension(MainWindow.WINDOW_WIDTH, MainWindow.WINDOW_HEIGHT));
        setBackground(new Color(10, 10, 30));
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("🏆 PUNTAJES ALTOS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        titleLabel.setOpaque(false);
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Posición", "Nombre", "Nivel", "Puntaje"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        scoreTable = new JTable(tableModel);
        scoreTable.setFillsViewportHeight(true);
        scoreTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scoreTable.setRowHeight(35);
        scoreTable.setBackground(new Color(20, 20, 45));
        scoreTable.setForeground(new Color(220, 230, 255));
        scoreTable.setGridColor(new Color(40, 40, 80));
        scoreTable.setSelectionBackground(new Color(60, 60, 120));
        scoreTable.setSelectionForeground(Color.WHITE);
        scoreTable.setShowGrid(true);
        scoreTable.setIntercellSpacing(new Dimension(0, 1));
        
        JTableHeader header = scoreTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        header.setBackground(new Color(40, 40, 90));
        header.setForeground(new Color(255, 220, 60));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 140, 255)));
        
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (!isSelected) {
                    if (row == 0) {
                        c.setBackground(new Color(50, 40, 20));
                        c.setForeground(new Color(255, 220, 100));
                    } else if (row == 1) {
                        c.setBackground(new Color(35, 35, 50));
                        c.setForeground(new Color(200, 210, 230));
                    } else if (row == 2) {
                        c.setBackground(new Color(45, 30, 20));
                        c.setForeground(new Color(220, 170, 120));
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(22, 22, 48) : new Color(18, 18, 42));
                        c.setForeground(new Color(200, 210, 240));
                    }
                }
                
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
        
        for (int i = 0; i < scoreTable.getColumnCount(); i++) {
            scoreTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.getViewport().setBackground(new Color(15, 15, 35));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));
        scrollPane.getVerticalScrollBar().setBackground(new Color(20, 20, 45));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton backButton = new JButton("⬅  VOLVER AL MENÚ");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 50, 100));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setPreferredSize(new Dimension(280, 48));
        backButton.addActionListener(e -> mainWindow.showScreen(MainWindow.MENU_SCREEN));

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(80, 80, 160));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                backButton.setBackground(new Color(50, 50, 100));
            }
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshScores() {
        tableModel.setRowCount(0);
        List<ScoreEntry> scores = ScoreManager.loadScores();
        
        int position = 1;
        String[] medals = {"🥇", "🥈", "🥉"};
        for (ScoreEntry entry : scores) {
            String posText = position <= 3 ? medals[position - 1] + " " + position + "º" : position + "º";
            Object[] row = {
                posText,
                entry.getName(),
                entry.getLevel(),
                entry.getScore()
            };
            tableModel.addRow(row);
            position++;
        }
        
        if (scores.isEmpty()) {
            Object[] row = {"-", "Sin registros", "-", "-"};
            tableModel.addRow(row);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(10, 10, 30),
            0, getHeight(), new Color(20, 15, 45)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}

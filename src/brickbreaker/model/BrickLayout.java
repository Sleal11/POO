package brickbreaker.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import brickbreaker.view.MainWindow;

public class BrickLayout {
    
    private List<Brick> bricks;
    
    private static final Color[][] LEVEL_COLORS = {
        {
            new Color(255, 60, 90),
            new Color(255, 140, 50),
            new Color(255, 230, 60),
            new Color(60, 220, 120),
            new Color(60, 150, 255)
        },
        {
            new Color(255, 80, 180),
            new Color(200, 80, 255),
            new Color(120, 100, 255),
            new Color(80, 200, 255),
            new Color(100, 255, 200)
        },
        {
            new Color(255, 50, 50),
            new Color(255, 100, 20),
            new Color(255, 180, 0),
            new Color(255, 220, 50),
            new Color(255, 120, 80)
        },
        {
            new Color(0, 255, 180),
            new Color(0, 200, 255),
            new Color(80, 120, 255),
            new Color(160, 80, 255),
            new Color(255, 80, 200)
        },
        {
            new Color(255, 50, 80),
            new Color(255, 160, 0),
            new Color(200, 255, 0),
            new Color(0, 220, 255),
            new Color(180, 50, 255)
        }
    };
    
    private static final int[] ROW_POINTS = {50, 40, 30, 20, 10};
    
    public BrickLayout(int level) {
        bricks = new ArrayList<>();
        generateLayout(level);
    }
    
    private void generateLayout(int level) {
        switch (level) {
            case 1:
                generateClassicGrid(level);
                break;
            case 2:
                generateDiamondPattern(level);
                break;
            case 3:
                generateFortressPattern(level);
                break;
            case 4:
                generateZigzagPattern(level);
                break;
            default:
                generateDensePattern(level);
                break;
        }
    }
    
    private void generateClassicGrid(int level) {
        int rows = 5;
        int cols = 10;
        int brickWidth = 60;
        int brickHeight = 25;
        int padding = 10;
        
        int totalWidth = cols * brickWidth + (cols - 1) * padding;
        int offsetX = (MainWindow.WINDOW_WIDTH - totalWidth) / 2;
        int offsetY = 60;
        
        Color[] colors = getColorsForLevel(level);
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = offsetX + col * (brickWidth + padding);
                int y = offsetY + row * (brickHeight + padding);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, colors[row], ROW_POINTS[row], 1));
            }
        }
    }
    
    private void generateDiamondPattern(int level) {
        int brickWidth = 60;
        int brickHeight = 25;
        int padding = 10;
        int cols = 10;
        
        int totalWidth = cols * brickWidth + (cols - 1) * padding;
        int offsetX = (MainWindow.WINDOW_WIDTH - totalWidth) / 2;
        int offsetY = 50;
        
        Color[] colors = getColorsForLevel(level);
        
        int[] rowWidths = {2, 4, 6, 8, 10, 8, 6, 4, 2};
        
        for (int row = 0; row < rowWidths.length; row++) {
            int rowCols = rowWidths[row];
            int rowOffset = (cols - rowCols) / 2;
            
            int colorIdx = Math.min(row, colors.length - 1);
            if (row >= 5) colorIdx = rowWidths.length - 1 - row;
            colorIdx = Math.min(colorIdx, colors.length - 1);
            
            int points = ROW_POINTS[colorIdx];
            int hp = (row == 4) ? 2 : 1;
            
            for (int col = 0; col < rowCols; col++) {
                int x = offsetX + (rowOffset + col) * (brickWidth + padding);
                int y = offsetY + row * (brickHeight + padding);
                bricks.add(new Brick(x, y, brickWidth, brickHeight, colors[colorIdx], points, hp));
            }
        }
    }
    
    private void generateFortressPattern(int level) {
        int brickWidth = 60;
        int brickHeight = 25;
        int padding = 10;
        int rows = 7;
        int cols = 10;
        
        int totalWidth = cols * brickWidth + (cols - 1) * padding;
        int offsetX = (MainWindow.WINDOW_WIDTH - totalWidth) / 2;
        int offsetY = 50;
        
        Color[] colors = getColorsForLevel(level);
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = offsetX + col * (brickWidth + padding);
                int y = offsetY + row * (brickHeight + padding);
                
                boolean isBorder = (row == 0 || row == rows - 1 || col == 0 || col == cols - 1);
                int hp = isBorder ? 2 : 1;
                
                int colorIdx = Math.min(row, colors.length - 1);
                int points = ROW_POINTS[colorIdx];
                
                boolean isInnerHole = (row >= 2 && row <= 4 && col >= 3 && col <= 6);
                
                if (!isInnerHole) {
                    bricks.add(new Brick(x, y, brickWidth, brickHeight, colors[colorIdx], points, hp));
                }
            }
        }
    }
    
    private void generateZigzagPattern(int level) {
        int brickWidth = 60;
        int brickHeight = 25;
        int padding = 10;
        int rows = 7;
        int cols = 10;
        
        int totalWidth = cols * brickWidth + (cols - 1) * padding;
        int offsetX = (MainWindow.WINDOW_WIDTH - totalWidth) / 2;
        int offsetY = 50;
        
        Color[] colors = getColorsForLevel(level);
        
        for (int row = 0; row < rows; row++) {
            int shift = (row % 2 == 0) ? 0 : 1;
            int rowCols = cols - shift;
            
            for (int col = 0; col < rowCols; col++) {
                int x = offsetX + col * (brickWidth + padding) + (shift * (brickWidth + padding) / 2);
                int y = offsetY + row * (brickHeight + padding);
                
                int colorIdx = Math.min(row, colors.length - 1);
                int hp = (row <= 1) ? 2 : 1;
                int points = ROW_POINTS[colorIdx];
                
                bricks.add(new Brick(x, y, brickWidth, brickHeight, colors[colorIdx], points, hp));
            }
        }
    }
    
    private void generateDensePattern(int level) {
        int brickWidth = 60;
        int brickHeight = 25;
        int padding = 8;
        int rows = 8;
        int cols = 10;
        
        int totalWidth = cols * brickWidth + (cols - 1) * padding;
        int offsetX = (MainWindow.WINDOW_WIDTH - totalWidth) / 2;
        int offsetY = 45;
        
        Color[] colors = getColorsForLevel(level);
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = offsetX + col * (brickWidth + padding);
                int y = offsetY + row * (brickHeight + padding);
                
                int colorIdx = Math.min(row % colors.length, colors.length - 1);
                int points = ROW_POINTS[colorIdx];
                
                int hp;
                if (row <= 1) hp = 3;
                else if (row <= 3) hp = 2;
                else hp = 1;
                
                bricks.add(new Brick(x, y, brickWidth, brickHeight, colors[colorIdx], points, hp));
            }
        }
    }
    
    private Color[] getColorsForLevel(int level) {
        int idx = Math.min(level - 1, LEVEL_COLORS.length - 1);
        return LEVEL_COLORS[idx];
    }
    
    public List<Brick> getBricks() {
        return bricks;
    }
    
    public boolean allDestroyed() {
        for (Brick b : bricks) {
            if (b.isActive()) return false;
        }
        return true;
    }
}

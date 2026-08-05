package brickbreaker.model;

import java.awt.Color;
import java.awt.Rectangle;

public class Brick {
    private int x, y;
    private int width, height;
    private Color color;
    private Color originalColor;
    private int points;
    private int hp;
    private int maxHp;
    private boolean active = true;

    public Brick(int x, int y, int width, int height, Color color, int points, int hp) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.originalColor = color;
        this.points = points;
        this.hp = hp;
        this.maxHp = hp;
    }

    public void hit() {
        hp--;
        if (hp <= 0) {
            active = false;
        } else {
            float damageRatio = (float) hp / maxHp;
            int r = (int)(originalColor.getRed() * damageRatio * 0.7);
            int g = (int)(originalColor.getGreen() * damageRatio * 0.7);
            int b = (int)(originalColor.getBlue() * damageRatio * 0.7);
            color = new Color(
                Math.max(30, Math.min(255, r)),
                Math.max(30, Math.min(255, g)),
                Math.max(30, Math.min(255, b))
            );
        }
    }

    public boolean isActive() { return active; }
    public int getPoints() { return points; }
    public Color getColor() { return color; }
    public Color getOriginalColor() { return originalColor; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

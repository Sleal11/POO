package brickbreaker.model;

import java.awt.Rectangle;

public class Paddle {
    private int x, y;
    private int width = 100;
    private int height = 15;
    private int speed = 6;
    
    private int minX;
    private int maxX;

    public Paddle(int startX, int startY, int minX, int maxX) {
        this.x = startX;
        this.y = startY;
        this.minX = minX;
        this.maxX = maxX;
    }

    public void moveLeft() {
        x -= speed;
        if (x < minX) x = minX;
    }

    public void moveRight() {
        x += speed;
        if (x > maxX - width) x = maxX - width;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        if (this.x < minX) this.x = minX;
        if (this.x > maxX - width) this.x = maxX - width;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

package brickbreaker.model;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Ball {
    private int x, y;
    private int radius = 8;
    private int dx = -3;
    private int dy = -4;

    private List<int[]> trail;
    private static final int MAX_TRAIL = 12;

    public Ball(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.trail = new ArrayList<>();
    }

    public void move() {
        trail.add(0, new int[]{x, y});
        if (trail.size() > MAX_TRAIL) {
            trail.remove(trail.size() - 1);
        }
        x += dx;
        y += dy;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void reverseX() {
        dx = -dx;
    }

    public void reverseY() {
        dy = -dy;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return radius; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }

    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
    
    public void increaseSpeed(double factor) {
        int newDx = (int)Math.round(Math.abs(dx) * factor);
        int newDy = (int)Math.round(Math.abs(dy) * factor);
        if (newDx > 8) newDx = 8;
        if (newDy > 8) newDy = 8;
        this.dx = (dx < 0) ? -newDx : newDx;
        this.dy = (dy < 0) ? -newDy : newDy;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    public List<int[]> getTrail() {
        return trail;
    }

    public void clearTrail() {
        trail.clear();
    }
}

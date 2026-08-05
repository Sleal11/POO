package brickbreaker.model;

import java.awt.Color;

public class Particle {
    private double x, y;
    private double vx, vy;
    private Color color;
    private int life;
    private int maxLife;
    private double size;
    private double gravity;

    public Particle(double x, double y, double vx, double vy, Color color, int life, double size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.color = color;
        this.life = life;
        this.maxLife = life;
        this.size = size;
        this.gravity = 0.15;
    }

    public void update() {
        x += vx;
        y += vy;
        vy += gravity;
        vx *= 0.98;
        life--;
        size *= 0.97;
    }

    public boolean isAlive() {
        return life > 0 && size > 0.5;
    }

    public float getOpacity() {
        return Math.max(0f, (float) life / maxLife);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public Color getColor() { return color; }
    public double getSize() { return size; }
    public int getLife() { return life; }
}

package brickbreaker.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleSystem {

    private List<Particle> particles;
    private Random random;

    public ParticleSystem() {
        particles = new ArrayList<>();
        random = new Random();
    }

    public void emit(double x, double y, Color color, int count) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 2 + random.nextDouble() * 5;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed - 2;
            int life = 20 + random.nextInt(30);
            double size = 3 + random.nextDouble() * 5;

            Color particleColor = varyColor(color);
            particles.add(new Particle(x, y, vx, vy, particleColor, life, size));
        }
    }

    public void emitSparks(double x, double y, Color color, int count) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 1 + random.nextDouble() * 3;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            int life = 10 + random.nextInt(15);
            double size = 2 + random.nextDouble() * 3;

            particles.add(new Particle(x, y, vx, vy, color, life, size));
        }
    }

    public void update() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (!p.isAlive()) {
                it.remove();
            }
        }
    }

    private Color varyColor(Color base) {
        int r = clamp(base.getRed() + random.nextInt(60) - 30, 0, 255);
        int g = clamp(base.getGreen() + random.nextInt(60) - 30, 0, 255);
        int b = clamp(base.getBlue() + random.nextInt(60) - 30, 0, 255);
        return new Color(r, g, b);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void clear() {
        particles.clear();
    }
}

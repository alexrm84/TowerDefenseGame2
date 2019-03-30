package com.geekbrains.td;

import com.badlogic.gdx.math.Vector2;

public class Bullet implements Poolable {

    private Vector2 position;
    private Vector2 velocity;
    private int damage;
    private boolean active;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void setup(float x, float y, float vx, float vy) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.damage = 30;
        this.active = true;
    }
}

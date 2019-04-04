package com.geekbrains.td;

public enum BulletType {
    RED(10, 240.0f, false),
    DOUBLE_RED(25, 320.0f, false),
    BLUE(20, 80.0f, true);

    int power;
    float speed;
    boolean autoaim;

    BulletType(int power, float speed, boolean autoaim) {
        this.power = power;
        this.speed = speed;
        this.autoaim = autoaim;
    }
}

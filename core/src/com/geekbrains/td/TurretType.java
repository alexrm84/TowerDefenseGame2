package com.geekbrains.td;

public enum TurretType {
    DOUBLE_RED(0, 1, 200,100, 350.0f, 0.05f, 270.0f, null, BulletType.DOUBLE_RED),
    RED(0, 0, 50, 20, 300.0f, 0.4f, 180.0f, DOUBLE_RED, BulletType.RED),
    BLUE(1, 0, 100, 40, 800.0f, 0.8f, 180.0f, null, BulletType.BLUE);

    int image_x;
    int image_y;
    int price;
    int destroyPrice;
    float fireRadius;
    float chargeTime;
    float rotationSpeed;
    TurretType child;
    BulletType bulletType;

    TurretType(int image_x, int image_y, int price, int destroyPrice, float fireRadius, float chargeTime, float rotationSpeed, TurretType child, BulletType bulletType) {
        this.image_x = image_x;
        this.image_y = image_y;
        this.price = price;
        this.destroyPrice = destroyPrice;
        this.fireRadius = fireRadius;
        this.chargeTime = chargeTime;
        this.rotationSpeed = rotationSpeed;
        this.child = child;
        this.bulletType = bulletType;
    }
}

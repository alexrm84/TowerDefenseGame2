package com.geekbrains.td;

public class PointOfTheWay {
    private int x;
    private int y;
    private PointOfTheWay previous;
    private int value;

    public PointOfTheWay(int x, int y, PointOfTheWay previous, int value) {
        this.x = x;
        this.y = y;
        this.previous = previous;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PointOfTheWay getPrevious() {
        return previous;
    }

    public int getValue() {
        return value;
    }
}

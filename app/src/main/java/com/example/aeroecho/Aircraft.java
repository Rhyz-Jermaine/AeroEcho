package com.example.aeroecho;

public class Aircraft {
    private float x;
    private float y;

    public Aircraft(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void moveTo(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
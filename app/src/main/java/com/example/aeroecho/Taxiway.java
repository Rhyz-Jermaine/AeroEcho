package com.example.aeroecho;

public class Taxiway {
    private String name;
    private float x;
    private float y;

    public Taxiway(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

package me.codeflusher.gcalc.util;

public enum Direction {
    X(1, 0, 0),
    Y(0, 1, 0),
    Z(0, 0, 1);
    final int xMult;
    final int yMult;
    final int zMult;

    Direction(int xMult, int yMult, int zMult) {
        this.xMult = xMult;
        this.yMult = yMult;
        this.zMult = zMult;
    }

    public int getxMult() {
        return xMult;
    }

    public int getyMult() {
        return yMult;
    }

    public int getzMult() {
        return zMult;
    }
}

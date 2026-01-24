package net.snapshots;

import java.io.Serializable;

public class PlayerSnapshot implements Serializable {
    public String username;
    public int x, y;
    public int direction;
    public int animation;

    public PlayerSnapshot(String u, int x, int y, int d, int a) {
        this.username = u;
        this.x = x;
        this.y = y;
        this.direction = d;
        this.animation = a;
    }
}
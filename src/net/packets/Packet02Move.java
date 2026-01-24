package net.packets;

import java.io.Serializable;

public class Packet02Move extends Packet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private int x, y;
    private int direction;        // new field
    private int currentAnimation; // new field
    private int currentRoomIndex;

    // Updated constructor
    public Packet02Move(String username, int x, int y, int direction, int currentAnimation, int currentRoom) {
        super(PacketType.MOVE);
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.currentAnimation = currentAnimation;
        this.currentRoomIndex = currentRoom;
    }

    // Getters
    public String getUsername() { return username; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDirection() { return direction; }
    public int getCurrentAnimation() { return currentAnimation; }
    public int getCurrentRoomIndex() { return currentRoomIndex; }
}
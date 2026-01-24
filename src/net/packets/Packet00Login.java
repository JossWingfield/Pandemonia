package net.packets;

import java.io.Serializable;

public class Packet00Login extends Packet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private int x, y;

    public Packet00Login(String username, int x, int y) {
        super(PacketType.LOGIN);
        this.username = username;
        this.x = x;
        this.y = y;
    }

    // No need for the string constructor anymore
    // public Packet00Login(String data) { super(data); }

    // Optional: getters
    public String getUsername() { return username; }
    public int getX() { return x; }
    public int getY() { return y; }

    // Not needed for object streams, but you can keep them for debugging
    @Override
    protected void read(String[] data) { }
    @Override
    protected String writeData() { return username + "," + x + "," + y; }
}
package net.packets;

import java.io.Serializable;

public class Packet04Chat extends Packet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String message;

    public Packet04Chat(String username, String message) {
        super(PacketType.CHAT);
        this.username = username;
        this.message = message;
    }


    public String getUsername() { return username; }
    public String getMessage() { return message; }
}
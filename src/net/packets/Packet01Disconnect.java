package net.packets;

import java.io.Serializable;

public class Packet01Disconnect extends Packet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    public Packet01Disconnect(String username) {
        super(PacketType.DISCONNECT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
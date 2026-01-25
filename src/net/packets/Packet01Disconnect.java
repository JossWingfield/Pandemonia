package net.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.ConnectionState;

public class Packet01Disconnect extends Packet {

    private final String username;

    public Packet01Disconnect(String username) {
        super(PacketType.DISCONNECT);
        this.username = username;
    }

    public Packet01Disconnect(DataInputStream in) throws IOException {
        super(PacketType.DISCONNECT);
        this.username = in.readUTF();
    }

    @Override
    public ConnectionState requiredState() {
        return ConnectionState.IN_GAME;
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        out.writeUTF(username);
    }

    public String getUsername() {
        return username;
    }
}